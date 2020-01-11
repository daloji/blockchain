package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Stack;
import java.util.concurrent.Callable;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.ObjectTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;

public class BlockChainHandler implements Callable<Object>{

private NetworkEventHandler networkListener;
	
	private BlockChainEventHandler blockChainListener;
	
	private Socket socketClient;
	
	private PeerNode peerNode;

	private DataOutputStream outPut;

	private DataInputStream input;

	private NetParameters netParameters;
	
	
	private STATE_ENGINE state = STATE_ENGINE.START;
	
	private BlockChainHandler(NetworkEventHandler networkListener,BlockChainEventHandler blockchaiListener,NetParameters netparam,PeerNode peerNode) {
			this.peerNode = peerNode;
			this.networkListener = networkListener;
			this.blockChainListener = blockchaiListener;
			this.netParameters = netparam;
			
	}
	
	@Override
	public Object call() throws Exception {
		/*byte[] data = new byte[Utils.BUFFER_SIZE];
		try {
			socketClient = new Socket(peerNode.getHost(),peerNode.getPort());
			socketClient.setSoTimeout(Utils.timeoutPeer);
			outPut = new DataOutputStream(socketClient.getOutputStream());
			input = new DataInputStream(socketClient.getInputStream()); 
			int count = 1;
			
			while(state !=STATE_ENGINE.STOP) {
				switch(state) {
				case START : state = sendVersion(outPut,netParameters,peerNode);
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
			}
		}catch (Exception e) {
			// TODO: handle exception
		}*/
		return null;
	}
	

	
}
