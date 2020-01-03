package com.daloji.blockchain.network.trame;

import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

public class AddrTrame  extends TrameHeader{

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
