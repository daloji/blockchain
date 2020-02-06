package com.daloji.blockchain.network.listener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.Inventory;

public interface BlockChainEventHandler {
	
	/**
	 * Callback lorsqu'un header de blockChain est recu
	 *  
	 *  
	 */
	
	public void onBlockHeaderReceive(DataOutputStream dataOut,DataInputStream dataInput,Inventory inventory);
	
	public void onBlockHeaderReceive(List<Inventory> listInventory);
	
	public void onBlockReiceve(Block block);
	
	public void onWatchDogSendRestart();
}
