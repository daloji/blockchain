package com.daloji.blockchain.network.trame;

import org.junit.Assert;
import org.junit.Test;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.trame.SendCmpctTrame;



public class SendCmpctTrameTest {

	private String trame = "F9BEB4D973656E64636D70637400000009000000CCFE104A000100000000000000F9BEB4D970696E670000000000000000080000002805A05116CA6F43ED9F8068F9BEB4D96164647200000000000000001F00000013C3485C012B661D5E0C0400000000000000000000000000000000FFFF36FCD18D208DF9BEB4D9676574686561646572730000050400001BAC53D600005DF6E0E2";
	
	private String trame_expect ="F9BEB4D970696E670000000000000000080000002805A05116CA6F43ED9F8068F9BEB4D96164647200000000000000001F00000013C3485C012B661D5E0C0400000000000000000000000000000000FFFF36FCD18D208DF9BEB4D9676574686561646572730000050400001BAC53D600005DF6E0E2";
	@Test
	public void chechDeserialize() {
		SendCmpctTrame sendcmpt = new SendCmpctTrame();
		byte[] data = sendcmpt.deserialise(Utils.hexStringToByteArray(trame));
		Assert.assertEquals(Utils.bytesToHex(data), trame_expect);
		Assert.assertEquals(sendcmpt.getIndex(), 0);
		Assert.assertEquals(sendcmpt.getCmptVersion(), "0100000000000000");
	}
}
