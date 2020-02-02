package com.daloji.blockchain.network.listener;

import com.daloji.blockchain.core.BlockChain;

public interface WatchDogListener {

	public void onBlockChainChange(BlockChain blockchain);
}
