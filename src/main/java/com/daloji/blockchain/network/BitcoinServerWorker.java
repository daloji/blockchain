package com.daloji.blockchain.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.utils.Utils;

public class BitcoinServerWorker extends AbstractCallable{

	private static final Logger logger =  LoggerFactory.getLogger(BitcoinServerWorker.class);

	private  ServerSocket  server = null;

	private boolean isRunning;


	public BitcoinServerWorker() {
		try {
			server = new ServerSocket(Utils.BITCOIN_PORT);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public Object call() throws Exception {
		Socket clientSocket = null;
		while(isRunning) {
			if(server == null) {
				logger.error("Erreur BitcoinServer");
				isRunning =false;
			}else {
				clientSocket = server.accept();
				ClientHandlerBitcoinNode clientHandler = new ClientHandlerBitcoinNode(clientSocket);
				
			}
		}
		return null;
	}


}
