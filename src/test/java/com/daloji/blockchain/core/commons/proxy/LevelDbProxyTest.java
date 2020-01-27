package com.daloji.blockchain.core.commons.proxy;

import org.junit.Assert;
import org.junit.Test;

import com.daloji.blockchain.core.Block;

public class LevelDbProxyTest {

	 @Test
	public void  addLevelDb() {
		 Block bloc = new Block();
		 bloc.setPrevBlockHash("previous");
		 bloc.setTime(122223300);
		 bloc.setTxCount(1);
		 LevelDbProxy.getInstance().addObject(bloc);
		 Block blockreceive = LevelDbProxy.getInstance().findBlock(bloc.generateHash());
		 Assert.assertNotNull(blockreceive);
		 Assert.assertEquals(blockreceive.getTime(), bloc.getTime());
		 Assert.assertEquals(blockreceive.getTxCount(), bloc.getTxCount());
		 LevelDbProxy.getInstance().deleteBlock(bloc.generateHash());
		  blockreceive = LevelDbProxy.getInstance().findBlock(bloc.generateHash());
		 Assert.assertNull(blockreceive);
		 LevelDbProxy.getInstance().closeDatabase();
	}		 

}
