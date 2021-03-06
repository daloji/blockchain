package com.daloji.blockchain.network.trame;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Addr;
import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.DnsLookUp;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;


/**
 *  Trame reseau qui list l'ensemble des noeuds connus par le noeud
 * 
 * @author daloji
 *
 */
public class AddrTrame  extends TrameHeader{


	private static final Logger logger = LoggerFactory.getLogger(AddrTrame.class);

	private static final String commande="addr";

	private List<Addr> listAddr;


	private static final int maxAddr = 100;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8871174569589589645L;


	public AddrTrame(boolean getIp) {
		if(getIp) {
			getExternalIp();
		}
	}

	public List<Addr> getListAddr() {
		return listAddr;
	}

	public void setListAddr(List<Addr> listAddr) {
		this.listAddr = listAddr;
	}

	@Override
	public <T> T deserialise(byte[] msg) {
		byte[] buffer = deserialiseHeader(msg);
		int offset =24;
	    if(buffer!=null) {
	    	String payload = Utils.bytesToHex(buffer);
	    	if(!Utils.allZero(Utils.hexStringToByteArray(payload))){
				buffer = new byte[1];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				String len = Utils.bytesToHex(buffer);
				long size = Integer.parseInt(len,16);
				Pair<Long, Integer> compactsize = Utils.getCompactSize(size, offset, msg);
				size = compactsize.first;
				offset = compactsize.second;
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
					try {
						String ip = Utils.convertHexaToIp(buffer);
						addr.setIp(ip);
					} catch (Exception e) {
						addr.setIp("0.0.0.0");
					}

					buffer = new byte[2];
					System.arraycopy(msg, offset, buffer, 0,buffer.length);
					offset = offset + buffer.length;
					String strport = Utils.bytesToHex(buffer);
					int port =Integer.parseInt(strport,16);
					addr.setPort(port);
					listAddr.add(addr);
				}
				byte[] info =new byte[offset];
				System.arraycopy(msg,0, info, 0, info.length);
				if(logger.isDebugEnabled()) {
					String extractZero =Utils.bytesToHex(info);
					extractZero = Utils.deleteEndZero(extractZero);
					logger.debug("["+getFromPeer().getHost()+"]"+"<IN> Addr : "+extractZero);
				}
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
		List<PeerNode> listnode =DnsLookUp.getInstance().getAllKnownPeer();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		if(getAddressReceive()==null) {
			getExternalIp();
		}
		peer.setHost(getAddressReceive());
		peer.setPort(8333);
		listnode.add(peer);
		//nb addr
		payload = payload + Utils.intHexpadding(listnode.size(), 0);
		for(PeerNode peernode:listnode) {
			payload = payload +Utils.convertDateString(epoch, 4);
			payload = payload +Utils.intHexpadding(1, 8);
			payload = payload + IP_CONST +Utils.convertIPtoHex(peernode.getHost());
			payload =payload + Integer.toHexString(peernode.getPort());
		}
		return payload;
	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generatePayload(NetParameters network) {
		String message ="";
		message = message + getMagic();
		message = message + Utils.convertStringToHex(commande,12);
		String payload = createPayload();
		int length =payload.length()/2;
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
