package com.daloji.blockchain.core;

import java.io.Serializable;
import java.util.List;

public class Inv implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private List<Inventory> listInventory;


	private String payload;


	public List<Inventory> getListInventory() {
		return listInventory;
	}


	public void setListInventory(List<Inventory> listInventory) {
		this.listInventory = listInventory;
	}


	public String getPayload() {
		return payload;
	}


	public void setPayload(String payload) {
		this.payload = payload;
	}



}
