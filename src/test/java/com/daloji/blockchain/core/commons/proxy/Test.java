package com.daloji.blockchain.core.commons.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {

	
	static Callable1 callable,callable1,callable2,callable3,callable4;
	
	public static void main(String[] args) throws InterruptedException {
		//ExecutorService  executorServiceInitialDownloadBlock =   Executors.newFixedThreadPool(3);
		ExecutorService executorServiceBlockDownloaded =new ThreadPoolExecutor( 3, 3, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

		callable = new Callable1("callable 1");
		callable1 = new Callable1("callable 2");
		callable2 = new Callable1("callable 3");
		callable3 = new Callable1("callable 4");
		callable4 = new Callable1("callable 5");

		List<Callable1> listcallble = new ArrayList<Callable1>();
		listcallble.add(callable);
		executorServiceBlockDownloaded.submit(callable);
		executorServiceBlockDownloaded.submit(callable1);
		executorServiceBlockDownloaded.submit(callable2);
		executorServiceBlockDownloaded.submit(callable3);
		executorServiceBlockDownloaded.submit(callable4);

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
