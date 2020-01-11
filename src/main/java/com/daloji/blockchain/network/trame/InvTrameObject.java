package com.daloji.blockchain.network.trame;

import com.daloji.blockchain.core.Inv;

public class InvTrameObject extends ObjectTrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Inv inventory;


	public Inv getInventory() {
		return inventory;
	}


	public void setInventory(Inv inventory) {
		this.inventory = inventory;
	}
	

}
