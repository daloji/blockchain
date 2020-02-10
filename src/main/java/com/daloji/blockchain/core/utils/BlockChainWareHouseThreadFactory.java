package com.daloji.blockchain.core.utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.BlockChain;
import com.daloji.blockchain.network.ConnectionNode;
import com.daloji.blockchain.network.WatchDogHandler;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.google.common.util.concurrent.CycleDetectingLockFactory;



public class BlockChainWareHouseThreadFactory {


	private static final Logger logger =  LoggerFactory.getLogger(BlockChainWareHouseThreadFactory.class);

	private static final  int SIZE_POOL = 100;
	
	private static final  int SIZE_CLIENT_POOL = 10;

	private ThreadPoolExecutor threadPoolExecutor;
	
	private ThreadPoolExecutor threadPoolExecutorServer;


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
		logger.info("Initialisation BlockChainWareHouseThreadFactory");
		threadPoolExecutor =  new ThreadPoolExecutor(SIZE_POOL, SIZE_POOL,2, TimeUnit.MINUTES,new LinkedBlockingQueue<Runnable>());
		threadPoolExecutorServer =  new ThreadPoolExecutor(SIZE_CLIENT_POOL, SIZE_CLIENT_POOL,2, TimeUnit.MINUTES,new LinkedBlockingQueue<Runnable>());
		threadPoolExecutor.allowCoreThreadTimeOut(true);
		threadPoolExecutorServer.allowCoreThreadTimeOut(true);
		executorsWatchDog = Executors.newScheduledThreadPool(1,	new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setDaemon(true);
				return t;
			}
		});

	}



	public static ReentrantLock lock(String name) {
		logger.info("lock du fichier" +name);
		if(lockfactory == null) {
			lockfactory = CycleDetectingLockFactory.newInstance(policy); 
		}

		return lockfactory.newReentrantLock(name);
	}


	public static <T> ReentrantLock lockThisObject(Class<T> clazz) {
		return lock(clazz.getSimpleName() + " lock");
	}


	public <T> void invokeAllIntialDownloadBlock(List<ConnectionNode> listThreadInPool) throws InterruptedException {
		if(listThreadInPool !=null) {
			for(ConnectionNode connectionNode:listThreadInPool) {
				threadPoolExecutor.submit(connectionNode);
			}
		}
	}

	public <T> void invokeBlock(Callable<T> task) throws InterruptedException {
		threadPoolExecutor.submit(task);
	}
	
	public <T> void invokeClient(Callable<T> task) throws InterruptedException {
		threadPoolExecutorServer.submit(task);
	}

	public <T> void shutDownBloc() { 
		//executorServiceBlockDownloaded.shutdown();
	}

	public  void onBlockChainChange(BlockChain blockchain) {
		watchdog.onBlockChainChange(blockchain);
	}

	public void addBlockChainListener(BlockChainEventHandler eventlistener) {
		logger.info("Initialisation WatchDogHandler");
		this.blockchainlistener = eventlistener;
		watchdog = new WatchDogHandler(blockchainlistener);
		executorsWatchDog.scheduleWithFixedDelay( watchdog,  0 , 20 , TimeUnit.SECONDS); 

	}


	public void stopExecutor() {
		/*	executorServiceInitialDownloadBlock.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!executorServiceInitialDownloadBlock.awaitTermination(60, TimeUnit.SECONDS)) {
				executorServiceInitialDownloadBlock.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!executorServiceInitialDownloadBlock.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			executorServiceInitialDownloadBlock.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
		 */
	}
}
