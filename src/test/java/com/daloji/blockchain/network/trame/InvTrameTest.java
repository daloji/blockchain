package com.daloji.blockchain.network.trame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.daloji.blockchain.core.InvType;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.IPVersion;
import com.daloji.blockchain.network.peers.PeerNode;
/**
 * Test unitaire des Messages Inventory
 * @author daloji
 *
 */

public class InvTrameTest {

	
	private static String trame_003; 
	
	private static String trame;
	
	private static String trame_002;
	
	private static String trame_004;
	
	private static String trame_001;
	
	private static String trame_receive;
	
	

	
	@BeforeClass
	public static void before() throws IOException {
		ClassLoader classLoader = InvTrameTest.class.getClassLoader();
		File file = new File(classLoader.getResource("test.properties").getFile());
		Properties prop = new Properties();
        // load a properties file
        prop.load(new FileInputStream(file));
        trame_003 = (prop.getProperty("trame_003"));
        trame = (prop.getProperty("trame"));
        trame_002 = (prop.getProperty("trame_002"));
        trame_001 = (prop.getProperty("trame_001"));
        trame_receive = (prop.getProperty("trame_receive"));
        trame_004  = (prop.getProperty("trame_004"));
        
	}
	
	@Test
	public void checkInVDeserialize() {
	
		String trame_expect ="D452634A9C71B3E20AF038A901000000A05F41EA960807974BE85FD6520BEE9177E287AC7A31E0C062F917AD435BB125010000006290DDD0B98B1CF767B60596B068AE36174B736650E4CEF332B930D7FACA3189010000005E76EAB60EE563A189734CA28345C993EC77A774A87711E720B01E745772D854010000006F496BFEA38BDB69CC582B5C71882219BDC5EB23F643A984E853C2ADDAC7644E0100000068DC477ACCB3C146892984C9BB7B6021318975CA882A9E6848C9E761BE7CA29E00000000CCC73856DA203FFFA5EFA518BCB69E6400BFB74063150A000000000000000000CF3232BDADD77E973903BC3C852BC3F3E8D7600BADC90100000000000000000054E2DDAB50DF94BE5CAA7D2B972B905F977ECB0683BC11000000000000000000BCF401E558439D7452CB9580BFD5FA8C848D84A896E500000000000000000000143EA05FD3B0B2C188CFBFB69D28E983E28288466DCB0B0000000000000000009AAB3A81A02BA4EB6F20653DC2C99229B551D967C203090000000000000000008BA45E9F6479A3246FD1ADD7F4B1D668F45B999F4D49030000000000000000003637090F5D8DD9DAABCA2FE9B15D544A144BD39B319A0300000000000000000059DDCC9AD4C601DD2A3EDC9D75302B90C1E801D0CE7F040000000000000000001F26170A2266160E7250DB36ED1B0F098BE656B14E550200000000000000000000F556AC25C083BFC0F5EEEA512C12DC902BBC3AF4AA09000000000000000000032E8CF7C32F5C06083E36D9D6AB968BBDE101DBAA87080000000000000000009E7897A420EC12CA87C4B4A99D73D7C423357FA4A0530C00000000000000000038763E7001A41E3276A985FD05B1C6FC36E77D52F2B8030000000000000000000BCC8EA6F45E0668F7600F4FE75074B9D9834B79F6F6000000000000000000005A184BFAA94F2CF568032584986238864E563E9D42F10400000000000000000018E50DFC7FE3D89B2BB7CE94AC12E8BAA1B0EC2BC2B0090000000000000000003D3D36E4873C2BA95520C83628350AF5B73FB27A5F8C1D000000000000000000D227187EC82A54AD0E231FBF7B3B839A81DB0F35591711000000000000000000CF16C235F2F7D92298BB00437A734E3DD7DED5346B28CE020000000000000000406F78B07B46C4022A6C441918A398FC66E6CB6D91C1EC332F140C00000000006FE28C0AB6F1B372C1A6A246AE63F74F931E8365E15A089C68D61900";
		InvTrame inv = new InvTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		inv.setFromPeer(peer);
		byte[] data = inv.deserialise(Utils.hexStringToByteArray(trame));
		Assert.assertEquals(inv.getMagic(),"F9BEB4D9");
		Assert.assertEquals(inv.getChecksum(),"7AD3FF67");
		Assert.assertEquals(inv.getCommande(),"696E76000000000000000000");
		Assert.assertEquals(inv.getLength(),73);
		Assert.assertEquals(inv.getListinv().size(),2);
		Assert.assertEquals(inv.getListinv().get(0).getType(),InvType.MSG_TX);
		Assert.assertEquals(inv.getListinv().get(1).getType(),InvType.MSG_TX);
		Assert.assertEquals(inv.getListinv().get(0).getHash(),"AB42BC3F30DA5F77D8FF57B1E4C73751789C6BD0C4E2DA2F0EC6B91BBE4B7B66");
		Assert.assertEquals(inv.getListinv().get(1).getHash(),"A1427571FA0428C4E98B933BD12AB3836151264A1EDDE354A7FEB39B095C03C6");
		Assert.assertEquals(Utils.bytesToHex(data),trame_expect);
	}


