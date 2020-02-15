package com.daloji.blockchain.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.utils.BlockChainWareHouseThreadFactory;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.listener.NetworkEventHandler;


/**
 * Bitcoin Server
 * @author daloji
 *
 */
public class BitcoinServerWorker extends AbstractCallable {

	private static final Logger logger =  LoggerFactory.getLogger(BitcoinServerWorker.class);

	private  ServerSocket  server = null;


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
		logger.info("Start BitcoinServer");
		Socket clientSocket = null;
		while(isRunning) {
			if(server == null) {
				logger.error("Erreur BitcoinServer");
				isRunning =false;
			}else {
				clientSocket = server.accept();
				ClientHandlerBitcoinNode clientHandler = new ClientHandlerBitcoinNode(clientSocket);
				BlockChainWareHouseThreadFactory.getInstance().invokeServer(clientHandler);

				
			}
		}
		return null;
	}
	

}
