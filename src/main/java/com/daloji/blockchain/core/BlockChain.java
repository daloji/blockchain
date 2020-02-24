package com.daloji.blockchain.core;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class BlockChain implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ConcurrentHashMap<String, Block> blockChain;
	

	
	public BlockChain() {
		blockChain = new ConcurrentHashMap<>();

	}

	public ConcurrentHashMap<String, Block> getBlockChain() {
		return blockChain;
	}

	public void setBlock(String hash, Block block) {
		blockChain.put(hash, block);
	}

}
