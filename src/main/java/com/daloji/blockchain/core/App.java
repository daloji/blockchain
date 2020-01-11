package com.daloji.blockchain.core;



import com.daloji.blockchain.network.NetworkOrchestrator;

public class App 
{
	public static void main( String[] args ) throws Exception
	{ 


		NetworkOrchestrator networkOrch = new NetworkOrchestrator();
		networkOrch.onStart();
		

	}

}
