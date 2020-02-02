package com.daloji.blockchain.core.commons.proxy;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.locks.ReentrantLock;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.utils.BlockChainWareHouseThreadFactory;
import com.daloji.blockchain.core.utils.Utils;

import ch.qos.logback.classic.Logger;

public class LevelDbProxy implements DatabaseExchange {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(LevelDbProxy.class);

	private static  LevelDbProxy instance = null; 

	private static  String LEVEL_DB_FILE ="Blockchain";

	private static String LAST_HASH = "LAST_HASH";

	private static String NB_HASH = "NB_HASH";

	protected final ReentrantLock lock = BlockChainWareHouseThreadFactory.lockThisObject(LevelDbProxy.class);

	/** level-Db **/
	private static DB database;


	/**
	 * singleton
	 * 
	 * @return
	 */
	public static LevelDbProxy getInstance()
	{
		if(instance == null) {
			initDatabase();
			instance = new LevelDbProxy();
		}

		return instance;
	}


	private static void initDatabase()  {
		Options options = new Options();
		options.createIfMissing(true);
		try {
			database = factory.open(new File(LEVEL_DB_FILE), options);
		} catch (IOException e) {

			logger.info(e.getMessage());
			database = null;
		}

	}

	public void closeDatabase() {
		try {
			if(database !=null) {
				database.close();	
			}

		} catch (IOException e) {

			logger.error(e.getMessage());
		}
	}



	@Override
	public void addBlock(Block bloc) {
		if(bloc !=null) {
			String hash = bloc.generateHash();
			if(!existKeys(hash)) {
				lock.lock();
				database.put(Utils.hexStringToByteArray(hash), Utils.convertToBytes(bloc));
				database.put(bytes(LAST_HASH), Utils.hexStringToByteArray(hash));
//				incrementNbHash();
				lock.unlock();		
			}
		}
	}


	@Override
	public Block findBlock(String hash) {
		Block bloc = null;
		ObjectInputStream is = null;
		try {
			byte[] data = database.get(Utils.hexStringToByteArray(hash));
			if(data !=null) {
				ByteArrayInputStream in = new ByteArrayInputStream(data);
				is = new ObjectInputStream(in);
				bloc = (Block) is.readObject();
				if(is!=null) {
					is.close();
				}
				if(in != null) {
					in.close();
				}
			}
		} catch (IOException | ClassNotFoundException ex) {
			logger.error(ex.getMessage());
		}
		return bloc ;
	}



	@Override
	public void deleteBlock(String hash) {
		database.delete(Utils.hexStringToByteArray(hash));
	}



	@Override
	public DBIterator getIterator() {
		return database.iterator();
	}


	@Override
	public long countKeys() {
		int countkey = 0;
		DBIterator dbiterator = database.iterator();
		while(dbiterator.hasNext()) {
			countkey++;
			dbiterator.next();
		}
		return countkey;
	}


	@Override
	public boolean existKeys(String hash) {
		boolean value = false;
		Block block = findBlock(hash);
		if(block != null) {
			value = true;
		}

		return value;
	}


	@Override
	public <T> void addObject(String hash,T object) {
		lock.lock();
		database.put(bytes(hash), Utils.convertToBytes(object));
		lock.unlock();	
	}


	@Override
	public void incrementNbHash() {
		long nbhash = 0;
		String nblong = getObject(NB_HASH);
		if(nblong != null) {
			nbhash = Long.parseLong(nblong) +1;
		}
		addObject(NB_HASH, Long.toString(nbhash));

	}


	@Override
	public <T> T getObject(String hash) {
		T object = null;
		ObjectInputStream is = null;
		byte[] data = database.get(bytes(hash));
		if(data !=null) {
			object = (T) Utils.bytesToHex(data);
		}
		return object ;
	}

}
