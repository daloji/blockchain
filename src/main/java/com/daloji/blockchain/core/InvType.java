package com.daloji.blockchain.core;

public enum InvType {

	ERROR(0),

	MSG_TX(1),
	
	MSG_BLOCK(2),
	
	MSG_FILTERED_BLOCK(3),
	
	MSG_CMPCT_BLOCK(4);

	protected int value;


	private  InvType(final int value) {
		this.value = value;
	}


	public int getValue() {
		return value;
	}

}
