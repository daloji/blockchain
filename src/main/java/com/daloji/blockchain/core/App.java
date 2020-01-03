package com.daloji.blockchain.core;



import com.daloji.blockchain.network.NetworkOrchestrator;


/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args ) throws Exception
	{ 
		NetworkOrchestrator networkOrch = new NetworkOrchestrator();
		networkOrch.onStart();
		String payloda ="73656E646865616465727300"; 
		//System.out.println(Utils.hexToAscii(payloda));
	}

}
