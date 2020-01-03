package com.daloji.blockchain.network;

import java.util.concurrent.Callable;

import com.daloji.blockchain.network.peers.PeerNode;

public class DnsCallable  implements Callable<PeerNode>{

	private PeerNode peer;


	public  DnsCallable(PeerNode peer) {
		this.peer = peer;
	}



	public PeerNode call() throws Exception {
		if(peer!=null) {
			
		}
		return peer;
	}

}
