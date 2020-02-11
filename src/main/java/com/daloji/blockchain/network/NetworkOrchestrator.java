package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Addr;
import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.BlockChain;
import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.core.commons.database.proxy.LevelDbProxy;
import com.daloji.blockchain.core.utils.BlockChainWareHouseThreadFactory;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;



public class  NetworkOrchestrator implements NetworkEventHandler,BlockChainEventHandler {

	private static final Logger logger = LoggerFactory.getLogger(NetworkOrchestrator.class);

	protected final ReentrantLock lock = BlockChainWareHouseThreadFactory.lockThisObject(NetworkOrchestrator.class);

	private static final int NB_THREAD = 1;

	private  CopyOnWriteArrayList<ConnectionNode> listPeerConnected = new CopyOnWriteArrayList<ConnectionNode>(); 

	private BlockChain blokchain = new BlockChain();

	/*
	 * List des Threads clients
	 */
	private List<ConnectionNode> listThreadNodeRunning;

	private List<ConnectionNode> listThreadConnected;

	private List<BlockChainHandler> listThreadBlochChain;



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
			logger.info("il n'y a plus de Thread Connexion . Reconnexion en attente");
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
			for (int i = 0; i < NB_THREAD; i++) {
				PeerNode peer = DnsLookUp.getInstance().getBestPeer();
				if(peer!=null) {
					connectionNode = new ConnectionNode(this,this, NetParameters.MainNet, peer);
					listThreadConnected.add(connectionNode);	
				}
			}
			BlockChainWareHouseThreadFactory.getInstance().addBlockChainListener(this);
			BlockChainWareHouseThreadFactory.getInstance().invokeAllIntialDownloadBlock(listThreadConnected);
			BitcoinServerWorker bitcoinServer = new BitcoinServerWorker(this,NetParameters.MainNet);
			//BlockChainWareHouseThreadFactory.getInstance().invokeClient(bitcoinServer);

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
		List<ConnectionNode> listPeerConnected = new ArrayList<ConnectionNode>(listThreadConnected); 
		if(listPeerConnected !=null) {
			for(ConnectionNode connection: listPeerConnected) {
				connection.onRestartIDB(listUsingPeer);
			}
			if(listPeerConnected.isEmpty()) {
				try {
					this.onRestart();	
				}catch (Exception e) {
					logger.error(e.getMessage());
				}
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

	@Override
	public void onRestart() throws Exception {
		logger.info("relance procedure Initial Bloc Download");
		ConnectionNode connectionNode = null;
		for (int i = 0; i < NB_THREAD; i++) {
			PeerNode peer = DnsLookUp.getInstance().getBestPeer();
			if(peer!=null) {
				connectionNode = new ConnectionNode(this,this, NetParameters.MainNet, peer);
				listThreadConnected.add(connectionNode);	
			}
		}
		BlockChainWareHouseThreadFactory.getInstance().invokeAllIntialDownloadBlock(listThreadConnected);
		
	}

	@Override
	public void onAddresseReceive(List<Addr> listAddr) {
		DnsLookUp.getInstance().receiveListAddr(listAddr);
	}


}
