package com.daloji.blockchain.network.trame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.peers.PeerNode;

/**
 * 
 * Test Unitaire serialisation deserialisation BlockTrame
 * @author daloji
 *
 */
public class BlockTrameTest {
	
	 private static String bloc_receive;
		
		@BeforeClass
		public static void before() throws IOException {
			ClassLoader classLoader = InvTrameTest.class.getClassLoader();
			File file = new File(classLoader.getResource("test.properties").getFile());
			Properties prop = new Properties();
	        // load a properties file
	        prop.load(new FileInputStream(file));
	        bloc_receive = (prop.getProperty("bloc_receive"));
	          
		}
	
	@Test
	public void BlockTrameTestTest() {
		BlockTrame block = new BlockTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		block.setFromPeer(peer);
		byte[] data = block.deserialise(Utils.hexStringToByteArray(bloc_receive));
		Assert.assertEquals(block.getMagic(),"F9BEB4D9");
		Assert.assertEquals(block.getChecksum(),"934D270A");
		Assert.assertEquals(block.getCommande(),"626C6F636B00000000000000");
	   Assert.assertEquals(block.getLength(),215);
	   Assert.assertEquals(block.getMerkelRoot(),"982051FD1E4BA744BBBE680E1FEE14677BA1A3C3540BF7B1CDB606E857233E0E");
	   Assert.assertEquals(block.getnBits(),486604799);
	   Assert.assertEquals(block.getNonce(),2573394689L);
	   Assert.assertEquals(block.getPreviousHash(),"6FE28C0AB6F1B372C1A6A246AE63F74F931E8365E15A089C68D6190000000000");
	   Assert.assertEquals(block.getTime(),1231469665);
	   Assert.assertEquals(block.getVersion(),"01000000");
	   Assert.assertEquals(block.getListTransacation().size(),1);
	   Assert.assertEquals(block.getListTransacation().get(0).getLockTime(),0);
	   Assert.assertEquals(block.getListTransacation().get(0).getTxOutCount(),1);
	   Assert.assertEquals(block.getListTransacation().get(0).getTxOut().size(),1);
	   Assert.assertEquals(block.getListTransacation().get(0).getTxOut().get(0).getPkScript(),"0496B538E853519C726A2C91E61EC11600AE1390813A627C66FB8BE7947BE63C52DA7589379515D4E0A604F8141781E62294721166BF621E73A82CBF2342C858EEAC00");
	   Assert.assertEquals(block.getListTransacation().get(0).getTxOut().get(0).getValue(),"00F2052A01000000");
	   Assert.assertEquals(block.getListTransacation().get(0).getTxOut().get(0).getPkScriptLength(),67);
	   Assert.assertEquals(block.getListTransacation().get(0).getTxIn().size(),1);
	   Assert.assertEquals(block.getListTransacation().get(0).getTxInCount(),1);
	   Assert.assertEquals(block.getListTransacation().get(0).getTxIn().get(0).getHash(),"0000000000000000000000000000000000000000000000000000000000000000");
	   Assert.assertEquals(block.getListTransacation().get(0).getTxIn().get(0).getSequence(),"FFFFFFFF");
	   Assert.assertEquals(block.getListTransacation().get(0).getTxIn().get(0).getSignatureScript(),"04FFFF001D0104");
	   Assert.assertEquals(block.getListTransacation().get(0).getTxIn().get(0).getIndex(),"FFFFFFFF");
	   Assert.assertEquals(block.getListTransacation().get(0).getTxIn().get(0).getSciptLeng(),7);
	   Assert.assertEquals(Utils.allZero(data),true);
	}
}
