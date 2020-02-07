package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.BlockChain;
import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.core.commons.proxy.LevelDbProxy;
import com.daloji.blockchain.core.utils.BlockChainWareHouseThreadFactory;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class  NetworkOrchestrator implements NetworkEventHandler,BlockChainEventHandler {



	private static final Logger logger = (Logger) LoggerFactory.getLogger(NetworkOrchestrator.class);


	private static final int NB_THREAD = 1;

	private  CopyOnWriteArrayList<ConnectionNode> listPeerConnected = new CopyOnWriteArrayList<ConnectionNode>(); 

	private  CopyOnWriteArrayList<Inventory> listHeaderBlock = new CopyOnWriteArrayList<Inventory>(); 

	private BlockChain blokchain = new BlockChain();

	/*
	 * List des Threads clients
	 */
	private List<ConnectionNode> listThreadNodeRunning;

	private List<ConnectionNode> listThreadConnected;

	private List<BlockChainHandler> listThreadBlochChain;

	private List<PeerNode> listPeer;


	/*
	 * (non-Javadoc)
	 * @see com.daloji.blockchain.network.listener.NetworkEventHandler#onNodeDisconnected(com.daloji.blockchain.network.AbstractCallable)
	 */
	@Override
	public void onNodeDisconnected(AbstractCallable connectionNode) {
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
		DnsLookUp.getInstance().restorePeer(connectionNode.getPeerNode());
		listThreadConnected.remove(connectionNode);
		if(listThreadConnected.isEmpty()) {
			logger.info("il n'y a plus de Thread Connexion ");
		}
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
		listThreadNodeRunning = new ArrayList<ConnectionNode>();
		listThreadConnected = new ArrayList<ConnectionNode>();
		Pair<Retour, List<PeerNode>> dnslookup = DnsLookUp.getInstance().getAllNodePeer();
		Retour retour = dnslookup.first;
		if(Utils.isRetourOK(retour)) {
			listPeer = dnslookup.second;
			for (int i = 0; i < NB_THREAD; i++) {
				PeerNode peer = DnsLookUp.getInstance().getBestPeer();
				if(peer!=null) {
					connectionNode = new ConnectionNode(this,this, NetParameters.MainNet, peer);
					listThreadConnected.add(connectionNode);	
				}
			}
			BlockChainWareHouseThreadFactory.getInstance().addBlockChainListener(this);
			BlockChainWareHouseThreadFactory.getInstance().invokeAllIntialDownloadBlock(listThreadConnected);
		}
		
		logger.info("fin du NetworkOrchestrator");
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
	public void onNodeConnected(AbstractCallable connectionNode) {
		logger.info("onNodeConnected connecté au noeud :"+ connectionNode.getPeerNode().getHost() +"  port "+connectionNode.getPeerNode().getPort());
		listPeerConnected.add((ConnectionNode) connectionNode);
	}

	@Override
	public void onNodeConnectHasError(AbstractCallable connectionNode) {


	}

	@Override
	public void onBlockHeaderReceive(DataOutputStream dataOut, DataInputStream dataInput, Inventory inventory) {
		// TODO Auto-generated method stub

	}



	@Override
	public void onBlockReiceve(Block block) {

		LevelDbProxy.getInstance().addBlock(block);
		blokchain.setBlock(block.getPrevBlockHash(), block);
		BlockChainWareHouseThreadFactory.getInstance().onBlockChainChange(blokchain);
	}

	@Override
	public void onWatchDogSendRestart() {
		List<PeerNode> listUsingPeer = DnsLookUp.getInstance().getListUsePeer();	
		if(listThreadConnected !=null) {
			for(ConnectionNode connection: listThreadConnected) {
				connection.onRestartIDB(listUsingPeer);
			}	
			
		}
		
	}

	@Override
	public void onBlockHeaderReceive(List<Inventory> listInventory) {
		for(Inventory inventory:listInventory) {
			PeerNode peer = DnsLookUp.getInstance().getBestPeer();
			if(peer !=null) {
				BlockChainHandler blockChain = new BlockChainHandler(this,this, NetParameters.MainNet, peer,inventory);
				try {
					BlockChainWareHouseThreadFactory.getInstance().invokeBlock(blockChain);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			}
			
		}
		BlockChainWareHouseThreadFactory.getInstance().shutDownBloc();
	}


}
