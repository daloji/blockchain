package com.daloji.blockchain.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.nio.charset.Charset;

import org.apache.commons.io.input.ReaderInputStream;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;

import javassist.bytecode.ByteArray;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.crypto.*","javax.security.auth.*"})
@PrepareForTest({ConnectionNode.class,Socket.class,DataInputStream.class,DataOutputStream.class})
public class ConnectionNodeTest  {

	private NetworkEventHandler networkEvent;

	private BlockChainEventHandler blockchaineEvent;

	@MockStrict
	private Socket socket;

	@MockStrict
	private DataOutputStream dataouput;

	@MockStrict
	private DataInputStream datainput;

	@Before
	public void beforeTest() {
		PowerMock.resetAll();
		PowerMock.mockStaticStrict(Socket.class);
		PowerMock.mockStaticStrict(DataOutputStream.class);
		PowerMock.mockStaticStrict(DataInputStream.class);
	}

	/**
	 *  Reception Version partielle  et Verack
	 * 
	 * 
	 */

	//@Test
	public void startConnection() throws Exception {

		String trameVersionReceive = "F9BEB4D976657273696F6E0000000000680000002C89C73B000000000000000000000000000000000000000000";
		String trameVersionpartie2 = "7F1101000904000000000000C4C7225E00000000000000000000000000000000000000000000FFFF55AA7073C9AE0904000000000000000000000000000000000000000000000000C56A088FD1A7AAC7122F5361746F7368693A302E31392E302E312F095C090001F9BEB4D976657261636B000000000000000000005DF6E0E2";

		PeerNode peer =new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		peer.setPort(8333);

		byte[] content = Utils.hexStringToByteArray(trameVersionReceive);
		InputStream anyInputStream = new ByteArrayInputStream(content);
		datainput = new DataInputStream(anyInputStream);
		
		PowerMock.expectNew(Socket.class,peer.getHost(),peer.getPort()).andReturn(socket);
		socket.setSoTimeout(Utils.timeoutPeer);
		EasyMock.expect(socket.getOutputStream()).andReturn(dataouput);
		EasyMock.expect(socket.getInputStream()).andReturn(datainput);
		dataouput.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		content = Utils.hexStringToByteArray(trameVersionpartie2);
		anyInputStream = new ByteArrayInputStream(content);
		DataInputStream datainput2 = new DataInputStream(anyInputStream);
		EasyMock.expect(datainput.read(new byte[1])).andReturn(12);
		//EasyMock.expect(datainput).andReturn(datainput2);
		PowerMock.replayAll();
		//datainput = new DataInputStream(anyInputStream);

		ConnectionNode connection = new ConnectionNode(null, null, NetParameters.MainNet, peer);
//		Whitebox.setInternalState(connection, "input", datainput);
		connection.call();
		PowerMock.verify();

	}


	/*
 PowerMock.expectNew(PROV_SI002_ExecuterProcessusBuilder.class).andReturn(_si002BuilderMock);
    EasyMock.expect(_si002BuilderMock.tracabilite(EasyMock.anyObject(Tracabilite.class))).andReturn(_si002BuilderMock);
    EasyMock.expect(_si002BuilderMock.cles(EasyMock.anyObject())).andReturn(_si002BuilderMock);
    EasyMock.expect(_si002BuilderMock.priorite(10)).andReturn(_si002BuilderMock);
    EasyMock.expect(_si002BuilderMock.processus(processus_p)).andReturn(_si002BuilderMock);
    EasyMock.expect(_si002BuilderMock.listeParametres(listParams_p)).andReturn(_si002BuilderMock);
    EasyMock.expect(_si002BuilderMock.build()).andReturn(_si002Mock);
    EasyMock.expect(_si002Mock.execute(_processInstance)).andReturn(reponseConnector_p);
    EasyMock.expect(_si002Mock.getRetour()).andReturn(retour_p).anyTimes();
	 */
}
