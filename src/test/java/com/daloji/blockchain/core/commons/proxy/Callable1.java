package com.daloji.blockchain.core.commons.proxy;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class Callable1 implements Callable<Object> ,CallableStop{

	private volatile boolean istopping = true;
	Timer timer = new Timer(true);

	private String info;
	public Callable1(String info) {
		this.info =info;
	}
	@Override
	public Object call() throws Exception {
		try {
		      timer.schedule(new TimeOutTimer(Thread.currentThread()), 10000);
				while(istopping) {
					System.out.println(info);
					Thread.sleep(5);
				}
			
		} catch (InterruptedException e) {
		System.out.println("interrupt  Thread "+info);
		}
		return null;
	}

	@Override
	public void onStopThread() {
		System.out.println("Callable1 STOpping ");
		istopping = false;
	}


	class TimeOutTimer extends TimerTask {
		Thread t;

		TimeOutTimer(Thread t) {
			this.t = t;
		}

		@Override
		public void run() {
			if (t != null && t.isAlive()) {
				//t.interrupt();
				onStopThread();
			}
		}
	}
}
