package com.daloji.blockchain.network.listener;

import com.daloji.blockchain.core.InvType;
import com.daloji.blockchain.core.Inventory;

public interface BlockChainEventHandler {
	
	/**
	 * Callback lorsqu'un header de blockChain est recu
	 *  
	 * @param InvType
	 *    
	 * 0	ERROR	Any data of with this number may be ignored
	 * 1	MSG_TX	Hash is related to a transaction
	 * 2	MSG_BLOCK	Hash is related to a data block
	 * 3	MSG_FILTERED_BLOCK	Hash of a block header; identical to MSG_BLOCK. Only to be used in getdata message. Indicates the reply should be a merkleblock message rather than a block message; this only works if a bloom filter has been set.
	 * 4	MSG_CMPCT_BLOCK	Ha
	 *  
	 *  @param hashHeader
	 *   hash du header de block 
	 *  
	 */
	
	public void onBlockHeaderReceive(Inventory inventory);
}
