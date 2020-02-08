package com.daloji.blockchain.core;

import java.io.Serializable;

public class Addr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ip;

	private int port;
	
	private long epoch;
	
	private String service;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getEpoch() {
		return epoch;
	}

	public void setEpoch(long epoch) {
		this.epoch = epoch;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

}
