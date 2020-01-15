package com.daloji.blockchain.network.trame;


import java.util.ArrayDeque;

import org.junit.Assert;
import org.junit.Test;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.peers.PeerNode;


public class DeserializerTrameTest {
	
	private static String trame="F9BEB4D976657273696F6E000000000066000000E853E24E7F1101000D0400000000000034D51C5E00000000000000000000000000000000000000000000FFFF55AA707389C60D04000000000000000000000000000000000000000000000000012A799748954F40102F5361746F7368693A302E31382E302F6159090001F9BEB4D976657261636B000000000000000000005DF6E0E2";

	private static String partial_trame="F9BEB4D976657273696F6E000000000068000000C79722900000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

	@Test
	public  void  deserialiseTest_OK() {
		
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		peer.setPort(8333);
		ArrayDeque<TrameHeader> stackCommand = DeserializerTrame.deserialise(Utils.hexStringToByteArray(trame), peer);
		Assert.assertNotNull(stackCommand); 
		Assert.assertEquals(stackCommand.size(), 2);
		TrameHeader trameheader = stackCommand.pop();
		Assert.assertTrue(trameheader instanceof VersionTrameMessage);
		trameheader = stackCommand.getFirst();
		Assert.assertTrue(trameheader instanceof VersionAckTrame);
		
	}
	
	@Test
	public void deserialiseTestPartialTram() {
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		peer.setPort(8333);
		ArrayDeque<TrameHeader> stackCommand = DeserializerTrame.deserialise(Utils.hexStringToByteArray(partial_trame), peer);
		Assert.assertNotNull(stackCommand); 
		Assert.assertEquals(stackCommand.size(), 1);
		TrameHeader trameheader = stackCommand.pop();
		Assert.assertTrue(trameheader instanceof VersionTrameMessage);

	}
}
