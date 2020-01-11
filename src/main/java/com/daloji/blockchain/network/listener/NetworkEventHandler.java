package com.daloji.blockchain.network.listener;

import com.daloji.blockchain.network.ConnectionNode;

public interface NetworkEventHandler {

	/**
	 * callback lors de la connection a un noeud 
	 * 
	 * @param connectionNode
	 * Connection node
	 */
	public void onNodeConnected(ConnectionNode connectionNode);

	/**
	 * Callback lorsque le noeud distant a des comportements qui ne correspond
	 *  au protocole du reseau
	 * @param connectionNode
	 * Connection node
	 */
	public void onNodeConnectHasError(ConnectionNode connectionNode);
	
	

	public void onNodeDisconnected();

	/**
	 * lancement de la configuration du reseau 
	 */
	public void onStart() throws Exception;

	public void onStop();


}
