package com.daloji.core.blockchain.net;

import java.util.concurrent.Callable;

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
