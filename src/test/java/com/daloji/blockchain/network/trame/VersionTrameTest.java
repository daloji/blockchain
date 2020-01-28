package com.daloji.blockchain.network.trame;

import org.junit.Assert;
import org.junit.Test;

import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.VersionTrameMessage;



public class VersionTrameTest {

	private static String trame="F9BEB4D976657273696F6E000000000066000000E853E24E7F1101000D0400000000000034D51C5E00000000000000000000000000000000000000000000FFFF55AA707389C60D04000000000000000000000000000000000000000000000000012A799748954F40102F5361746F7368693A302E31382E302F6159090001F9BEB4D976657261636B000000000000000000005DF6E0E2";

	private static String partial_trame="F9BEB4D976657273696F6E000000000068000000C79722900000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

	@Test
	public  void checkVersionDeserialisation() {

		VersionTrameMessage version = new VersionTrameMessage(false);
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		version.setFromPeer(peer);
		byte[] data = version.deserialise(Utils.hexStringToByteArray(trame));
		long epoch = Utils.little2big("34D51C5E");
		Assert.assertEquals(version.getService(),"0D04000000000000");
		Assert.assertEquals(version.getEpoch(), epoch);
		Assert.assertEquals(version.getAddressTrans(), "0D0400000000000000000000000000000000000000000000");
		Assert.assertEquals(version.getAddressReceive(), "000000000000000000000000000000000000FFFF55AA7073");
		Assert.assertEquals(version.getUserAgent(), Utils.hexToAscii("2F5361746F7368693A302E31382E302F"));
		Assert.assertEquals(version.getNonce(), "012A799748954F40");
		Assert.assertEquals(version.isRelay(), true);
		Assert.assertEquals(version.isPartialTrame(), false);
		Assert.assertEquals(Utils.allZero(data), false);

	}

	@Test
	public void checkPartialVersionDeserialisation() {
		VersionTrameMessage version = new VersionTrameMessage(false);
		version.deserialise(Utils.hexStringToByteArray(partial_trame));
		Assert.assertEquals(version.isPartialTrame(), true);

	}

	@Test
	public void checkSerialisation() {

		String host = "127.0.0.1";
		int port = 8333;
		VersionTrameMessage version = new VersionTrameMessage(false);
		PeerNode peer =new PeerNode(IPVersion.IPV4);
		peer.setHost(host);
		version.setAddressReceive(host);
		version.setPortReceive(port);
		version.setAddressTrans(host);
		version.setPortTrans(port);
		version.setNonce("012A799748954F40");
		version.setUserAgent("/Satoshi:0.18.0/");
		//version.setService(service);
		String trame=version.generateMessage(NetParameters.MainNet, peer);
		Assert.assertEquals(version.getUserAgent(), Utils.hexToAscii("2F5361746F7368693A302E31382E302F"));
		Assert.assertEquals(version.getNonce(), "012A799748954F40");

	}
}
