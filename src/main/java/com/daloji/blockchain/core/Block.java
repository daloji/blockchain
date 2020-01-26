package com.daloji.blockchain.core;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.daloji.blockchain.network.trame.BlockTrame;

/**
 * Element primitif de la blockchain 
 * 
 *  * @author daloji
 *
 */
public class Block implements Serializable{


	private static String GENESIS_MERKEL_ROOT ="3BA3EDFD7A7B12B27AC72C3E67768F617FC81BC3888A51323A9FB8AA4B1E5E4A";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * information sur la version
	 */
	private long version;
	/**
	 * hash du bloc precedent
	 */
	private String  prevBlockHash;

	/**
	 * hash de l'arbre merkle
	 */
	private String  merkleRoot;
	/**
	 *  date de la creation du bloc
	 */
	private long time;
	/**
	 * difficulte du block
	 */
	private long difficultyTarget; // "nBits"
	/**
	 *  nonce padding
	 */

	private long nonce;
	/**
	 * nombre de transaction en entree
	 */
	private int  txCount = 0;


	private List<Transaction> listTransaction;

	
	
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getPrevBlockHash() {
		return prevBlockHash;
	}

	public void setPrevBlockHash(String prevBlockHash) {
		this.prevBlockHash = prevBlockHash;
	}

	public String getMerkleRoot() {
		return merkleRoot;
	}

	public void setMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
	}
	public long getTime() {

		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getDifficultyTarget() {
		return difficultyTarget;
	}
	public void setDifficultyTarget(long difficultyTarget) {
		this.difficultyTarget = difficultyTarget;
	}
	public long getNonce() {
		return nonce;
	}
	public void setNonce(long nonce) {
		this.nonce = nonce;
	}
	

	public int getTxCount() {
		return txCount;
	}

	public void setTxCount(int txCount) {
		this.txCount = txCount;
	}

	/**
	 * Block genesis de la blockchain crée par Satoshi Nakamoto
	 * @return
	 */
	public Block createGenesisBlock() {
		Block block =new Block();
		// epoch hexa 495FAB29  3 January 2009 18:15:05
		block.setTime(1231006505);
		block.setPrevBlockHash("0000000000000000000000000000000000000000000000000000000000000000");
		block.setVersion(1);
		block.setMerkleRoot(GENESIS_MERKEL_ROOT);
		block.setDifficultyTarget(0x1d00ffff);
		block.setNonce(2083236893);
		return block;
	}


	public List<Transaction> getListTransaction() {
		return listTransaction;
	}

	/**
	 * generation hash du bloc
	 * @return
	 * @throws IOException 
	 */
	public String generateHash() throws IOException {
		String info = "";
		info = info + Utils.intHexpadding((int)version,4);
		info = info + prevBlockHash;
		info = info + merkleRoot;
		info = info + Utils.convertDateString(time, 4);
		info = info + Utils.intHexpadding((int)difficultyTarget,4);
		info = info + Utils.intHexpadding((int)nonce,4);
		byte[] hashbyte = Crypto.doubleSha256(Utils.hexStringToByteArray(info));
		//byte[] revertbyte = Utils.revBytes(hashbyte);
		return Utils.bytesToHex(hashbyte);
	}


	/**
	 * 
	 * Recuperation du hash de la genesis block
	 * @throws IOException 
	 * 
	 */

	public String getHashGenesisBloc() throws IOException {
		Block bl =createGenesisBlock();
		return bl.generateHash();
	}


	public void setListTransaction(List<Transaction> listTransaction) {
		this.listTransaction = listTransaction;
	}


	public static Block buildFromBlockTrame(BlockTrame bloctrame) {
		Block bloc=null;
		if(bloctrame != null) {
			bloc = new Block();
			bloc.setMerkleRoot(bloctrame.getMerkelRoot());
			bloc.setNonce(bloctrame.getNonce());
			bloc.setPrevBlockHash(bloctrame.getPreviousHash());
			bloc.setTime(bloctrame.getTime());
			bloc.setVersion(Utils.little2big(bloctrame.getVersion()));
			bloc.setDifficultyTarget(bloctrame.getnBits());
		}
		return bloc;
	}

	@Override
	public String toString() {
		String transact = "";
		if(listTransaction != null) {
			for(Transaction transaction:listTransaction) {
				List<TransactionInput> listInput= transaction.getTxIn();
				if(listInput !=null) {
					for(TransactionInput transactioninput:listInput) {
						transact = transact +transactioninput.toString();
					}
				}
				List<TransactionOutput> listout= transaction.getTxOut();
				if(listInput !=null) {
					for(TransactionOutput transactionoutput:listout) {
						transact = transact +transactionoutput.toString();
					}
				}
			}
		}
		return "Block [version=" + version + ", prevBlockHash=" + prevBlockHash + ", merkleRoot=" + merkleRoot
				+ ", time=" + time + ", difficultyTarget=" + difficultyTarget + ", nonce=" + nonce + ", txCount="
				+ txCount + ", listTransaction=" + transact + "]";
	}

	
	
}
