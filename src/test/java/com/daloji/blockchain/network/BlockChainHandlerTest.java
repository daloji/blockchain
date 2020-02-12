package com.daloji.blockchain.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.daloji.blockchain.core.InvType;
import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.InvTrameTest;
import com.daloji.blockchain.network.trame.STATE_ENGINE;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.crypto.*","javax.security.auth.*"})
@PrepareForTest({BlockChainHandler.class,Socket.class,DataInputStream.class,DataOutputStream.class})
public class BlockChainHandlerTest  {

	
	private BlockChainEventHandler blockchaineEvent;

	@MockStrict
	private Socket socket;
	

	@MockStrict
	private DataOutputStream dataouput;

	@MockStrict
	private DataInputStream datainput;
	
	private static String trame_receive;
	
	private static String trame_receive_bloc001;

	@Before
	public void beforeTest() {
		PowerMock.resetAll();
		PowerMock.mockStaticStrict(Socket.class);
		PowerMock.mockStaticStrict(DataOutputStream.class);
		PowerMock.mockStaticStrict(DataInputStream.class);
		PowerMock.mockStaticStrict(NetworkEventHandler.class);
	}
	
	@BeforeClass
	public static void initTest() throws FileNotFoundException, IOException {

		ClassLoader classLoader = InvTrameTest.class.getClassLoader();
		File file = new File(classLoader.getResource("test.properties").getFile());
		Properties prop = new Properties();
	    // load a properties file
	    prop.load(new FileInputStream(file));
	    trame_receive = (prop.getProperty("block_receive_001"));
	    trame_receive_bloc001 = (prop.getProperty("trame_receive_bloc001"));
	}

	/**
	 *  Reception Inv Trame sans entête 
	 *  comportement bizarre mais doit être gerer
	 * 
	 * 
	 */

	@Test
	public void getBlock_001() throws Exception {

		String hash_header ="4860EB18BF1B1620E37E9490FC8A427514416FD75159AB86688E9A8300000000";
		PeerNode peer =new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		peer.setPort(8333);
		Inventory inventory = new Inventory();
		inventory.setHash(hash_header);
		inventory.setType(InvType.MSG_BLOCK);
		List<Inventory> listinv = new ArrayList<Inventory>();
		listinv.add(inventory);
		byte[] content = Utils.hexStringToByteArray(trame_receive);
		InputStream anyInputStream = new ByteArrayInputStream(content);
		datainput = new DataInputStream(anyInputStream);
		PowerMock.expectNew(Socket.class,peer.getHost(),peer.getPort()).andReturn(socket);
		socket.setSoTimeout(Utils.timeoutPeer);
		EasyMock.expect(socket.getOutputStream()).andReturn(dataouput);
		EasyMock.expect(socket.getInputStream()).andReturn(datainput);
		dataouput.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		BlockChainHandler blockhandler= new BlockChainHandler(null, null, NetParameters.MainNet, peer, listinv);
		PowerMock.replayAll();
		Whitebox.setInternalState(blockhandler, "state", STATE_ENGINE.READY);
		blockhandler.call();
		PowerMock.verify();

	}
	@Test
	public void getBlock_002() throws Exception {
		String hash_header ="4860EB18BF1B1620E37E9490FC8A427514416FD75159AB86688E9A8300000000";
		PeerNode peer =new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		peer.setPort(8333);
		Inventory inventory = new Inventory();
		inventory.setHash(hash_header);
		inventory.setType(InvType.MSG_BLOCK);
		List<Inventory> listinv = new ArrayList<Inventory>();
		listinv.add(inventory);
		byte[] content = Utils.hexStringToByteArray(trame_receive_bloc001);
		InputStream anyInputStream = new ByteArrayInputStream(content);
		datainput = new DataInputStream(anyInputStream);
		PowerMock.expectNew(Socket.class,peer.getHost(),peer.getPort()).andReturn(socket);
		socket.setSoTimeout(Utils.timeoutPeer);
		EasyMock.expect(socket.getOutputStream()).andReturn(dataouput);
		EasyMock.expect(socket.getInputStream()).andReturn(datainput);
		dataouput.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		PowerMock.replayAll();
		BlockChainHandler blockhandler= new BlockChainHandler(null, null, NetParameters.MainNet, peer, listinv);
		Whitebox.setInternalState(blockhandler, "state", STATE_ENGINE.READY);
		blockhandler.call();
		PowerMock.verify();
	}
	
	@Test
	public void getBlock_003() throws Exception {
		String hash_header ="4860EB18BF1B1620E37E9490FC8A427514416FD75159AB86688E9A8300000000";
		PeerNode peer =new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		peer.setPort(8333);
		Inventory inventory = new Inventory();
		inventory.setHash(hash_header);
		inventory.setType(InvType.MSG_BLOCK);
		List<Inventory> listinv = new ArrayList<Inventory>();
		listinv.add(inventory);
		byte[] content = Utils.hexStringToByteArray(trame_receive_bloc001);
		InputStream anyInputStream = new ByteArrayInputStream(content);
		datainput = new DataInputStream(anyInputStream);
		PowerMock.expectNew(Socket.class,peer.getHost(),peer.getPort()).andReturn(socket);
		socket.setSoTimeout(Utils.timeoutPeer);
		EasyMock.expect(socket.getOutputStream()).andReturn(dataouput);
		EasyMock.expect(socket.getInputStream()).andReturn(datainput);
		dataouput.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		PowerMock.replayAll();
		BlockChainHandler blockhandler= new BlockChainHandler(null, null, NetParameters.MainNet, peer, listinv);
		Whitebox.setInternalState(blockhandler, "state", STATE_ENGINE.READY);
		blockhandler.call();
		PowerMock.verify();
	}
}
