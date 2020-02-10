package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.trame.DeserializerTrame;
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
			byte[] data = new byte[Utils.BUFFER_SIZE];
			outPut = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream()); 
			int count = 1;
			while(count!=-1 && !isInterrupt) {
				count = input.read(data);
				if(count > 0) {
					String extractZero =Utils.bytesToHex(data);
					extractZero = Utils.deleteEndZero(extractZero);
					logger.info(extractZero);
					ArrayDeque<TrameHeader> deserialize = DeserializerTrame.getInstance().deserialise(lastTrame,data,peerNode);
					if(deserialize.size()>0) {
						TrameHeader trame = deserialize.getLast();
						lastTrame = trame;
						state = findNExtStep(deserialize);
						replyAllRequest(deserialize,outPut, netParameters, peerNode);
					}	
				}
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
