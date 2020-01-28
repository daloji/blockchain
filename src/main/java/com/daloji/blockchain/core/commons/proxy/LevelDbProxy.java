package com.daloji.blockchain.core.commons.proxy;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.locks.Lock;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.utils.Utils;

import ch.qos.logback.classic.Logger;

public class LevelDbProxy implements DatabaseExchange {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(LevelDbProxy.class);

	private static  LevelDbProxy instance = null; 

	private static final String LEVEL_DB_FILE ="Blockchain";

	/** level-Db **/
	private static DB database;

	private Lock lock;

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

			logger.info(e.getMessage());
		}
	}


	@Override
	public void addObject(Block bloc) {
		String hash = "";
		if(bloc !=null) {
			hash = bloc.generateHash();	
			database.put(bytes(hash), Utils.convertToBytes(bloc));
		}

	}


	@Override
	public Block findBlock(String hash) {
		Block bloc = null;
		ObjectInputStream is = null;
		try {
			byte[] data = database.get(bytes(hash));
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
		database.delete(bytes(hash));
	}

}
