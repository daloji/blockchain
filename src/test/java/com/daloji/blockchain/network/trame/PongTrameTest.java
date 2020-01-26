package com.daloji.blockchain.network.trame;

import org.junit.Assert;
import org.junit.Test;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

public class PongTrameTest {



	@Test
	public void pongTrameDeserialiseTest() {
		PongTrame pongtrame = new PongTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		pongtrame.setFromPeer(peer);
		String pong = pongtrame.generateMessage(NetParameters.MainNet, peer);
		Assert.assertEquals(pong.substring(0,8),"F9BEB4D9");
		Assert.assertEquals(pong.substring(8,32),"706f6e670000000000000000");
		Assert.assertEquals(pong.substring(32,40),"08000000");
        String nonce = pongtrame.getNonce();
        byte[] array = Crypto.doubleSha256(Utils.hexStringToByteArray(nonce));
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		Assert.assertEquals(pong.substring(40,48),checksum);
		Assert.assertEquals(pong.substring(48,64),nonce);


	}

}
