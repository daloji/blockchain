package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;

import javax.naming.NamingException;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.DeserializerTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;
import com.daloji.blockchain.network.trame.TrameHeader;

public class ConnectionNode  extends AbstractCallable{


	public ConnectionNode(NetworkEventHandler networkListener,BlockChainEventHandler blockchaiListener,NetParameters netparam,PeerNode peerNode) throws NamingException {
		super();
		this.peerNode = peerNode;
		this.networkListener = networkListener;
		this.blockChainListener = blockchaiListener;
		this.netParameters = netparam;
	}
	/*
	@Override
	public Object call() throws Exception {
		byte[] data = new byte[Utils.BUFFER_SIZE];
		socketClient = new Socket(peerNode.getHost(),peerNode.getPort());
		socketClient.setSoTimeout(Utils.timeoutPeer);
		outPut = new DataOutputStream(socketClient.getOutputStream());
		input = new DataInputStream(socketClient.getInputStream()); 
		int count = 1;
		while(state !=STATE_ENGINE.START) {
			switch(state) {
			case BOOT : state = sendVersion(outPut,netParameters,peerNode);
								break;
			case VERSION_SEND:state = sendVerAck(outPut,netParameters,peerNode);
								break;
			case VER_ACK_RECEIVE:networkListener.onNodeConnected(this);
								state = sendGetBlock(outPut, netParameters, peerNode);
								break;
			case INV_RECEIVE: count = input.read(data); 
							  if(count>0) {
								Stack<ObjectTrame> stack = receiveInventory(data);
								pileCommand.addAll(stack);
								}
							  break;

			}
			count = input.read(data); 
			if(count>0) {
				Stack<ObjectTrame> stack = processMessage(data);
				pileCommand.addAll(stack);
			}
		}
		return null;
	}*/

	@Override
	public Object call() throws Exception {

		byte[] data = new byte[Utils.BUFFER_SIZE];
		socketClient = new Socket(peerNode.getHost(),peerNode.getPort());
		socketClient.setSoTimeout(Utils.timeoutPeer);
		outPut = new DataOutputStream(socketClient.getOutputStream());
		input = new DataInputStream(socketClient.getInputStream()); 
		int count = 1;
		while(state !=STATE_ENGINE.START) {
			switch(state) {
				case BOOT : state = sendVersion(outPut,netParameters,peerNode);
				break;
				case VER_ACK_RECEIVE:state = sendVerAck(outPut,netParameters,peerNode);
							state = sendGetBlock(outPut, netParameters, peerNode);
				break;
				case VERSION_SEND: state = sendVerAck(outPut,netParameters,peerNode);
				break;
			}
			count = input.read(data);
			if(count > 0) {
				ArrayDeque<TrameHeader> deserialize = DeserializerTrame.getInstance().deserialise(data,peerNode);
				state = findNExtStep(deserialize);
			}

		}	
		return null;

	}
	
	
	
	

}
