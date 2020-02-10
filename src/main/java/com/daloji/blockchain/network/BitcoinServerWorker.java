package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.utils.BlockChainWareHouseThreadFactory;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.DeserializerTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;
import com.daloji.blockchain.network.trame.TrameHeader;

public class BitcoinServerWorker extends AbstractCallable {

	private static final Logger logger =  LoggerFactory.getLogger(BitcoinServerWorker.class);

	private  ServerSocket  server = null;

	private  TrameHeader lastTrame;

	private boolean isRunning;


	public BitcoinServerWorker(NetworkEventHandler networkListener,NetParameters netparam) {
		super();
		try {
			server = new ServerSocket(Utils.BITCOIN_PORT);
			isRunning = true;
			netParameters = netparam;
			this.networkListener = networkListener;
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public Object call() throws Exception {
		Socket clientSocket = null;
		sendAddrBroadCast();
		
		while(isRunning) {
			if(server == null) {
				logger.error("Erreur BitcoinServer");
				isRunning =false;
			}else {
				clientSocket = server.accept();
				ClientHandlerBitcoinNode clientHandler = new ClientHandlerBitcoinNode(clientSocket);
				BlockChainWareHouseThreadFactory.getInstance().invokeClient(clientHandler);

				
			}
		}
		return null;
	}
	
	public void sendAddrBroadCast() throws IOException {
		int count =1;
		byte[] data = new byte[Utils.BUFFER_SIZE];
		List<PeerNode> listpeer = DnsLookUp.getInstance().getListPeerFree();
		for(PeerNode peer:listpeer) {
			this.peerNode = peer;
			socketClient = new Socket(peer.getHost(),peer.getPort());
			socketClient.setSoTimeout(Utils.timeoutPeer);
			outPut = new DataOutputStream(socketClient.getOutputStream());
			input = new DataInputStream(socketClient.getInputStream()); 
			while(state !=STATE_ENGINE.START && count!=-1 ) {
				logger.info("Running Bitcoin Server for node  "+state);
				switch(state) {
				case BOOT : state = sendVersion(outPut,netParameters,peer);
				listState.add(state);
				break;
				case VER_ACK_RECEIVE:state = sendVerAck(outPut,netParameters,peer);
				networkListener.onNodeConnected(this);
				listState.add(state);
				break;
				case VERSION_SEND: //state = sendVerAck(outPut,netParameters,peerNode);
					break;
				case READY :// networkListener.onNodeConnected(this);
					break;
			
				}
				count = input.read(data);
				if(count > 0) {
					String extractZero =Utils.bytesToHex(data);
					extractZero = Utils.deleteEndZero(extractZero);
					logger.info(extractZero);
					ArrayDeque<TrameHeader> deserialize = DeserializerTrame.getInstance().deserialise(lastTrame,data,peer);
					if(deserialize.size()>0) {
						TrameHeader trame = deserialize.getLast();
						lastTrame = trame;
						state = findNExtStepAddrBroadcast(deserialize);
					}	
				}
			}
			
			
		}
		
	}


}
