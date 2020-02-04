package com.daloji.blockchain.core.commons.proxy;

import java.util.concurrent.Callable;

public class Callable2 implements Callable<Object> {

	@Override
	public Object call() throws Exception {
		while(true) {
			System.out.println("ICIC Callable1 ");
			Thread.sleep(4);
		}
	}

}
