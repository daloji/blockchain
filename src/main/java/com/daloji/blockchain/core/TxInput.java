package com.daloji.blockchain.core;

import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.utils.Utils;

public class TxInput extends  TransactionInput{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static Pair<TxInput,byte[]>  buildTXInput(byte[] data) {
		TxInput transactinput = null;
		byte[] buffer = null;
		int offset = 0;
		if(data != null) {
			transactinput =  new TxInput();
			buffer = new byte[36];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			offset = offset + buffer.length;
			transactinput.setHash(Utils.bytesToHex(buffer));
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			buffer = new byte[1];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			String len = Utils.bytesToHex(buffer);
			long size = Integer.parseInt(len,16);
			Pair<Long, Integer> compactsize = Utils.getCompactSize(size, offset, data);
			size = compactsize.first;
			offset = compactsize.second;
			transactinput.setSciptLeng(size);
			buffer = new byte[(int)size];
			System.arraycopy(data, offset, buffer, 0,buffer.length);
			offset = offset + buffer.length;
			transactinput.setSignatureScript(Utils.bytesToHex(buffer));
			buffer = new byte[4];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			String sequence = Utils.bytesToHex(buffer);
			sequence = Utils.StrLittleEndian(sequence);
			transactinput.setSequence(sequence);
			offset = offset + buffer.length;
			size = data.length - offset;
			buffer = new byte[(int)size];
			System.arraycopy(data, offset, buffer, 0,buffer.length);

		}
		return new Pair<TxInput, byte[]>(transactinput, buffer);
	}
}
