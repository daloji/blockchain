package com.daloji.blockchain.network.listener;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.daloji.blockchain.core.Inventory;

public interface BlockChainEventHandler {
	
	/**
	 * Callback lorsqu'un header de blockChain est recu
	 *  
	 *  
	 */
	
	public void onBlockHeaderReceive(DataOutputStream dataOut,DataInputStream dataInput,Inventory inventory);
	
	public void onBlockHeaderReceive(Inventory inventory);
}
