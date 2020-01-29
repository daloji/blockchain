package com.daloji.blockchain.core.commons.proxy;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import org.iq80.leveldb.DBIterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.utils.Utils;


//@RunWith(PowerMockRunner.class)
//@PowerMockIgnore({"javax.crypto.*","javax.security.auth.*"})
//@PrepareForTest({LevelDbProxy.class})
public class LevelDbProxyTest {

	
	//@MockStrict
	private LevelDbProxy levelDbproxy;
	
	//@Before
	public void beforeTest() {
		PowerMock.resetAll();
		
	}
	
	//@Test
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
