package com.daloji.blockchain.network.listener;

import com.daloji.blockchain.network.AbstractCallable;

public interface NetworkEventHandler {

	/**
	 * callback lors de la connection a un noeud 
	 * 
	 * @param connectionNode
	 * Connection node
	 */
	public void onNodeConnected(AbstractCallable connectionNode);

	/**
	 * Callback lorsque le noeud distant a des comportements qui ne correspond
	 *  au protocole du reseau
	 * @param connectionNode
	 * Connection node
	 */
	public void onNodeConnectHasError(AbstractCallable connectionNode);
	
	/**
	 *  Callback de deconnexion d'un node
	 * @param connectionNode
	 */

	public void onNodeDisconnected(AbstractCallable connectionNode);

	/**
	 * lancement de la configuration du reseau 
	 */
	public void onStart() throws Exception;

	public void onStop();


}
