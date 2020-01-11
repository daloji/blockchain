package com.daloji.blockchain.core;

public class Inventory {

	private InvType type;
	
	private String hash;

	public InvType getType() {
		return type;
	}

	public void setType(InvType type) {
		this.type = type;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
