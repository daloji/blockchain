package com.daloji.blockchain.network.trame;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Addr;
import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.InvType;
import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class AddrTrame  extends TrameHeader{


	private static final Logger logger = (Logger) LoggerFactory.getLogger(VersionAckTrame.class);

	private static final String commande="addr";
	
	private List<Addr> listAddr;


	private static final int maxAddr = 100;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8871174569589589645L;

	

	
	public List<Addr> getListAddr() {
		return listAddr;
	}

	public void setListAddr(List<Addr> listAddr) {
		this.listAddr = listAddr;
	}

	@Override
	public <T> T deserialise(byte[] msg) {
		int offset =0;
		byte[] buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setMagic(Utils.bytesToHex(buffer));
		offset = offset +  buffer.length;
		buffer = new byte[12];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setCommande(Utils.bytesToHex(buffer));
		offset = offset +buffer.length;
		buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		String hex = Utils.bytesToHex(buffer);
		long length = Utils.little2big(hex);
		this.setLength((int)length);
		offset = offset +buffer.length;
		buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setChecksum(Utils.bytesToHex(buffer));
		offset = offset +buffer.length;
		buffer = new byte[(int)length];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		String payload = Utils.bytesToHex(buffer);
		if(!Utils.allZero(Utils.hexStringToByteArray(payload))){
			buffer = new byte[1];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			String len = Utils.bytesToHex(buffer);
			long size = Integer.parseInt(len,16);
			if(size<Utils.FD_HEXA) {
				buffer = new byte[1];
				offset = offset + buffer.length;				
			}else if(size>=Utils.FD_HEXA && size<=Utils.FFFF_HEXA) {
				buffer = new byte[2];
				offset = offset + 1;
				System.arraycopy(msg, offset, buffer, 0,buffer.length);
				len = Utils.bytesToHex(buffer);
				size = Integer.parseInt(len,16);
				offset = offset + buffer.length;	
			}else if(size>Utils.FFFF_HEXA) {
				buffer = new byte[4];
				offset = offset + 1;
				System.arraycopy(msg, offset, buffer, 0,buffer.length);
				len = Utils.bytesToHex(buffer);
				size = Integer.parseInt(len,16);
				offset = offset + buffer.length;
			}
			listAddr = new ArrayList<Addr>();
			
			for(int i=0 ; i<(int)size;i++) {
				Addr addr = new Addr();
				buffer = new byte[4];
				System.arraycopy(msg, offset, buffer, 0,buffer.length);
				String epoch = Utils.bytesToHex(buffer);
				long unixTime = Utils.little2big(epoch);
				addr.setEpoch(unixTime);
				offset = offset + buffer.length;
				buffer = new byte[8];
				System.arraycopy(msg, offset, buffer, 0,buffer.length);
				offset = offset + buffer.length;
				addr.setService(Utils.bytesToHex(buffer));
				buffer = new byte[16];
				System.arraycopy(msg, offset, buffer, 0,buffer.length);
				offset = offset + buffer.length;
				addr.setIp(Utils.bytesToHex(buffer));
				buffer = new byte[2];
				System.arraycopy(msg, offset, buffer, 0,buffer.length);
				offset = offset + buffer.length;
				addr.setPort(Utils.bytesToHex(buffer));
				listAddr.add(addr);
			}
			byte[] info =new byte[offset];
			System.arraycopy(msg,0, info, 0, info.length);
			logger.info("["+getFromPeer().getHost()+"]"+"<IN> Addr : "+Utils.bytesToHex(info));
			if(offset<msg.length) {
				buffer = new byte[msg.length-offset];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);

			}else {
				buffer = new byte[0];
			}

			
		}else {
			this.setPartialTrame(true);
			buffer = new byte[0];
		}
		return (T) buffer;
	}

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		setMagic(network.getMagic());
		//get Epoch Time
		epoch  = Instant.now().getEpochSecond();
		setAddressTrans(peer.getHost());
		String message = generatePayload(network);
		return message;
	}


	

	public String createPayload() {
		String payload ="";
		//nb addr
		payload = payload + Utils.intHexpadding(maxAddr, 0);
		payload = payload +Utils.convertDateString(epoch, 8);
		logger.debug("timestamp  :"+Utils.convertDateString(epoch, 8));
		payload = payload +Utils.intHexpadding(1, 8);
		logger.debug("service  :"+Utils.intHexpadding(1, 8));	
		//adresse IP source
		String ipsource = Utils.convertIPtoHex(getAddressReceive());
		logger.debug("adresseIP source  :"+ipsource);	
		payload = payload + IP_CONST +ipsource;
		payload =payload + Integer.toHexString(port);
		logger.debug("port  :"+Integer.toHexString(port));
		return payload;
	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generatePayload(NetParameters network) {
		logger.debug("construction addr Trame");	
		String message ="";
		message = message + getMagic();
		logger.debug("magic :"+getMagic());
		message = message + Utils.convertStringToHex(commande,12);
		logger.debug("commande :"+Utils.convertStringToHex(commande,12));	
		logger.debug("version  :"+Utils.intHexpadding(1, 8));
		String payload = createPayload();
		int length =payload.length()/2;
		logger.debug("length :"+length);	
		message=message+Utils.intHexpadding(length, 4);
		byte[] payloadbyte =Utils.hexStringToByteArray(payload);
		//4 premier octet seulement pour le chechsum
		byte[] array =Crypto.doubleSha256(payloadbyte);
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		logger.debug("checksum : "+checksum);	
		message = message +checksum;
		//ajout de la payload 
		message = message +payload;
		logger.debug("Addr envoyé : "+message);	
		return message;
	}


}
