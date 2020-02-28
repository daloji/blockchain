package com.daloji.blockchain.core.commons.database.proxy;

import org.iq80.leveldb.DBIterator;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.commons.Retour;

public interface DatabaseExchange {

	public void addBlock(Block bloc);
	
	public Block findBlock(String hash);
	
	public void deleteBlock(String hash);
	
	public void incrementNbHash();
	
	public <T> void addObject(String hash,T object);
	
	public DBIterator getIterator();
	
	public boolean existKeys(String hash);
	
	public <T> T getObject(String hash);
	
	public long countKeys();
	
	public Pair<Retour,String> checkBlocChainStatus();
	
	public int getNbHash();
	
	public String getLastHash();
	
	public void  addBlockChainDepth(long nbblock);
	
	public long  getBlockChainDepth();
	
	public boolean isInitialDownloadBlock();
	
}
