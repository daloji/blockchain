package com.daloji.blockchain.network.trame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

public class GetAddrTrame extends TrameHeader{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final Logger logger =  LoggerFactory.getLogger(GetAddrTrame.class);

	private static final String commande = "getaddr";

	@Override
	public String generatePayload(NetParameters network) {
		String message ="";
		message = message + getMagic();
		message = message + Utils.convertStringToHex(commande,12);
		byte[] payload = new byte[0]; 
		byte[] array = Crypto.doubleSha256(payload);
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		message = message + "00000000" +checksum;
		return message;
	}

	@Override
	public <T> T deserialise(byte[] msg) {
		byte[] buffer = new byte[4];
		int offset = 0;
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setMagic(Utils.bytesToHex(buffer));
		offset = offset + buffer.length;
		buffer = new byte[12];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setCommande(Utils.bytesToHex(buffer));
		offset = offset + buffer.length;
		buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		String hex = Utils.bytesToHex(buffer);
		long length=Utils.little2big(hex);
		this.setLength((int)length);
		offset = offset + buffer.length;
		buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setChecksum(Utils.bytesToHex(buffer));
		offset = offset + buffer.length;
		byte[] info =new byte[offset];
		System.arraycopy(msg,0, info, 0, info.length);
		if(logger.isDebugEnabled()) {
			logger.debug("["+getFromPeer().getHost()+"]"+"<IN> getAddr "+Utils.bytesToHex(info));
		}
		if(offset<msg.length) {
			buffer = new byte[msg.length-offset];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);

		}else {
			buffer = new byte[0];
		}

		return (T) buffer;
	}

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		setMagic(network.getMagic());
		String message =generatePayload(network);
		return message;
	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

}
