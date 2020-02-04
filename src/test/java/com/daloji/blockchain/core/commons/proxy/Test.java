package com.daloji.blockchain.core.commons.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

	
	static Callable1 callable,callable1;
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService  executorServiceInitialDownloadBlock =   Executors.newFixedThreadPool(3);
		callable = new Callable1("callable 1");
		callable1 = new Callable1("callable 2");
		List<Callable1> listcallble = new ArrayList<Callable1>();
		listcallble.add(callable);
		executorServiceInitialDownloadBlock.submit(callable);
		executorServiceInitialDownloadBlock.submit(callable1);
		//invokeAll(listcallble);
		System.out.println("******************************");
		int nb =0;
		while(true) {
			System.out.println("MAINNNNNN ");
			try {
				nb++;
				Thread.sleep(15);
				if(nb==1000) {
					callable.onStopThread();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
