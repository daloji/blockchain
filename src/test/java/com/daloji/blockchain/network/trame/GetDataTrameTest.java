package com.daloji.blockchain.network.trame;



import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.GetDataTrame;

import junit.framework.Assert;

public class GetDataTrameTest {
  
	private String hash = "6FE28C0AB6F1B372C1A6A246AE63F74F931E8365E15A089C68D6190000000000";

	//@Test
	public void getDataTHeaderTest_OK() {
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		GetDataTrame getData =new GetDataTrame(hash);
		String data = getData.generateMessage(NetParameters.MainNet, peer);
		Assert.assertNotNull(data);
		String magic = data.substring(0, 8);
		String commande =data.substring(8,32);
		String length = data.substring(32, 40);
		length = Utils.StrLittleEndian(length);
		int len = Integer.parseInt(length, 16);
		String checksum = data.substring(40, 48);
		String payload = data.substring(48, data.length()); 
		String protocol = payload.substring(0, 8);
		String Strsize = payload.substring(8, 10);
		String hash = payload.substring(12,payload.length()); 
		int size = Integer.parseInt(Strsize, 16);
		Assert.assertEquals(NetParameters.MainNet.getMagic(), magic);
		Assert.assertEquals("getdata",Utils.hexToAscii(commande));
		Assert.assertEquals(Utils.intHexpadding(70015, 4), protocol);
		Assert.assertEquals(size, 1);
		
	}
	
}
