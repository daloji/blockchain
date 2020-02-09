package com.daloji.blockchain.core.commons.proxy;

import java.io.File;

import org.easymock.EasyMock;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.daloji.blockchain.core.Block;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.crypto.*","javax.security.auth.*"})
@PrepareForTest({DB.class,Iq80DBFactory.class})
public class LevelDbProxyTest {


	
	@MockStrict
	private static DB database;
	
	@MockStrict
	private Iq80DBFactory factory;

	
	@Before
	public void beforeTest() {
		PowerMock.resetAll();
		PowerMock.mockStaticStrict(DB.class);
		PowerMock.mockStaticStrict(Iq80DBFactory.class);

	}
	
	@Test
	public void addDataLevelDb() throws Exception {
	    PowerMock.expectNew(Iq80DBFactory.class).andReturn(factory);
		EasyMock.expect(factory.open(EasyMock.anyObject(File.class), EasyMock.anyObject(Options.class))).andReturn(database);
		//EasyMock.expect(leveldb.getNbHash()).andReturn(1);
		Block bloc = new Block();
		bloc.setPrevBlockHash("previous");
		bloc.setTime(122223300);
		bloc.setTxCount(1);
		PowerMock.replayAll();
		LevelDbProxy.getInstance().addBlock(bloc);
		PowerMock.verify();
	}

	
}
