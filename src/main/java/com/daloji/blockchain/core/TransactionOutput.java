 package com.daloji.blockchain.core;

import java.io.Serializable;

import com.daloji.blockchain.core.commons.Pair;

/**
 *  Transaction output
 * @author daloji
 *
 */
public class TransactionOutput implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String value;
	
	private long pkScriptLength;
	
	private String pkScript;

	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	

	public long getPkScriptLength() {
		return pkScriptLength;
	}

	public void setPkScriptLength(long pkScriptLength) {
		this.pkScriptLength = pkScriptLength;
	}

	public String getPkScript() {
		return pkScript;
	}

	public void setPkScript(String pkScript) {
		this.pkScript = pkScript;
	}

	public static Pair<TransactionOutput,byte[]> buildTransactionOutput(byte[] data) {
		TransactionOutput transactoupt = null;
		byte[] buffer = null;
		int offset = 0;
		if(data !=null) {
			transactoupt = new TransactionOutput();
			buffer = new byte[8];
			System.arraycopy(data, offset, buffer, 0,buffer.length);
			transactoupt.setValue(Utils.bytesToHex(buffer));
			offset = offset + buffer.length;
			buffer = new byte[1];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			String len = Utils.bytesToHex(buffer);
			long size = Integer.parseInt(len,16);
			if(size<Utils.FD_HEXA) {
				buffer = new byte[1];
				offset = offset + buffer.length;				
			}else if(size>=Utils.FD_HEXA && size<=Utils.FFFF_HEXA) {
				buffer = new byte[2];
				offset = offset + 1;
				System.arraycopy(data, offset, buffer, 0,buffer.length);
				len = Utils.bytesToHex(buffer);
				len = Utils.StrLittleEndian(len);
				size =Integer.parseInt(len,16);
				offset = offset + buffer.length;	
			}else if(size>Utils.FFFF_HEXA) {
				buffer = new byte[4];
				offset = offset + 1;
				System.arraycopy(data, offset, buffer, 0,buffer.length);
				len = Utils.bytesToHex(buffer);
				len = Utils.StrLittleEndian(len);
				size =Integer.parseInt(len,16);
				offset = offset + buffer.length;
			}
			transactoupt.setPkScriptLength(size);
			offset = offset + buffer.length;
			buffer = new byte[(int)size];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			transactoupt.setPkScript(Utils.bytesToHex(buffer));
			offset = offset + buffer.length;
			size = data.length - offset;
			buffer = new byte[(int)size];
			System.arraycopy(data, offset, buffer, 0,buffer.length);
		}
		return new Pair<TransactionOutput, byte[]>(transactoupt, buffer) ;
	}
	
}
