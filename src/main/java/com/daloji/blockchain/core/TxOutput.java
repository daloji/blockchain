package com.daloji.blockchain.core;

import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.utils.Utils;

public class TxOutput  extends TransactionOutput{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Pair<TxOutput,byte[]> buildTxOutput(byte[] data) {
		TxOutput transactoupt = null;
		byte[] buffer = null;
		int offset = 0;
		if(data !=null) {
			transactoupt = new TxOutput();
			buffer = new byte[8];
			System.arraycopy(data, offset, buffer, 0,buffer.length);
			transactoupt.setValue(Utils.bytesToHex(buffer));
			offset = offset + buffer.length;
			buffer = new byte[1];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			String len = Utils.bytesToHex(buffer);
			long size = Integer.parseInt(len,16);
			Pair<Long, Integer> compactsize = Utils.getCompactSize(size, offset, data);
			size = compactsize.first;
			offset = compactsize.second;
			transactoupt.setPkScriptLength(size);
			buffer = new byte[(int)size];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			transactoupt.setPkScript(Utils.bytesToHex(buffer));
			offset = offset + buffer.length;
			size = data.length - offset;
			buffer = new byte[(int)size];
			System.arraycopy(data, offset, buffer, 0,buffer.length);
		}
		return new Pair<TxOutput, byte[]>(transactoupt, buffer) ;
	}

}
