package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.InvType;
import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.InitialDownloadBlock;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.DeserializerTrame;
import com.daloji.blockchain.network.trame.InvTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;
import com.daloji.blockchain.network.trame.TrameHeader;

import ch.qos.logback.classic.Logger;

public class ConnectionNode  extends AbstractCallable implements InitialDownloadBlock{


	private static final Logger logger = (Logger) LoggerFactory.getLogger(ConnectionNode.class);

	private  TrameHeader lastTrame;

	private volatile boolean isInterrupt = false;

	public ConnectionNode(NetworkEventHandler networkListener,BlockChainEventHandler blockchaiListener,NetParameters netparam,PeerNode peerNode) throws NamingException {
		super();
		this.peerNode = peerNode;
		this.networkListener = networkListener;
		this.blockChainListener = blockchaiListener;
		this.netParameters = netparam;
	}


	
	@Override
	public Object call() throws Exception {
		try{
			byte[] data = new byte[Utils.BUFFER_SIZE];
			socketClient = new Socket(peerNode.getHost(),peerNode.getPort());
			socketClient.setSoTimeout(Utils.timeoutPeer);
			outPut = new DataOutputStream(socketClient.getOutputStream());
			input = new DataInputStream(socketClient.getInputStream()); 
			int count = 1;
			listState.add(STATE_ENGINE.BOOT);
			while(state !=STATE_ENGINE.START && count!=-1 && !isInterrupt) {
				switch(state) {
				case BOOT : state = sendVersion(outPut,netParameters,peerNode);
				listState.add(state);
				break;
				case VER_ACK_RECEIVE:state = sendVerAck(outPut,netParameters,peerNode);
				networkListener.onNodeConnected(this);
				listState.add(state);

				break;
				case VERSION_SEND: //state = sendVerAck(outPut,netParameters,peerNode);
					break;
				case READY :// networkListener.onNodeConnected(this);
					break;
				case GETBLOCK_SEND :state = sendGetBlock(outPut, netParameters, peerNode);
				listState.add(state);	
				break;
				}
				count = input.read(data);
				if(count > 0) {
					logger.info(Utils.bytesToHex(data));
					ArrayDeque<TrameHeader> deserialize = DeserializerTrame.getInstance().deserialise(lastTrame,data,peerNode);
					if(deserialize.size()>0) {
						TrameHeader trame = deserialize.getLast();
						lastTrame = trame;
						callGetBlock(deserialize);
						state = findNExtStep(deserialize);
						replyAllRequest(deserialize,outPut, netParameters, peerNode);
					}	
				}
			}
			this.peerNode.setUse(false);
			networkListener.onNodeDisconnected(this);
				logger.info("fin du Thread ConnectoionNode");
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return null;

		
	}
	private void callGetBlock(ArrayDeque<TrameHeader> trameArray) {
		List<Inventory>  listBlock = new ArrayList<Inventory>();
		if(trameArray !=null) {

			for(TrameHeader trame:trameArray) {
				if(trame instanceof InvTrame) {
					List<Inventory> listinventory =((InvTrame) trame).getListinv();
					for(Inventory inventory :listinventory) {
						if(inventory.getType() ==InvType.MSG_BLOCK) {
							listBlock.add(inventory);
							
						}
					}
				}
			}
			if(!listBlock.isEmpty()) {
				blockChainListener.onBlockHeaderReceive(listBlock);
			}
		}
	}



	@Override
	public void onRestartIDB(List<PeerNode> peer) {
		for(PeerNode node :peer) {
			if(this.peerNode.getHost().equals(node.getHost())) {
				isInterrupt = true;
			}
		}
	}

}
