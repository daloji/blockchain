package com.daloji.blockchain.network.peers;

import java.security.Timestamp;

import com.daloji.blockchain.network.IPVersion;

public class PeerNode extends PeerParameters{
	
	
	public PeerNode(IPVersion versionIp) {
		super(versionIp);
	}

	private String host;
	
	private int port;
	
	private Timestamp time;
	

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
