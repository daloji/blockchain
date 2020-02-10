package com.daloji.blockchain.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.BlockChain;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.WatchDogListener;


public class WatchDogHandler implements Runnable,WatchDogListener {

	private static final Logger logger =  LoggerFactory.getLogger(WatchDogHandler.class);

	private BlockChain blockchain = null;
	
	private BlockChainEventHandler blockchainlistener;
	
	private long nbBlock = 0;
	
	private long oldnbBlock = 0;
	
	private long nbcycleRestart =4;
	
	private long nbCounterWatchdog =0;
	
	
	public WatchDogHandler(BlockChainEventHandler blockchainlistener) {
		this.blockchainlistener = blockchainlistener;
	}
	
	@Override
	public void run() {
		logger.info("WatchDog  : Verification");
		if(blockchain !=null) {
			nbBlock = blockchain.getBlockChain().mappingCount();
			if(nbBlock==oldnbBlock) {
				logger.info("WatchDog : Restart Initial Block Download");
				blockchainlistener.onWatchDogSendRestart();
				nbBlock = 0;
			}else {
				logger.info("WatchDog :  nombre de block telecharge depuis le demarrage" + nbBlock );
				oldnbBlock = nbBlock;
			}
		}else {
			
			if(nbCounterWatchdog<nbcycleRestart) {
				nbCounterWatchdog = nbCounterWatchdog +1;
			}else {
				nbCounterWatchdog = 0;
				blockchainlistener.onWatchDogSendRestart();
			}
		}
		
	}
	@Override
	public void onBlockChainChange(BlockChain blockchain) {
		this.blockchain = blockchain;
	}
}
