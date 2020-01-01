package com.daloji.core.blockchain.net;

public interface CallbackConnector {
	
	
	public void onSocketConnected();
	
	public void onSocketReceiveByte();
	
	public void onSocketDisconnected();

}
