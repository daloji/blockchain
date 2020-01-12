package com.daloji.blockchain.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 
 * @author daloji
 *
 */
public class Crypto {

	
	/**
	 * Double hash sha-256 
	 * @param input
	 *   donnée a hasher
	 * @return 
	 */
	public static byte[] doubleSha256(byte[] input) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.update(input, 0, input.length);
			return digest.digest(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}

	}
}
