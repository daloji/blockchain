package com.daloji.blockchain.core.commons.proxy;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.iq80.leveldb.DBIterator;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.powermock.reflect.Whitebox;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.BlockChainHandler;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.trame.InvTrameTest;
import com.daloji.blockchain.network.trame.STATE_ENGINE;

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


//@Test
	public void listKeyValue() throws IOException {
		ClassLoader classLoader = LevelDbProxyTest.class.getClassLoader();
		File file = new File(classLoader.getResource("database").getPath());
		PowerMock.replayAll();
		Whitebox.setInternalState(LevelDbProxy.getInstance(), "LEVEL_DB_FILE", file.getAbsolutePath()+"/blockchain");
		DBIterator dbiterator = LevelDbProxy.getInstance().getIterator();
		try {
		  for(dbiterator.seekToFirst(); dbiterator.hasNext(); dbiterator.next()) {
		    String key = Utils.bytesToHex(dbiterator.peekNext().getKey());
		  //  String value = asString(dbiterator.peekNext().getValue());
		    System.out.println(" keys :"+Utils.StrLittleEndian(key));
		  }
		}catch (Exception e) {
			// TODO: handle exception
		} finally {
		  // Make sure you close the iterator to avoid resource leaks.
			dbiterator.close();
		}
		PowerMock.verify();
	}
}
