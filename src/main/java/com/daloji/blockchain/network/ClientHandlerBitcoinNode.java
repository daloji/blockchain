package com.daloji.blockchain.network;

import java.net.Socket;

public class ClientHandlerBitcoinNode extends AbstractCallable{

	private Socket socket;
	
	public ClientHandlerBitcoinNode(Socket socket) {
		this.socket = socket;
	}
	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
