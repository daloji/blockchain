package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.DeserializerTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;
import com.daloji.blockchain.network.trame.TrameHeader;

public class ClientHandlerBitcoinNode extends AbstractCallable{

	private static final Logger logger =  LoggerFactory.getLogger(ClientHandlerBitcoinNode.class);

	private volatile boolean isInterrupt = false;

	private  TrameHeader lastTrame;

	private Socket socket;

	public ClientHandlerBitcoinNode(Socket socket) {
		this.socket = socket;
	}
	@Override
	public Object call() throws Exception {

		try {
			PeerNode peer;
			byte[] data = new byte[Utils.BUFFER_SIZE];
			outPut = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream()); 
			peer = new PeerNode(IPVersion.IPV4);
			peer.setHost(socket.getInetAddress().getHostAddress());
			peer.setPort(socket.getPort());
			int count = 1;
			listState.add(STATE_ENGINE.BOOT);
			while(count!=-1 && !isInterrupt) {
				count = input.read(data);
				if(count > 0) {
					String extractZero =Utils.bytesToHex(data);
					extractZero = Utils.deleteEndZero(extractZero);
					logger.info(extractZero);
					ArrayDeque<TrameHeader> deserialize = DeserializerTrame.getInstance().deserialise(lastTrame,data,peer);
					if(deserialize.size()>0) {
						TrameHeader trame = deserialize.getLast();
						lastTrame = trame;
						state = findNExtStepServer(deserialize);
						//	replyAllRequest(deserialize,outPut, netParameters, peerNode);
					}

					switch(state) {
					case VERSION_RECEIVE : state = sendVersion(outPut,NetParameters.MainNet,peer);
										   listState.add(STATE_ENGINE.VERSION_SEND);
										   sendVerAck(outPut,NetParameters.MainNet,peer);
										   listState.add(STATE_ENGINE.VER_ACK_SEND);
					break;
					case READY :replyAllRequest(deserialize,outPut, netParameters, peerNode);
					break;
								
					}
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
