package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class  NetworkOrchestrator  implements NetworkHandler {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(NetworkOrchestrator.class);

	private ExecutorService executorService;

	private static final int sizePool = 1;

	private  CopyOnWriteArrayList<ConnectionNode> listPeerConnected = new CopyOnWriteArrayList<ConnectionNode>(); 

	/*
	 * List des Threads clients
	 */
	private List<ConnectionNode> listThreadNodeRunning;

	private List<ConnectionNode> listThreadInPool;

	private List<PeerNode> listPeer;



	@Override
	public void onNodeDisconnected() {
		// TODO Auto-generated method stub

	}

	/*
	 * 
	 * (non-Javadoc)
	 * @see com.daloji.core.blockchain.net.NetworkHandler#onStart()
	 */
	@Override
	public void onStart() throws Exception {
		logger.info("onStart NetworkOrchestrator");
		ConnectionNode connectionNode = null;
		executorService = Executors.newFixedThreadPool(sizePool);
		listThreadNodeRunning = new ArrayList<ConnectionNode>();
		listThreadInPool = new ArrayList<ConnectionNode>();
		Pair<Retour, List<PeerNode>> dnslookup = DnsLookUp.getInstance().getAllNodePeer();
		Retour retour = dnslookup._first;
		if(Utils.isRetourOK(retour)) {
			listPeer = dnslookup._second;
			for (int i = 0; i < sizePool; i++) {
				PeerNode peer = DnsLookUp.getInstance().getBestPeer(listPeer);
				connectionNode = new ConnectionNode(this, NetParameters.MainNet, peer);
				listThreadInPool.add(connectionNode);
			}

			executorService.invokeAll(listThreadInPool);
			executorService.shutdown();

			List<Future<ConnectionNode>> resultList = null;
		}
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}


	/*
	 * (non-Javadoc)
	 * @see com.daloji.core.blockchain.net.NetworkHandler#onNodeConnected(com.daloji.core.blockchain.net.ConnectionNode)
	 */
	@Override
	public void onNodeConnected(ConnectionNode connectionNode) {
		logger.info("onNodeConnected connecté au noeud :"+ connectionNode.getPeerNode().getHost() +"  port "+connectionNode.getPeerNode().getPort());
		listPeerConnected.add(connectionNode);
	}

	@Override
	public void onNodeConnectHasError(ConnectionNode connectionNode) {
		logger.info("Erreur lors de la lecture de la trame venant de :"+connectionNode.getPeerNode().getHost());
		logger.info("fermeture de la connexion");
		try {
			DataInputStream dataInput = connectionNode.getInput();
			if(dataInput !=null) {
				dataInput.close();
			}
			DataOutputStream dataOutput = connectionNode.getOutPut();
			if(dataOutput!=null) {
				dataOutput.close();
			}
			Socket socket = connectionNode.getSocketClient();
			if(socket!=null) {

				socket.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());

		}

	}

}
