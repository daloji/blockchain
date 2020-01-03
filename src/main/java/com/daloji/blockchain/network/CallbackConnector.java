package com.daloji.blockchain.network;

public interface CallbackConnector {
	
	
	public void onSocketConnected();
	
	public void onSocketReceiveByte();
	
	public void onSocketDisconnected();

}
