package com.daloji.blockchain.network.trame;



import org.junit.Assert;
import org.junit.Test;

import com.daloji.blockchain.core.InvType;
import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;


public class GetDataTrameTest {
  
	private String hash = "6FE28C0AB6F1B372C1A6A246AE63F74F931E8365E15A089C68D6190000000000";

	@Test
	public void getDataTHeaderTest_OK() {
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		Inventory inv = new Inventory();
		inv.setHash(hash);
		inv.setType(InvType.MSG_BLOCK);
		GetDataTrame getData =new GetDataTrame(inv);
		getData.setFromPeer(peer);
		String data = getData.generateMessage(NetParameters.MainNet, peer);
		Assert.assertNotNull(data);
		
		GetDataTrame getData1 =new GetDataTrame(null);
		getData1.setFromPeer(peer);
		byte[] value = getData1.deserialise(Utils.hexStringToByteArray(data));
		Assert.assertNotNull(data);
		Assert.assertEquals(getData1.getMagic(),"F9BEB4D9");
		Assert.assertEquals(getData1.getChecksum(),"2799A1C8");
		Assert.assertEquals(getData1.getCommande(),"676574646174610000000000");
		Assert.assertEquals(getData1.getLength(),37);
		Assert.assertEquals(getData1.getListInv().size(),1);
		Assert.assertEquals(getData1.getListInv().get(0).getHash(),hash);
		Assert.assertEquals(Utils.allZero(value),true);

		
	}
	
}
