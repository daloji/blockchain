package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;


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
		PowerMock.mockStaticStrict(DataOutputStream.class);
	}



	@Test
	public void startVersion() throws Exception {
		PeerNode peer =new PeerNode(IPVersion.IPV4);
		peer.setHost("127.0.0.1");
		peer.setPort(8333);
		PowerMock.expectNew(Socket.class,peer.getHost(),peer.getPort()).andReturn(socket);
		socket.setSoTimeout(Utils.timeoutPeer);
	    EasyMock.expect(socket.getOutputStream()).andReturn(dataouput);
	    EasyMock.expect(socket.getInputStream()).andReturn(datainput);

		PowerMock.replayAll();
		ConnectionNode connection = new ConnectionNode(null, null, NetParameters.MainNet, peer);
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
