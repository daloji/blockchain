package com.daloji.blockchain.network.trame;

import org.junit.Assert;
import org.junit.Test;

import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.peers.PeerNode;

public class GetAddrTrameTest {
	
	public static final String trame ="F9BEB4D9676574616464720000000000000000005DF6E0E2";

	@Test
	public void GetAddrDeSerialisation() {


		GetAddrTrame getaddr = new GetAddrTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		getaddr.setFromPeer(peer);
		byte[] deserialised = getaddr.deserialise(Utils.hexStringToByteArray(trame));
		Assert.assertEquals(getaddr.getMagic(),"F9BEB4D9");
		Assert.assertEquals(getaddr.getChecksum(),"5DF6E0E2");
		Assert.assertEquals(getaddr.getCommande(),"676574616464720000000000");
		Assert.assertEquals(getaddr.getLength(),0);
		Assert.assertEquals(true,Utils.allZero(deserialised));


	}
}
