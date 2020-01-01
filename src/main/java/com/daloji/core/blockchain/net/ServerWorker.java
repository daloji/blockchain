package com.daloji.core.blockchain.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerWorker implements Runnable{

    protected int  serverPort;
	
	protected ServerSocket serverSocket;
	
	protected boolean isStopped = false;
		
	protected Thread runningThread;
	
	protected ExecutorService threadPool = 	Executors.newFixedThreadPool(10);

	public ServerWorker(int port){
		this.serverPort = port;
	}

	public void run(){
		synchronized(this){
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		while(! isStopped()){
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (IOException e) {
				if(isStopped()) {
					System.out.println("Server Stopped.") ;
					break;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}
			//this.threadPool.execute(new ConnectionHandler(clientSocket,"Thread Pooled Server"));
		}
		this.threadPool.shutdown();
		System.out.println("Server Stopped.") ;
	}


	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop(){
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port " +serverPort, e);
		}
	}
	
	/*
	 * ThreadPooledServer server = new ThreadPooledServer(9000);
new Thread(server).start();

try {
    Thread.sleep(20 * 1000);
} catch (InterruptedException e) {
    e.printStackTrace();
}
System.out.println("Stopping Server");
server.stop();*/
	 
}
