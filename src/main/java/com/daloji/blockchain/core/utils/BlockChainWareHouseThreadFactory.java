package com.daloji.blockchain.core.utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.util.concurrent.CycleDetectingLockFactory;


public class BlockChainWareHouseThreadFactory {


	private ExecutorService executorService;

	private ExecutorService executorServiceBlockDownloaded;

	private static CycleDetectingLockFactory lockfactory = null;

	private static CycleDetectingLockFactory.Policy policy = CycleDetectingLockFactory.Policies.THROW;

	public static ReentrantLock lock(String name) {
		if(lockfactory == null) {
			lockfactory = CycleDetectingLockFactory.newInstance(policy); 
		}

		return lockfactory.newReentrantLock(name);
	}


	public static <T> ReentrantLock lockThisObject(Class<T> clazz) {
		return lock(clazz.getSimpleName() + " lock");
	}
	
	
	public static <T> void invokeAll(List<Callable<T>> listCallable) {
		
	}
}
