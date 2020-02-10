package com.daloji.blockchain.core.commons.proxy;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.commons.database.proxy.LevelDbProxy;
import com.daloji.blockchain.core.utils.Utils;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.crypto.*","javax.security.auth.*"})
@PrepareForTest({DB.class,LevelDbProxy.class})
public class LevelDbProxyTest {

	private static final String DATABASE = "DATABASE";

	private static DB database;


	private static Path path;

	@BeforeClass
	public static void beforeClass() throws IOException {
		path = Files.createTempDirectory(DATABASE);	
		Options options = new Options();
		options.createIfMissing(true);
		database = factory.open(path.toFile(), options);
	}

	@Before
	public void beforeTest() throws IOException {
		PowerMock.resetAll();
		PowerMock.mockStaticStrict(DB.class);


	}



	@Test
	public void addBlocNull() throws Exception {
		Whitebox.setInternalState(LevelDbProxy.class,"database", database);
		Block bloc = null;
		LevelDbProxy.getInstance().addBlock(bloc);
		PowerMock.verify();
	}


	@Test
	public void addBloc() throws Exception {
		Block expectbloc = new Block();
		expectbloc.setPrevBlockHash("previous");
		expectbloc.setTime(122223300);
		expectbloc.setTxCount(1);
		PowerMock.replayAll();
		Whitebox.setInternalState(LevelDbProxy.class,"database", database);
		LevelDbProxy.getInstance().addBlock(expectbloc);
		PowerMock.verifyAll();;
		Block bloc =LevelDbProxy.getInstance().findBlock(expectbloc.generateHash());
		Assert.assertNotNull(bloc);
		Assert.assertEquals(expectbloc.getPrevBlockHash(), bloc.getPrevBlockHash());
		Assert.assertEquals(expectbloc.getTime(), bloc.getTime());
		Assert.assertEquals(expectbloc.getTxCount(), bloc.getTxCount());
		String hash = LevelDbProxy.getInstance().getLastHash();
		Assert.assertEquals(hash, expectbloc.generateHash());
		int nbhash = LevelDbProxy.getInstance().getNbHash();
		Assert.assertEquals(nbhash, 1);
		LevelDbProxy.getInstance().deleteBlock(expectbloc.generateHash());
		database.delete(bytes("SIZE_HASH"));
		database.delete(bytes("LAST_HASH"));
		bloc =LevelDbProxy.getInstance().findBlock(expectbloc.generateHash());
		Assert.assertNull(bloc);
	}

	@AfterClass
	public  static void  after() throws IOException {
		database.close();
		FileUtils.deleteDirectory(path.toFile());
	}

	@Test
	public void addBloc_001() throws Exception {
		Block expectbloc = new Block();
		expectbloc.setPrevBlockHash("6FE28C0AB6F1B372C1A6A246AE63F74F931E8365E15A089C68D6190000000000");
		expectbloc.setTime(122223300);
		expectbloc.setTxCount(1);
		PowerMock.replayAll();
		Whitebox.setInternalState(LevelDbProxy.class,"database", database);
		LevelDbProxy.getInstance().addBlock(expectbloc);
		PowerMock.verifyAll();;
		Block bloc =LevelDbProxy.getInstance().findBlock(expectbloc.generateHash());
		Assert.assertNotNull(bloc);
		Assert.assertEquals(expectbloc.getPrevBlockHash(), bloc.getPrevBlockHash());
		Assert.assertEquals(expectbloc.getTime(), bloc.getTime());
		Assert.assertEquals(expectbloc.getTxCount(), bloc.getTxCount());
		LevelDbProxy.getInstance().deleteBlock(expectbloc.generateHash());
		database.delete(bytes("SIZE_HASH"));
		database.delete(bytes("LAST_HASH"));
		bloc =LevelDbProxy.getInstance().findBlock(expectbloc.generateHash());
		Assert.assertNull(bloc);

	}


}
