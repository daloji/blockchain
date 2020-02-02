package com.daloji.blockchain.core.commons.proxy;

import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.MockStrict;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.daloji.blockchain.core.Block;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.crypto.*","javax.security.auth.*"})
@PrepareForTest({LevelDbProxy.class})
public class LevelDbProxyTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@MockStrict
	private LevelDbProxy levelDbproxy;
	
	private static DB database;

	@Before
	public void beforeTest() throws IOException {
		PowerMock.resetAll();
		ClassLoader classLoader = LevelDbProxyTest.class.getClassLoader();
		File file = new File(classLoader.getResource("database/Blockchain/").getFile());
		tempFolder.create();
		///System.out.println(file.getCanonicalPath());	
		Path sourcepath= Paths.get(file.getCanonicalPath()+"/000010.log");
		Path destfile = Paths.get(tempFolder.getRoot().getAbsolutePath()+"/000010.log");
		Files.copy( sourcepath,destfile, StandardCopyOption.REPLACE_EXISTING);
		sourcepath= Paths.get(file.getCanonicalPath()+"/000007.sst");
		destfile = Paths.get(tempFolder.getRoot().getAbsolutePath()+"/000007.sst");
		Files.copy( sourcepath,destfile, StandardCopyOption.REPLACE_EXISTING);
		sourcepath= Paths.get(file.getCanonicalPath()+"/CURRENT");
		destfile = Paths.get(tempFolder.getRoot().getAbsolutePath()+"/CURRENT");
		Files.copy( sourcepath,destfile, StandardCopyOption.REPLACE_EXISTING);
		sourcepath= Paths.get(file.getCanonicalPath()+"/LOCK");
		destfile = Paths.get(tempFolder.getRoot().getAbsolutePath()+"/LOCK");
		Files.copy( sourcepath,destfile, StandardCopyOption.REPLACE_EXISTING);
		sourcepath= Paths.get(file.getCanonicalPath()+"/MANIFEST-000009");
		destfile = Paths.get(tempFolder.getRoot().getAbsolutePath()+"/MANIFEST-000009");
		Files.copy( sourcepath,destfile, StandardCopyOption.REPLACE_EXISTING);
		Options options = new Options();
		options.createIfMissing(true);
		database = factory.open(new File(tempFolder.getRoot().toString()), options);
	}
	
	@AfterClass
	public static void after() throws IOException {
		database.close();
	}

	@Test
	public void  addLevelDb() {
		Block bloc = new Block();
		bloc.setPrevBlockHash("previous");
		bloc.setTime(122223300);
		bloc.setTxCount(1);
		PowerMock.replayAll();
		Whitebox.setInternalState(LevelDbProxy.class,database );
		LevelDbProxy.getInstance().addBlock(bloc);
		Block blockreceive = LevelDbProxy.getInstance().findBlock(bloc.generateHash());
		Assert.assertNotNull(blockreceive);
		Assert.assertEquals(blockreceive.getTime(), bloc.getTime());
		Assert.assertEquals(blockreceive.getTxCount(), bloc.getTxCount());
		LevelDbProxy.getInstance().deleteBlock(bloc.generateHash());
		blockreceive = LevelDbProxy.getInstance().findBlock(bloc.generateHash());
		Assert.assertNull(blockreceive);
		String hash = LevelDbProxy.getInstance().getObject("LAST_HASH");
		Assert.assertEquals(hash, bloc.generateHash());
		LevelDbProxy.getInstance().closeDatabase();
		PowerMock.verify();
	}		 

	


	@Test
	public void listKeyValue() throws IOException {

		
		PowerMock.replayAll();
		Whitebox.setInternalState(LevelDbProxy.class,database );
		DBIterator dbiterator = LevelDbProxy.getInstance().getIterator();
		dbiterator.seekToFirst();
		int countkey = 0;
		while(dbiterator.hasNext()) {
			countkey++;
			dbiterator.next();
		}
		dbiterator.close();
		/*
		for(dbiterator.seekToFirst(); dbiterator.hasNext(); dbiterator.next()) {
			String key = Utils.bytesToHex(dbiterator.peekNext().getKey());
			//  String value = asString(dbiterator.peekNext().getValue());
			System.out.println(" keys :"+Utils.StrLittleEndian(key));
		}
		 */
		PowerMock.verify();
		Assert.assertEquals(countkey, 12);
		
	}
}
