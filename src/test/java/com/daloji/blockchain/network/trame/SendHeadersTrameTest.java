package com.daloji.blockchain.network.trame;

import org.junit.Assert;
import org.junit.Test;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.trame.SendHeadersTrame;

public class SendHeadersTrameTest {
  
	private static String trame = "F9BEB4D973656E646865616465727300000000005DF6E0E2F9BEB4D973656E64636D70637400000009000000E92F5EF80002000000000000000000000000FFFF55AA7073A7DA0C04000000000000000000000000000000000000000000000000E160DC9626AB2C82102F5361746F7368693A302E31382E302F9A59090001F9BEB4D976657261636B000000000000000000005DF6E0E2";

	private static String trame_expect = "F9BEB4D973656E64636D70637400000009000000E92F5EF80002000000000000000000000000FFFF55AA7073A7DA0C04000000000000000000000000000000000000000000000000E160DC9626AB2C82102F5361746F7368693A302E31382E302F9A59090001F9BEB4D976657261636B000000000000000000005DF6E0E2";
	@Test
	public void checkDeserilaze() {
		SendHeadersTrame sendHeader = new SendHeadersTrame();
		byte[] data = sendHeader.deserialise(Utils.hexStringToByteArray(trame));
		Assert.assertEquals(sendHeader.getMagic(),"F9BEB4D9");
		Assert.assertEquals(sendHeader.getChecksum(),"5DF6E0E2");
		Assert.assertEquals(sendHeader.getCommande(),"73656E646865616465727300");
		Assert.assertEquals(Utils.allZero(data),false);
		Assert.assertEquals(Utils.bytesToHex(data),trame_expect);

	}
	
	
	@Test
	public void checkDeserialaize_001() {
		String trame = "F9BEB4D973656E646865616465727300000000005DF6E0E2";
		SendHeadersTrame sendHeader = new SendHeadersTrame();
		byte[] data = sendHeader.deserialise(Utils.hexStringToByteArray(trame));
		Assert.assertEquals(sendHeader.getMagic(),"F9BEB4D9");
		Assert.assertEquals(sendHeader.getChecksum(),"5DF6E0E2");
		Assert.assertEquals(sendHeader.getCommande(),"73656E646865616465727300");
		Assert.assertEquals(Utils.allZero(data),true);

	}
	
}
