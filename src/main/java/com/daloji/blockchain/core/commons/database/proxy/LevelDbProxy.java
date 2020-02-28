package com.daloji.blockchain.core.commons.database.proxy;

import static org.iq80.leveldb.impl.Iq80DBFactory.asString;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.core.utils.BlockChainWareHouseThreadFactory;
import com.daloji.blockchain.core.utils.Utils;



public class LevelDbProxy implements DatabaseExchange {

	private static final Logger logger =  LoggerFactory.getLogger(LevelDbProxy.class);

	private static  LevelDbProxy instance = null; 

	private static  String LEVEL_DB_FILE ="level-db";

	private static String LAST_HASH = "LAST_HASH";

	private static String NB_HASH = "NB_HASH";

	private static String SIZE_HASH = "SIZE_HASH";
	
	private static String BLOCKCHAIN_NETWORK_SIZE ="BLOCKCHAIN_NETWORK_SIZE";
	
	
	private static String IDB_STOP ="IDB_STOP";


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
			if(database == null) {
				database = factory.open(new File(LEVEL_DB_FILE), options);
			}
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
				updateStatus(bloc);
				lock.unlock();		
			}
		}
	}


	@Override
	public Block findBlock(String hash) {
		Block bloc = null;
		ObjectInputStream is = null;
		try {
			if(hash !=null) {
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
		database.put(bytes(hash), Utils.convertToBytes(object));
	}


	@Override
	public void incrementNbHash() {
		long nbhash = 0;
		String strnblong = getObject(NB_HASH);
		if(strnblong != null) {
			nbhash = Long.parseLong(strnblong) +1;
		}
		addObject(NB_HASH, Long.toString(nbhash));

	}


	@Override
	public <T> T getObject(String hash) {
		T object = null;
		byte[] data = database.get(bytes(hash));
		if(data !=null) {
			object = (T) Utils.bytesToHex(data);
		}
		return object ;
	}



	public void updateStatus(final Block bloc) {
		long nbhash = 0;
		String previoushash = null;
		int nb = 0;
		Block blocgenesis = new Block();
		String genesisHash = blocgenesis.getHashGenesisBloc();
		String value = asString(database.get(bytes(SIZE_HASH)));
		if(value !=null) {
			nbhash = Integer.parseInt(value);
		}
		Block newBloc = bloc;
		if(newBloc !=null) {
			String hash = newBloc.generateHash();
			while(newBloc!=null) {
				previoushash = newBloc.getPrevBlockHash();
				newBloc = findBlock(previoushash);
				nb++;
			}
			if(nb>nbhash) {
				String strnb =String.valueOf(nb);
				database.put(bytes(SIZE_HASH), bytes(strnb));
				database.put(bytes(LAST_HASH), Utils.hexStringToByteArray(hash));
				long depthblock = getBlockChainDepth();
				String idb = "true";
				if(depthblock-1000<nb) {
					idb = "false";
				}
				database.put(bytes(IDB_STOP), bytes(idb));
			}else {
				if(nbhash ==0) {
					database.put(bytes(LAST_HASH), bytes(genesisHash));	
				}
			}
		}
	}

	@Override
	public Pair<Retour,String> checkBlocChainStatus() {
		Pair<Retour, String> retour;
		boolean valid = false;
		Block bloc = new Block();
		String genesisHash = bloc.getHashGenesisBloc();
		String previoushash = genesisHash;
		String lasthash =getLastHash();
		bloc = findBlock(lasthash);
		while(bloc!=null) {
			previoushash = bloc.getPrevBlockHash();
			bloc = findBlock(previoushash);
			if(bloc != null) {
				if(genesisHash.equals(bloc.getPrevBlockHash())) {
					valid = true;
				}
			}
		}

		if(!valid) {
			retour = new Pair<Retour,String>(Utils.createRetourNOK("hash manquant", "previoushash"), previoushash);
		}else {
			retour = new Pair<Retour,String>(Utils.createRetourOK(), null);

		}
		return retour;
	}





	@Override
	public String getLastHash() {
		String lastHash;
		byte[] data = database.get(bytes(LAST_HASH));
		if(data !=null) {
			lastHash = Utils.bytesToHex(data);
		}
		else {
			Block block = new Block();
			lastHash = block.getHashGenesisBloc();
		}
		return lastHash;
	}


	@Override
	public int getNbHash() {
		int nbhash = 0;
		String value = asString(database.get(bytes(SIZE_HASH)));
		if(value !=null) {
			nbhash = Integer.parseInt(value);
		}
		return nbhash;
	}


	@Override
	public void addBlockChainDepth(long nbblock) {
		String strnb =String.valueOf(nbblock);
		database.put(bytes(BLOCKCHAIN_NETWORK_SIZE), bytes(strnb));	
	}


	@Override
	public long getBlockChainDepth() {
		int nbhash = 0;
		String value = asString(database.get(bytes(BLOCKCHAIN_NETWORK_SIZE)));
		if(value !=null) {
			nbhash = Integer.parseInt(value);
		}
		return nbhash;
	}


	@Override
	public boolean isInitialDownloadBlock() {
		String value = asString(database.get(bytes(IDB_STOP)));
		boolean isIdb = true;
		if(value !=null) {
			isIdb = Boolean.parseBoolean(value);
		}
		return isIdb;
	}

}
