package com.daloji.blockchain.network.trame;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

/**
 * 
 * Test Unitaire serialisation deserialisation BlockTrame
 * @author daloji
 *
 */
public class BlockTrameTest {

	private static String bloc_receive;

	private static String bloc_receive_002; 

	private static String bloc_receive_003;

	private static String bloc_receive_004;

	private static String block_receive_005;

	@BeforeClass
	public static void before() throws Exception {
		ClassLoader classLoader = InvTrameTest.class.getClassLoader();
		File file = new File(classLoader.getResource("test.properties").getFile());
		Properties prop = new Properties();
		// load a properties file
		prop.load(new FileInputStream(file));
		bloc_receive = (prop.getProperty("bloc_receive"));
		bloc_receive_002 = (prop.getProperty("bloc_receive_002"));
		bloc_receive_003 = (prop.getProperty("bloc_receive_003"));
		bloc_receive_004 = (prop.getProperty("bloc_receive_004"));
		block_receive_005 = (prop.getProperty("block_receive_005"));
	}

	@Test
	public void blockTrameTestTest() {
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
		Assert.assertEquals(block.getListTransacation().get(0).getTxOut().get(0).getPkScript(),"410496B538E853519C726A2C91E61EC11600AE1390813A627C66FB8BE7947BE63C52DA7589379515D4E0A604F8141781E62294721166BF621E73A82CBF2342C858EEAC");
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

	//@Test
	public void blockTrameTest002() {
		BlockTrame block = new BlockTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		block.setFromPeer(peer);
		byte[] data = block.deserialise(Utils.hexStringToByteArray(bloc_receive_002));
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

	@Test
	public void blockTrameTest003() {
		BlockTrame block = new BlockTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		block.setFromPeer(peer);
		byte[] data = block.deserialise(Utils.hexStringToByteArray(bloc_receive_004));
		Assert.assertEquals(block.getMagic(),"F9BEB4D9");
		Assert.assertEquals(block.getChecksum(),"F72170CE");
		Assert.assertEquals(block.getCommande(),"626C6F636B00000000000000");
		Assert.assertEquals(block.getLength(),216);

	}


	@Test
	public void blockTrameTest005() {
		BlockTrame blockTrame = new BlockTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		blockTrame.setFromPeer(peer);
		byte[] data = blockTrame.deserialise(Utils.hexStringToByteArray(block_receive_005));
		Assert.assertEquals(blockTrame.getMagic(),"F9BEB4D9");
		Assert.assertEquals(blockTrame.getChecksum(),"6053866F");
		Assert.assertEquals(blockTrame.getCommande(),"626C6F636B00000000000000");
		Assert.assertEquals(blockTrame.getLength(),216);
		Assert.assertEquals(blockTrame.getMerkelRoot(),"3559D5EA3C76414688D91E80BBF3CE8362BAC94144E5193DFC25E5E4618881F7");
		Assert.assertEquals(blockTrame.getnBits(),486604799);
		Assert.assertEquals(blockTrame.getNonce(),2864320536L);
		Assert.assertEquals(blockTrame.getPreviousHash(),"69E01E2D00D6F87ADC1F552002ABA2B7DF61AC076DD61861BE09EC7200000000");
		Assert.assertEquals(blockTrame.getTime(),1232006385);
		Assert.assertEquals(blockTrame.getVersion(),"01000000");
		Assert.assertEquals(blockTrame.getListTransacation().size(),1);
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getLockTime(),0);
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOutCount(),1);
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().size(),1);
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getPkScript(),"4104E8DD55CCFFAB2551A9563FC298B464525129B84AB036A36C92E8D1017185280D239DB08EEAD652F6E75C7834021E435C00D0758E0F322DC46116508325DC43A6AC");
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getValue(),"00F2052A01000000");
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getPkScriptLength(),67);
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().size(),1);
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxInCount(),1);
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getHash(),"0000000000000000000000000000000000000000000000000000000000000000");
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSequence(),"FFFFFFFF");
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSignatureScript(),"04FFFF001D02C205");
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getIndex(),"FFFFFFFF");
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSciptLeng(),8);


	}

	@Test
	public void blockTrameTest007() {
		BlockTrame blockTrame = new BlockTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		blockTrame.setFromPeer(peer);
		byte[] data = blockTrame.deserialise(Utils.hexStringToByteArray(block_receive_005));
		Block block = Block.buildFromBlockTrame(blockTrame);


		Assert.assertEquals(blockTrame.getLength(),216);
		Assert.assertEquals(blockTrame.getMerkelRoot(),block.getMerkleRoot());
		Assert.assertEquals(blockTrame.getnBits(),block.getDifficultyTarget());
		Assert.assertEquals(blockTrame.getNonce(),block.getNonce());
		Assert.assertEquals(blockTrame.getPreviousHash(),block.getPrevBlockHash());
		Assert.assertEquals(blockTrame.getTime(),block.getTime());
		Assert.assertEquals(blockTrame.getVersion(),Utils.intHexpadding((int)block.getVersion(),4));

		Assert.assertEquals(blockTrame.getListTransacation().size(),block.getListTransaction().size());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getLockTime(),block.getListTransaction().get(0).getLockTime());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOutCount(),block.getListTransaction().get(0).getTxOutCount());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().size(),block.getListTransaction().get(0).getTxOut().size());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getPkScript(),block.getListTransaction().get(0).getTxOut().get(0).getPkScript());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getValue(),block.getListTransaction().get(0).getTxOut().get(0).getValue());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getPkScriptLength(),block.getListTransaction().get(0).getTxOut().get(0).getPkScriptLength());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().size(),block.getListTransaction().get(0).getTxIn().size());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxInCount(),block.getListTransaction().get(0).getTxIn().size());

		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getHash(),block.getListTransaction().get(0).getTxIn().get(0).getHash());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSequence(),block.getListTransaction().get(0).getTxIn().get(0).getSequence());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSignatureScript(),block.getListTransaction().get(0).getTxIn().get(0).getSignatureScript());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getIndex(),block.getListTransaction().get(0).getTxIn().get(0).getIndex());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSciptLeng(),block.getListTransaction().get(0).getTxIn().get(0).getSciptLeng());

	}

	
	@Test
	public void blockTrameTest008() {
		BlockTrame blockTrame = new BlockTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		blockTrame.setFromPeer(peer);
		byte[] data = blockTrame.deserialise(Utils.hexStringToByteArray(block_receive_005));
		Block block = Block.buildFromBlockTrame(blockTrame);

		blockTrame = Block.buildBlockTrame(block);
		Assert.assertEquals(blockTrame.getMerkelRoot(),block.getMerkleRoot());
		Assert.assertEquals(blockTrame.getnBits(),block.getDifficultyTarget());
		Assert.assertEquals(blockTrame.getNonce(),block.getNonce());
		Assert.assertEquals(blockTrame.getPreviousHash(),block.getPrevBlockHash());
		Assert.assertEquals(blockTrame.getTime(),block.getTime());
		Assert.assertEquals(blockTrame.getVersion(),Utils.intHexpadding((int)block.getVersion(),4));

		Assert.assertEquals(blockTrame.getListTransacation().size(),block.getListTransaction().size());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getLockTime(),block.getListTransaction().get(0).getLockTime());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOutCount(),block.getListTransaction().get(0).getTxOutCount());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().size(),block.getListTransaction().get(0).getTxOut().size());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getPkScript(),block.getListTransaction().get(0).getTxOut().get(0).getPkScript());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getValue(),block.getListTransaction().get(0).getTxOut().get(0).getValue());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getPkScriptLength(),block.getListTransaction().get(0).getTxOut().get(0).getPkScriptLength());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().size(),block.getListTransaction().get(0).getTxIn().size());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxInCount(),block.getListTransaction().get(0).getTxIn().size());

		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getHash(),block.getListTransaction().get(0).getTxIn().get(0).getHash());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSequence(),block.getListTransaction().get(0).getTxIn().get(0).getSequence());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSignatureScript(),block.getListTransaction().get(0).getTxIn().get(0).getSignatureScript());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getIndex(),block.getListTransaction().get(0).getTxIn().get(0).getIndex());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSciptLeng(),block.getListTransaction().get(0).getTxIn().get(0).getSciptLeng());


	}

	@Test
	public void blockTrameTest009() {
		BlockTrame blockTrameExpect = new BlockTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		blockTrameExpect.setFromPeer(peer);
		byte[] data = blockTrameExpect.deserialise(Utils.hexStringToByteArray(block_receive_005));
		Block block = Block.buildFromBlockTrame(blockTrameExpect);
		blockTrameExpect = Block.buildBlockTrame(block);
		String payload = blockTrameExpect.generateMessage(NetParameters.MainNet, peer);
		BlockTrame blockTrame = new BlockTrame();
		blockTrame.setFromPeer(peer);
	    data = blockTrame.deserialise(Utils.hexStringToByteArray(payload));

		
		Assert.assertEquals(blockTrame.getMerkelRoot(),blockTrameExpect.getMerkelRoot());
		Assert.assertEquals(blockTrame.getnBits(),blockTrameExpect.getnBits());
		Assert.assertEquals(blockTrame.getNonce(),blockTrameExpect.getNonce());
		Assert.assertEquals(blockTrame.getPreviousHash(),blockTrameExpect.getPreviousHash());
		Assert.assertEquals(blockTrame.getTime(),blockTrameExpect.getTime());
		Assert.assertEquals(blockTrame.getVersion(),blockTrameExpect.getVersion());

		Assert.assertEquals(blockTrame.getListTransacation().size(),blockTrameExpect.getListTransacation().size());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getLockTime(),blockTrameExpect.getListTransacation().get(0).getLockTime());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOutCount(),blockTrameExpect.getListTransacation().get(0).getTxOutCount());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().size(),blockTrameExpect.getListTransacation().get(0).getTxOut().size());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getPkScript(),blockTrameExpect.getListTransacation().get(0).getTxOut().get(0).getPkScript());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getValue(),blockTrameExpect.getListTransacation().get(0).getTxOut().get(0).getValue());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxOut().get(0).getPkScriptLength(),blockTrameExpect.getListTransacation().get(0).getTxOut().get(0).getPkScriptLength());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().size(),blockTrameExpect.getListTransacation().get(0).getTxIn().size());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxInCount(),blockTrameExpect.getListTransacation().get(0).getTxIn().size());

		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getHash(),blockTrameExpect.getListTransacation().get(0).getTxIn().get(0).getHash());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSequence(),blockTrameExpect.getListTransacation().get(0).getTxIn().get(0).getSequence());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSignatureScript(),blockTrameExpect.getListTransacation().get(0).getTxIn().get(0).getSignatureScript());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getIndex(),blockTrameExpect.getListTransacation().get(0).getTxIn().get(0).getIndex());
		Assert.assertEquals(blockTrame.getListTransacation().get(0).getTxIn().get(0).getSciptLeng(),blockTrameExpect.getListTransacation().get(0).getTxIn().get(0).getSciptLeng());


	}

}
