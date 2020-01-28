package com.daloji.blockchain.network.trame;

import org.junit.Test;

import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

public class GetBlocksTrameTest {

	/**
	 * verification getBlockGenesis
	 */
	@Test
	public void getBlockGenesis() {
		GetBlocksTrame getblock = new GetBlocksTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		getblock.setFromPeer(peer);
		String trame = getblock.generateMessage(NetParameters.MainNet, peer);
		byte[] data = Utils.hexStringToByteArray(trame);
		
		//TODO Check response
	}
}
