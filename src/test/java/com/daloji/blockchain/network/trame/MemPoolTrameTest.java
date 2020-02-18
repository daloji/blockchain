package com.daloji.blockchain.network.trame;

import org.junit.Assert;
import org.junit.Test;

import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

public class MemPoolTrameTest {

	private static final String trameExpect ="F9BEB4D96d656d706f6f6c0000000000000000005DF6E0E2";

	@Test
	public void memPoolTrameserialiseTest() {
		MemPoolTrame mempooltrame = new MemPoolTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		String message = mempooltrame.generateMessage(NetParameters.MainNet, peer);
		Assert.assertEquals(message,trameExpect);
	}
	
	@Test
	public void memPoolTrameDeserialiseTest() {
		
		MemPoolTrame mempooltrame = new MemPoolTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		mempooltrame.setFromPeer(peer);
		byte[] data = mempooltrame.deserialise(Utils.hexStringToByteArray(trameExpect));
		Assert.assertEquals(mempooltrame.getMagic(),"F9BEB4D9");
		Assert.assertEquals(mempooltrame.getChecksum(),"5DF6E0E2");
		Assert.assertEquals(mempooltrame.getCommande(),"6D656D706F6F6C0000000000");
		Assert.assertEquals(Utils.bytesToHex(data),"");

	}
}
