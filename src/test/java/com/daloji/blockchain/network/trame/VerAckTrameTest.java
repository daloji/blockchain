package com.daloji.blockchain.network.trame;

import org.junit.Test;

import com.daloji.blockchain.core.Utils;

public class VerAckTrameTest {

	public static final String trame ="F9BEB4D976657261636B000000000000000000005DF6E0E200";
	
	//@Test
	public void checkVerAckDeSerialisation() {
			
		
		VersionAckTrame verack = new VersionAckTrame();
		String deserialiser =verack.deserialise(Utils.hexStringToByteArray(trame));
		
	}
}
