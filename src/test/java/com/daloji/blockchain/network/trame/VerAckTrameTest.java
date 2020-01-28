package com.daloji.blockchain.network.trame;

import org.junit.Assert;
import org.junit.Test;

import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.VersionAckTrame;

public class VerAckTrameTest {

	public static final String trame ="F9BEB4D976657261636B000000000000000000005DF6E0E2";
	
	@Test
	public void checkVerAckDeSerialisation() {
			
		
		VersionAckTrame verack = new VersionAckTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		verack.setFromPeer(peer);
		byte[] deserialised = verack.deserialise(Utils.hexStringToByteArray(trame));
		Assert.assertEquals(verack.getMagic(),"F9BEB4D9");
		Assert.assertEquals(verack.getChecksum(),"5DF6E0E2");
		Assert.assertEquals(verack.getCommande(),"76657261636B000000000000");
		Assert.assertEquals(verack.getLength(),0);
		Assert.assertEquals(true,Utils.allZero(deserialised));


	}
}
