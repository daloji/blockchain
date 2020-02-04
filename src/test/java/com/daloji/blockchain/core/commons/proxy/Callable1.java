package com.daloji.blockchain.core.commons.proxy;

import java.util.concurrent.Callable;

public class Callable1  implements Callable<Object> ,CallableStop{

	private volatile boolean istopping = true;

	private String info;
	public Callable1(String info) {
		this.info =info;
	}
	@Override
	public Object call() throws Exception {
		while(istopping) {
			System.out.println(info);
			Thread.sleep(5);
		}
		return null;
	}

	@Override
	public void onStopThread() {
		System.out.println("Callable1 STOpping ");
		istopping = false;
	}

}
