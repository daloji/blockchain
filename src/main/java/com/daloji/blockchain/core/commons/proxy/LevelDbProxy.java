package com.daloji.blockchain.core.commons.proxy;

import java.io.File;
import java.io.IOException;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.slf4j.LoggerFactory;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import ch.qos.logback.classic.Logger;

public class LevelDbProxy {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(LevelDbProxy.class);

	private static final LevelDbProxy instance = new LevelDbProxy();

	private static final String LEVEL_DB_FILE ="Blockchain";

	/** level-Db **/
	private static DB database;

	/**
	 * singleton
	 * 
	 * @return
	 */
	public static LevelDbProxy getInstance()
	{
		initDatabase();
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

	private void closeDatabase() {
		try {
			if(database !=null) {
				database.close();	
			}

		} catch (IOException e) {

			logger.info(e.getMessage());
		}
	}

}
