package com.daloji.blockchain.core;

import java.io.Serializable;

/**
 * Element primitif de la blockchain 
 * 
 *  * @author daloji
 *
 */
public class Block implements Serializable{


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
	private int  txn_count = 0;
	

}
