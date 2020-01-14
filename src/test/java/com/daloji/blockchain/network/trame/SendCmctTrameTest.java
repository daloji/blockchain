package com.daloji.blockchain.network.trame;

import org.junit.Test;

import com.daloji.blockchain.core.Utils;

public class SendCmctTrameTest {

	private String trame = "F9BEB4D973656E64636D70637400000009000000E92F5EF80002000000000000000000000000FFFF55AA7073A7DA0C04000000000000000000000000000000000000000000000000E160DC9626AB2C82102F5361746F7368693A302E31382E302F9A59090001F9BEB4D976657261636B000000000000000000005DF6E0E2";
	@Test
	public void chechDeserialize() {
		SendCmpctTrame sendcmpt = new SendCmpctTrame();
		byte[] data = sendcmpt.deserialise(Utils.hexStringToByteArray(trame));
	}
}
