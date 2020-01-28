package com.daloji.blockchain.core.utils;

import java.util.concurrent.locks.ReentrantLock;

import com.google.common.util.concurrent.CycleDetectingLockFactory;


public class BlockChainThreadFactory {

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
}
