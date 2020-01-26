package com.daloji.blockchain.network.trame;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class PingTrame extends TrameHeader{

	private static final Logger logger = (Logger) LoggerFactory.getLogger(PingTrame.class);

	private String cmd = "ping";
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
		buffer = new byte[(int)length];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		String payload = Utils.bytesToHex(buffer);
		byte[] info =new byte[offset+(int)length];
		System.arraycopy(msg,0, info, 0, info.length);
		offset = offset + (int)length;
		if(logger.isDebugEnabled()) {
		logger.debug("["+getFromPeer().getHost()+"]"+"<IN> Ping : "+Utils.bytesToHex(info));
		}
		if(Utils.allZero(Utils.hexStringToByteArray(payload))){
			this.setPartialTrame(true);
			buffer = new byte[0];
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
		String msg = "";
		setMagic(network.getMagic());
		setCommande(Utils.convertStringToHex(cmd,12));
		setLength(8);
		msg = msg + network.getMagic();
		msg = msg + getCommande();
		msg = msg + Utils.intHexpadding(getLength(), 4);
		String payload = Utils.generateNonce(8);
		byte[] array = Crypto.doubleSha256(Utils.hexStringToByteArray(payload));
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		msg = msg +checksum;
		msg = msg +payload;
		return msg;

	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cmd == null) ? 0 : cmd.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PingTrame other = (PingTrame) obj;
		if (cmd == null) {
			if (other.cmd != null)
				return false;
		} else if (!cmd.equals(other.cmd))
			return false;
		return true;
	}

	
	
}
