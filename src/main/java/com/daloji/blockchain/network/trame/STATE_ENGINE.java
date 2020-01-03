package com.daloji.blockchain.network.trame;

public enum STATE_ENGINE {
	
	START("Start"),
	
	VERSION_SEND("version send"),

	VERSION_RECEIVE("version receive"),
	
	VER_ACK_RECEIVE("version ack receive"),

	VER_ACK_SEND("verack send"),
	
	ADDR_RECEIVE("addr receive"),
	
	ADDR_SEND("addr send"),
	
	SENDHEADERS_SEND("sendheaders send"),
	
	SENDHEADERS_RECEIVE("sendheaders receive"),
	
	TX_SEND("TX send"),
	
	TX_RECEIVE("TX receive"),
	
	INV_SEND("inv send"),
	
	INV_RECEIVE("inv receive"),

	SENDCMPCT_SEND("sendcmpct send"),
	
	SENDCMPCT_RECEIVE("sendcmpct receive"),
	
	
	ERROR("ERROR");

	protected String info;

	private  STATE_ENGINE(final String info) {
		this.info =	info;
	}



}
