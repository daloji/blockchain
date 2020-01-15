package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Stack;

import javax.naming.NamingException;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.ObjectTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;

import ch.qos.logback.classic.Logger;

public class BlockChainHandler  extends AbstractCallable{

	private static final Logger logger = (Logger) LoggerFactory.getLogger(AbstractCallable.class);

	private STATE_ENGINE state = STATE_ENGINE.START;
	
	private Inventory inventory;

	public BlockChainHandler(DataOutputStream dataOut,DataInputStream dataInput){
		outPut = dataOut;
		input = dataInput;
	}
	
	public BlockChainHandler(NetworkEventHandler networkListener,BlockChainEventHandler blockchaiListener,NetParameters netparam,PeerNode peerNode,Inventory inventory){
		super();
		this.peerNode = peerNode;
		this.networkListener = networkListener;
	    this.blockChainListener = blockchaiListener;
		this.netParameters = netparam;
		this.inventory = inventory;
	}

	@Override
	public Object call() throws Exception {
		logger.info("start BlockChainHandler");
		byte[] data = new byte[Utils.BUFFER_SIZE];
		try {
			socketClient = new Socket(peerNode.getHost(),peerNode.getPort());
			socketClient.setSoTimeout(Utils.timeoutPeer);
			outPut = new DataOutputStream(socketClient.getOutputStream());
			input = new DataInputStream(socketClient.getInputStream()); 
			int count = 1;
			while(state !=STATE_ENGINE.STOP) {
				switch(state) {
				case START: state = sendVersion(outPut,netParameters,peerNode);
									break;
				case VERSION_SEND:state = sendVerAck(outPut,netParameters,peerNode);
									break;
				case VER_ACK_RECEIVE://networkListener.onNodeConnected(this);
									state = sendGetData(outPut, netParameters, peerNode,inventory);
									break;
				case INV_RECEIVE: count = input.read(data); 
								  
								  break;

				}

				count = input.read(data); 
				if(count>0) {
//					Stack<ObjectTrame> stack = processMessage(data);
	//				pileCommand.addAll(stack);
				}
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		//sendGetData(outPut, netparam, peernode, inv)
		/*
		try {
			int count = 1;
			while(state !=STATE_ENGINE.STOP) {
				switch(state) {
				case START: state = sendVersion(outPut,netParameters,peerNode);
									break;
				case VERSION_SEND:state = sendVerAck(outPut,netParameters,peerNode);
									break;
				case VER_ACK_RECEIVE://networkListener.onNodeConnected(this);
									state = sendGetData(outPut, netParameters, peerNode,inventory);
									break;
				case INV_RECEIVE: count = input.read(data); 
								  
								  break;

				}

				count = input.read(data); 
				if(count>0) {
					Stack<ObjectTrame> stack = processMessage(data);
					pileCommand.addAll(stack);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	*/
		return null;
	}



}
