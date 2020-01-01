package com.daloji.core.blockchain.peers;

import com.daloji.core.blockchain.net.NetParameters;
import com.daloji.core.blockchain.net.PeerNode;

public interface MessageSend {
	
	
	public void sendVersionMessage(NetParameters netparameter,PeerNode peer);

}
