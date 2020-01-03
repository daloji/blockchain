package com.daloji.blockchain.network.trame;

import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

public interface TrameSend {
	
	
	public void sendVersionMessage(NetParameters netparameter,PeerNode peer);

}
