package com.daloji.blockchain.core.utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.daloji.blockchain.core.BlockChain;
import com.daloji.blockchain.network.ConnectionNode;
import com.daloji.blockchain.network.WatchDogHandler;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.google.common.util.concurrent.CycleDetectingLockFactory;


public class BlockChainWareHouseThreadFactory {

	private static final  int SIZE_POOL = 3;

	private ExecutorService executorServiceInitialDownloadBlock ;

	private ExecutorService executorServiceBlockDownloaded;

	private static  ScheduledExecutorService executorsWatchDog;

	private static CycleDetectingLockFactory lockfactory = null;

	private static BlockChainWareHouseThreadFactory instance = null;

	private static WatchDogHandler watchdog;

	private  BlockChainEventHandler blockchainlistener;

	private static CycleDetectingLockFactory.Policy policy = CycleDetectingLockFactory.Policies.THROW;

	public static BlockChainWareHouseThreadFactory getInstance() {
		if(instance == null) {
			instance = new BlockChainWareHouseThreadFactory();
		}
		return instance;
	}


	private  BlockChainWareHouseThreadFactory() {

		executorServiceInitialDownloadBlock =  Executors.newFixedThreadPool(SIZE_POOL);;
		executorServiceBlockDownloaded =new ThreadPoolExecutor( SIZE_POOL, SIZE_POOL, 2, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
		executorsWatchDog = Executors.newScheduledThreadPool(1,
				new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setDaemon(true);
				return t;
			}
		});
		
	}



	public static ReentrantLock lock(String name) {
		if(lockfactory == null) {
			lockfactory = CycleDetectingLockFactory.newInstance(policy); 
		}

		return lockfactory.newReentrantLock(name);
	}


	public static <T> ReentrantLock lockThisObject(Class<T> clazz) {
		return lock(clazz.getSimpleName() + " lock");
	}


	public <T> void invokeAllIntialDownloadBlock(List<ConnectionNode> listThreadInPool) throws InterruptedException {
		executorServiceInitialDownloadBlock.invokeAll(listThreadInPool);
	}

	public <T> void invokeBlock(Callable<T> task) throws InterruptedException {
		executorServiceBlockDownloaded.submit(task);
	}

	public  void onBlockChainChange(BlockChain blockchain) {
		watchdog.onBlockChainChange(blockchain);
	}

	public void addBlockChainListener(BlockChainEventHandler eventlistener) {
		this.blockchainlistener = eventlistener;
		watchdog = new WatchDogHandler(blockchainlistener);
		executorsWatchDog.scheduleWithFixedDelay( watchdog,  0 , 2 , TimeUnit.MINUTES); 
		
	}
}
