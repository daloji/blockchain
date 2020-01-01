package com.daloji.core.blockchain.net;

import java.net.Socket;

public interface NetworkHandler {

	/**
	 *  callback lors de la connection a un noeud 
	 *  
	 * @param peernode
	 *      noeud bitcoin 
	 * @param socket
	 *      socket 
	 */
	//public void onNodeConnected(PeerNode peernode,Socket socket)
	
	public void onNodeConnected(ConnectionNode connectionNode);

	public void onNodeDisconnected();

	/**
	 * lancement de la configuration du reseau 
	 */
	public void onStart() throws Exception;

	public void onStop();


}