	@Test
	public void checkInVDeserialize001() {
		InvTrame inv = new InvTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		inv.setFromPeer(peer);
		byte[] data = inv.deserialise(Utils.hexStringToByteArray(trame_003));
		Assert.assertEquals(inv.getMagic(),"F9BEB4D9");
		Assert.assertEquals(inv.getChecksum(),"17AAE3B8");
		Assert.assertEquals(inv.getCommande(),"696E76000000000000000000");
		Assert.assertEquals(inv.getLength(),1261);
		Assert.assertEquals(inv.getListinv().size(),35);
		Assert.assertEquals(inv.getListinv().get(0).getType(),InvType.MSG_TX);
		Assert.assertEquals(inv.getListinv().get(1).getType(),InvType.MSG_TX);
		Assert.assertEquals(inv.getListinv().get(0).getHash(),"42DC3BDFED092E1840FD35AD41018FE7B44E4445863F43A5B5DADFB44E4103A3");
		Assert.assertEquals(inv.getListinv().get(1).getHash(),"9174F6B792E5A78FD79E5A3850C141DBF10D1EF7FA491B8BE303822CA9DEF00C");
		Assert.assertEquals(Utils.allZero(data),true);
	}
	@Test
	public void checkInVDeserialize002() {

		InvTrame inv = new InvTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		inv.setFromPeer(peer);
		byte[] data = inv.deserialise(Utils.hexStringToByteArray(trame_001));
		Assert.assertEquals(inv.getMagic(),"F9BEB4D9");
		Assert.assertEquals(inv.getChecksum(),"A3935E2B");
		Assert.assertEquals(inv.getCommande(),"696E76000000000000000000");
		Assert.assertEquals(inv.getLength(),1117);
		Assert.assertEquals(inv.getListinv().size(),0);
		Assert.assertEquals(Utils.allZero(data),false);
		//Assert.assertEquals(Utils.bytesToHex(data),trame_receive);

	}
	@Test
	public void checkInvDeserialize003() {
		
		InvTrame inv = new InvTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		inv.setFromPeer(peer);
		byte[] data = inv.deserialise(Utils.hexStringToByteArray(trame_002));
		Assert.assertEquals(inv.getMagic(),"F9BEB4D9");
		Assert.assertEquals(inv.getChecksum(),"25173C57");
		Assert.assertEquals(inv.getCommande(),"696E76000000000000000000");
		Assert.assertEquals(inv.getLength(),18003);
		Assert.assertEquals(inv.getListinv().size(),499);
		Assert.assertEquals(Utils.allZero(data),false);
		Assert.assertEquals(Utils.bytesToHex(data),"DB773C8F3B90EFA51D8E40291406897062C164DFF617D2A7BF64F64F");

	}
	
	/**
	 * cas reception de plus de 50 messages Inventory
	 */
	@Test
	public void checkInvDeserialize004() {
		
		InvTrame inv = new InvTrame();
		PeerNode peer = new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		inv.setFromPeer(peer);
		byte[] data = inv.deserialise(Utils.hexStringToByteArray(trame_004));
		Assert.assertEquals(inv.getMagic(),"F9BEB4D9");
		Assert.assertEquals(inv.getChecksum(),"25173C57");
		Assert.assertEquals(inv.getCommande(),"696E76000000000000000000");
		Assert.assertEquals(inv.getLength(),18003);
		Assert.assertEquals(inv.getListinv().size(),41);
		Assert.assertEquals(Utils.allZero(data),true);


	}

}
