package com.daloji.blockchain.core.commons.proxy;

import com.daloji.blockchain.core.Block;

public interface DatabaseExchange {

	public void addObject(Block bloc);
	
	public Block findBlock(String hash);
	
	public boolean deleteBlock(String hash);
	
}
