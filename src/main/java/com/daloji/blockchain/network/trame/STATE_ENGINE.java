package com.daloji.blockchain.network.trame;

public enum STATE_ENGINE {
	
	BOOT("boot"),
	
	VERSION_SEND("version send"),

	VERSION_RECEIVE("version receive"),
	
	VERSION_PARTIAL_RECEIVE("version partial receive"),
	
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
	
	GETBLOCK_SEND("getblock send"),
	
	GETDATA_SEND("getdata send"),
	
	PARTIAL_TRAME("partial trame"),
	
	READY("ready"),
	
	START("start"),
	
	STOP("stop"),
	
	GET_ADDR_SEND("getAddr send"),
	
	ERROR_PROTOCOLE("ERROR"),
	
	ERROR("ERROR");

	protected String info;

	private  STATE_ENGINE(final String info) {
		this.info =	info;
	}



}
