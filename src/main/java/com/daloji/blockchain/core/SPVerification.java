package com.daloji.blockchain.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.daloji.blockchain.network.peers.PeerNode;

public class SPVerification {

	private static SPVerification instance = null;

	
	private ConcurrentHashMap<PeerNode,List<Inventory>> mempool = null;
	
	public static SPVerification getInstance()
	{
		if(instance==null) {
			instance = new SPVerification();
		}
		return instance;
	}

	public SPVerification() {
		mempool = new ConcurrentHashMap<PeerNode, List<Inventory>>();
	}


	public void addInventory(PeerNode peer,Inventory inventory) {
		 List<Inventory> listinv = mempool.get(peer);
		 if(listinv == null) {
			 listinv = new ArrayList<Inventory>();
			 listinv.add(inventory);
		 }else {
			 listinv.add(inventory);
		 }
		 mempool.put(peer, listinv) ;
	}
	
	
	public void addInventory(PeerNode peer,List<Inventory> listInventory) {
		 List<Inventory> listinv = mempool.get(peer);
		 if(listinv == null) {
			 listinv = new ArrayList<Inventory>();
			 listinv.addAll(listInventory);
		 }else {
			 listinv.addAll(listInventory);
		 }
		 mempool.put(peer, listinv) ;
	}
	
	
	
	public List<Inventory> getListInventory(PeerNode peer){
		 List<Inventory> listinv = mempool.get(peer);
		 return listinv;
	}

}
