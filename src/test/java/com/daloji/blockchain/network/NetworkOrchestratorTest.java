package com.daloji.blockchain.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.peers.PeerNode;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.crypto.*","javax.security.auth.*"})
@PrepareForTest({ConnectionNode.class,NetworkOrchestrator.class,DnsLookUp.class,Socket.class,DataInputStream.class,DataOutputStream.class})
public class NetworkOrchestratorTest {

	@MockStrict
	private DnsLookUp dnsLookup;


	@MockStrict
	private Socket socket;

	@MockStrict
	private DataOutputStream dataouput;

	@MockStrict
	private DataInputStream datainput;

	@Before
	public void beforeTest() {
		PowerMock.resetAll();
		PowerMock.mockStaticStrict(DnsLookUp.class);
		PowerMock.mockStaticStrict(Socket.class);
		PowerMock.mockStaticStrict(DataOutputStream.class);
		PowerMock.mockStaticStrict(DataInputStream.class);
	}
	/**
	 * Erreur DnsLookup
	 * @throws Exception
	 */
	@Test
	public void startNEtworkOrchestrator_NOK_001() throws Exception {

		Pair<Retour, List<PeerNode>> dnslookup = new Pair<Retour, List<PeerNode>>(Utils.createRetourNOK("diagnostic", "info"), null);
		EasyMock.expect(DnsLookUp.getInstance()).andReturn(dnsLookup);
		EasyMock.expect(dnsLookup.getAllNodePeer()).andReturn(dnslookup);
		PowerMock.replayAll();
		NetworkOrchestrator network = new NetworkOrchestrator();
		network.onStart();
		PowerMock.verify();
	
	}
	
	@Test
	public void startNEtworkOrchestrator_OK_001() throws Exception {
		String trameVersionReceive = "F9BEB4D9696E76000000000000000000ED04000017AAE3B8230100000042DC3BDFED092E1840FD35AD41018FE7B44E4445863F43A5B5DADFB44E4103A3010000009174F6B792E5A78FD79E5A3850C141DBF10D1EF7FA491B8BE303822CA9DEF00C010000002C1EE4C9508B3E20B4C98107EFBBB9F991D70091B5842F3A4E069930B255EE9E010000004317A27C3FCBE1AA158C551927470736E37B19EECF27B9542C276804181159AC01000000D70F41CB8907FF5770C532894B123D5FC37A287DE9BD0AFCF6DD8885F44832220100000085B7CDC37BA4E7CED3B5C3AFFF7080FA44A3900362144F45A7B908BB4D0B84CC01000000C1C3224021348286A1ACF6D85BB1CDD714981A8BA0B019A890F7DAB592873C0801000000E6BC00AA91616A8563BF245A06EDB973C82F3CE99FC34F2B3338EE3D2B84BF6D01000000232E4EB54AB62D98A1C9111E7A220B23B5CA8629A95CE50F98F916FD6245D785010000002E5990FF5653FF51B903FAF47684FD4FFD9F20862E447FB1E62683793BFB6FCA010000006020600C00B411A8B3908202BF8848ED201DEB8644DE7A6DC5B0DC67EDEAFD520100000031DD10CDA22E0D0E487E02CB69B38A2550FDEED79976225E23A6E97B276619920100000086FC24B797215D060D2E08263DB198DBA7C7FB1BB106F66ADD587B6EF098EFC30100000076AA4C8D9E617DDF6773AFEC1A5E0E64F59CF152C48209392DCC354F2D7DD9A901000000D935987BF5BBC5D2D08169AB374FC9288F4E9302B372D03A2FCF51BDE341103A01000000808C13506F48867F8170DF9F28A010DA52EB46B1949963F8D471A97A122E296E010000009C0DB0833A4B6F10DEF45AD7CF0654EEB6378196EB394DEE991931C43C23ECE2010000002B6C411732A519FD53A1BEC83AD406D021D194BCE4BAB02CA6B6250F7D868B9401000000D2DC2E91700C2C442DE4A7F7BFC5ED30E2642C4836A7D5CFECC57E018E35E9EF01000000A397A296C4AFD455D5F5644E00A1BD877696341105B1CD52A4B6A4CFFC406300010000002D438B67C6A547AF95A42B05C9F75C3C4445CDD9E1FE924ADB54F68C71C5DB5901000000B2570A1F525B6C42FD50FEB71FB44AE7E723137E39007D5DBE36CC7A069081C50100000033B001E080A4DAE14027B13FE4E1173994CFEF15165D1BE368B3B97B87E4847B0100000032DD4CA986DBC7D9A06040DCFC3E5A40D42DDAFAC5B98C6FFE8D6B40DAAB809D0100000018743A85EFE50211E89C37BF7BE0740A0F3E8F0B1E6B50D39D54164C7FCED7B801000000168F1C65761DE80E829726F6ED9F86EB4464EAA293D7B97273412962E0D49D810100000014D57ED022AE4F5CE1A1A81C36E391897B4321E11E05441C20C3D17675B1721801000000F4391CBDB45E1EBA0145D19C697113A0A489FFB3A552C34A51E443CEFE0B612001000000D037A7841DA872F436D0C0B25D12D0ECCE35C1A34973B7DACC62AE88CE355FF201000000524580D71022F131998137D3DC7C3438A40A12F253FBD37C60BDA4493B50A43B01000000ABD680AF8CDCE56B15DB169334541F1FBB6837739F1D00441B60CFE448B1FEE601000000A6D6C7A61AE3D3E3A855737C99623B6377F693CEF7EA1AE132C71BF635FF432601000000865282D0DE82E0AB47C12648A98E3D7970BFF439E0BA75BDF777F252CE30536001000000EC8093E8DFFDCEDF476EFCF40084133C5ED049A262AA418B653F39D98420ABF201000000B9213B0AB77434CB2CC8423BB5DE79ACFD8822182EACBAA5C76355D5208631DC000000";
		PeerNode peernode= new PeerNode(IPVersion.IPV4);
		peernode.setHost("127.0.0.1");
		peernode.setPort(8333);
		List<PeerNode> listnode = new ArrayList<PeerNode>();
		listnode.add(peernode);
		Pair<Retour, List<PeerNode>> dnslookup = new Pair<Retour, List<PeerNode>>(Utils.createRetourOK(), listnode);
		EasyMock.expect(DnsLookUp.getInstance()).andReturn(dnsLookup);
		EasyMock.expect(dnsLookup.getAllNodePeer()).andReturn(dnslookup);
		EasyMock.expect(DnsLookUp.getInstance()).andReturn(dnsLookup);
		EasyMock.expect(dnsLookup.getBestPeer()).andReturn(peernode);	
		byte[] content = Utils.hexStringToByteArray(trameVersionReceive);
		InputStream anyInputStream = new ByteArrayInputStream(content);
		datainput = new DataInputStream(anyInputStream);
		PowerMock.expectNew(Socket.class,peernode.getHost(),peernode.getPort()).andReturn(socket);
		socket.setSoTimeout(Utils.timeoutPeer);
		EasyMock.expect(socket.getOutputStream()).andReturn(dataouput);
		EasyMock.expect(socket.getInputStream()).andReturn(datainput);
		dataouput.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		PowerMock.replayAll();
		NetworkOrchestrator network = new NetworkOrchestrator();
		network.onStart();
		PowerMock.verify();
	}
}