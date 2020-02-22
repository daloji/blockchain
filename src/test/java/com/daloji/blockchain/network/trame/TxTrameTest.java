package com.daloji.blockchain.network.trame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.peers.PeerNode;

public class TxTrameTest {

	private static  String trame_tx_001;
	
	private static String trame_tx_test;
	
	@BeforeClass
	public static void before() throws IOException {
		ClassLoader classLoader = InvTrameTest.class.getClassLoader();
		File file = new File(classLoader.getResource("test.properties").getFile());
		Properties prop = new Properties();
        // load a properties file
        prop.load(new FileInputStream(file));
        trame_tx_001 = (prop.getProperty("trame_tx_001"));
        trame_tx_test = (prop.getProperty("trame_tx_test"));

	}
	
	@Test
	public void txDesirialise001() {
		TxTrame txTrame = new TxTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		txTrame.setFromPeer(peer);
		byte[] data = txTrame.deserialise(Utils.hexStringToByteArray(trame_tx_001));
		Assert.assertEquals(txTrame.getMagic(),"F9BEB4D9");
		Assert.assertEquals(txTrame.getChecksum(),"D274230C");
		Assert.assertEquals(txTrame.getCommande(),"747800000000000000000000");
		Assert.assertEquals(txTrame.getLength(),226);
		Assert.assertEquals(txTrame.getTransaction().getTxInCount(),1);
		Assert.assertEquals(txTrame.getTransaction().getTxOutCount(),2);
		Assert.assertEquals(txTrame.getTransaction().getTxIn().size(),1);
		Assert.assertEquals(txTrame.getTransaction().getTxOut().size(),2);
		Assert.assertEquals(txTrame.getTransaction().getTxIn().get(0).getHash(),"881AD35B628BE9CEB3D2A44F90E122C10B0C2FD11B3AB51CAA4A0F77F6E6D1DA01000000");
		Assert.assertEquals(txTrame.getTransaction().getTxIn().get(0).getIndex(),null);
		Assert.assertEquals(txTrame.getTransaction().getTxIn().get(0).getSciptLeng(),107);
		Assert.assertEquals(txTrame.getTransaction().getTxIn().get(0).getSequence(),"FFFFFFFD");
		Assert.assertEquals(txTrame.getTransaction().getTxIn().get(0).getSignatureScript(),"4830450221008DD4B719B7316F5D26EE0CC22B3FF05B6F49699EA2E3BCB059C91463C7353B16022042E3A73CE1374D772F03A1F9E3EA5C4CDBC20C9D9AAFA6438EE6ACCC443C10B4012102DE587BA3B6F64E8BD70D8078DBC33FDD80B44024A4D4A1FE6BFF840B4B669B8E");
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(0).getPkScript(),"76A914101CE1EB6EEE436E5753286ECEA48D5B7097BE7D88AC");
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(0).getPkScriptLength(),25);
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(0).getValue(),"3628200000000000");
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(1).getPkScript(),"76A914D90B315D8C162E638A9CF477A602A394123E7F1F88AC");
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(1).getPkScriptLength(),25);
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(1).getValue(),"36CE990900000000");
		Assert.assertEquals(Utils.bytesToHex(data),"C02639254DE25B2BA59D83E9440DBD6E069BFAF53E70B4265001000000D6B429C23E22DB053CCB332D7367C85C98ED1AF7E3F8C0A5ED9D8A4CD558CDEE01000000C917BF90FB81468351F449A01DD4C91057748F5052E0337023834731A088371D010000003DEC7271B572D909641B76B33CBFEF5EF0D77874BF748FB4B0B838796E8BDE8B01000000E02B2A71F36D85792620A31928AE4CB10EA625D5CBE44646F5DC258B60C55A0801000000901863E58DC7CD47216E6D008E798DFFBE932A47AEF807D8BA884917F39B7A66000000");
		Assert.assertEquals(txTrame.getTransaction().getLockTime(),618482);


	}
	
	@Test
	public void txDesirialise002() {
		TxTrame txTrame = new TxTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		txTrame.setFromPeer(peer);
		byte[] data = txTrame.deserialise(Utils.hexStringToByteArray(trame_tx_test));
		Assert.assertEquals(txTrame.getMagic(),"F9BEB4D9");
		Assert.assertEquals(txTrame.getChecksum(),"E293CDBE");
		Assert.assertEquals(txTrame.getCommande(),"747800000000000000000000");
		Assert.assertEquals(txTrame.getLength(),258);
		Assert.assertEquals(txTrame.getTransaction().getTxInCount(),1);
		Assert.assertEquals(txTrame.getTransaction().getTxOutCount(),2);
		//Assert.assertEquals(txTrame.getTransaction().getTxIn().size(),1);
		//Assert.assertEquals(txTrame.getTransaction().getTxOut().size(),2);
		Assert.assertEquals(txTrame.getTransaction().getTxIn().get(0).getHash(),"6DBDDB085B1D8AF75184F0BC01FAD58D1266E9B63B50881990E4B40D6AEE362900000000");
		Assert.assertEquals(txTrame.getTransaction().getTxIn().get(0).getIndex(),null);
		Assert.assertEquals(txTrame.getTransaction().getTxIn().get(0).getSciptLeng(),139);
		Assert.assertEquals(txTrame.getTransaction().getTxIn().get(0).getSequence(),"FFFFFFFF");
		Assert.assertEquals(txTrame.getTransaction().getTxIn().get(0).getSignatureScript(),"483045022100F3581E1972AE8AC7C7367A7A253BC1135223ADB9A468BB3A59233F45BC578380022059AF01CA17D00E41837A1D58E97AA31BAE584EDEC28D35BD96923690913BAE9A0141049C02BFC97EF236CE6D8FE5D94013C721E915982ACD2B12B65D9B7D59E20A842005F8FC4E02532E873D37B96F09D6D4511ADA8F14042F46614A4C70C0F14BEFF5");
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(0).getPkScript(),"76A9141AA0CD1CBEA6E7458A7ABAD512A9D9EA1AFB225E88AC");
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(0).getPkScriptLength(),25);
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(0).getValue(),"404B4C0000000000");
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(1).getPkScript(),"76A9140EAB5BEA436A0484CFAB12485EFDA0B78B4ECC5288AC");
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(1).getPkScriptLength(),25);
		Assert.assertEquals(txTrame.getTransaction().getTxOut().get(1).getValue(),"80FAE9C700000000");
		Assert.assertEquals(Utils.bytesToHex(data),"");
		Assert.assertEquals(txTrame.getTransaction().getLockTime(),0);
	}
	
}
