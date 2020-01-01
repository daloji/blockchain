package com.daloji.core.blockchain.peers;

import com.daloji.core.blockchain.net.NetParameters;
import com.daloji.core.blockchain.net.PeerNode;

public class AddrTrame  extends MessageHeader{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8871174569589589645L;

	@Override
	public <T> T deserialise(byte[] msg) {
		// TODO Auto-generated method stub
		return null;
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

}
