package com.daloji.blockchain.network.trame;

import java.io.Serializable;

public class ObjectTrame  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private TrameType  type;
	
	private boolean isPartialTrame;
	
	private String payload;

	
	public boolean isPartialTrame() {
		return isPartialTrame;
	}

	public void setPartialTrame(boolean isPartialTrame) {
		this.isPartialTrame = isPartialTrame;
	}

	public TrameType getType() {
		return type;
	}

	public void setType(TrameType type) {
		this.type = type;
	}


	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
	
}
