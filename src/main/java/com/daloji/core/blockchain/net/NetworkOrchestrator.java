package com.daloji.core.blockchain.net;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.LoggerFactory;

import com.daloji.core.blockchain.Utils;
import com.daloji.core.blockchain.commons.Pair;
import com.daloji.core.blockchain.commons.Retour;

import ch.qos.logback.classic.Logger;

public class  NetworkOrchestrator  implements NetworkHandler {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(NetworkOrchestrator.class);

	private ExecutorService executorService;

	private   List<ConnectionNode> listThreadNode;

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
		executorService = Executors.newFixedThreadPool(10);
		listThreadNode = new ArrayList<ConnectionNode>();
		Pair<Retour, List<PeerNode>> dnslookup = DnsLookUp.getInstance().getAllNodePeer();
		Retour retour = dnslookup._first;
		if(Utils.isRetourOK(retour)) {
			listPeer = dnslookup._second;
			PeerNode peer = DnsLookUp.getInstance().getBestPeer(listPeer);
			ConnectionNode connectionNode = new ConnectionNode(this, NetParameters.MainNet, peer);
			listThreadNode.add(connectionNode);
			executorService.invokeAll(listThreadNode);
			executorService.shutdown();


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

	}



}
