package com.daloji.blockchain.network.trame;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;


/**
 * BIP 0152
 * 
 * https://github.com/bitcoin/bips/blob/master/bip-0152.mediawiki
 * 
 * @author daloji
 *
 */
public class SendCmpctTrame extends TrameHeader {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(SendCmpctTrame.class);


	/**
	 * 
	 */
	private static final String commande = "SendCmpct";
	/**
	 *  announce
	 */
	private int index;

	/**
	 *  version
	 */
	private String cmptVersion;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String generatePayload(NetParameters network) {
		// TODO Auto-generated method stub
		return null;
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
			hex = Utils.bytesToHex(buffer);
			long index = Utils.little2big(hex);
			this.setIndex((int)index);
			offset = offset +buffer.length;
			buffer = new byte[8];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			this.setCmptVersion(Utils.bytesToHex(buffer));
			offset = offset +buffer.length;
			buffer = new byte[msg.length-offset];
			System.arraycopy(msg,offset, buffer, 0, buffer.length);
			byte[] info =new byte[offset+(int)length];
			System.arraycopy(msg,0, info, 0, info.length);
			offset = offset + (int)length;
			if(logger.isDebugEnabled()) {
				logger.debug("["+getFromPeer().getHost()+"]"+"<IN> SendCmpct : "+Utils.bytesToHex(info));
			}
		}else {
			this.setPartialTrame(true);
			offset = offset +buffer.length;
			if(offset<msg.length) {
				buffer = new byte[msg.length -offset];
				System.arraycopy(msg,offset, buffer, 0, buffer.length);
			}else {
				buffer = new byte[0];
			}
		}
		return (T) buffer;
	}



	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getCmptVersion() {
		return cmptVersion;
	}

	public void setCmptVersion(String cmptVersion) {
		this.cmptVersion = cmptVersion;
	}

}
