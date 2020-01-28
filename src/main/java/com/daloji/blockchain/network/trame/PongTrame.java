package com.daloji.blockchain.network.trame;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class PongTrame extends TrameHeader{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) LoggerFactory.getLogger(PingTrame.class);

	private String cmd = "pong";
	
	private String nonce ;
	
	

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	@Override
	public String generatePayload(NetParameters network) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T deserialise(byte[] msg) {
		// TODO Auto-generated method stub
		return null;
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
		nonce = Utils.generateNonce(16);
		byte[] array = Crypto.doubleSha256(Utils.hexStringToByteArray(nonce));
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		msg = msg +checksum;
		msg = msg +nonce;
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
		result = prime * result + ((nonce == null) ? 0 : nonce.hashCode());
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
		PongTrame other = (PongTrame) obj;
		if (cmd == null) {
			if (other.cmd != null)
				return false;
		} else if (!cmd.equals(other.cmd))
			return false;
		if (nonce == null) {
			if (other.nonce != null)
				return false;
		} else if (!nonce.equals(other.nonce))
			return false;
		return true;
	}
 
	 
}
