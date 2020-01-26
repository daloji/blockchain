package com.daloji.blockchain.core;

import java.io.Serializable;

import com.daloji.blockchain.core.commons.Pair;

/**
 *  Transaction input
 * @author daloji
 *
 */
public class TransactionInput implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String hash;
	
	private String index;
	
	private long sciptLeng;
	
	private String signatureScript;
	
	private String sequence;


	

	public long getSciptLeng() {
		return sciptLeng;
	}



	public void setSciptLeng(long sciptLeng) {
		this.sciptLeng = sciptLeng;
	}



	public String getSignatureScript() {
		return signatureScript;
	}



	public void setSignatureScript(String signatureScript) {
		this.signatureScript = signatureScript;
	}



	public String getSequence() {
		return sequence;
	}



	public void setSequence(String sequence) {
		this.sequence = sequence;
	}



	public String getHash() {
		return hash;
	}


	public void setHash(String hash) {
		this.hash = hash;
	}


	public String getIndex() {
		return index;
	}


	public void setIndex(String index) {
		this.index = index;
	}


	public static Pair<TransactionInput,byte[]>  buildTransactionInput(byte[] data) {
		TransactionInput transactinput = null;
		byte[] buffer = null;
		int offset = 0;
		if(data != null) {
			transactinput =  new TransactionInput();
			buffer = new byte[32];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			offset = offset + buffer.length;
			transactinput.setHash(Utils.bytesToHex(buffer));;
			buffer = new byte[4];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			String len = Utils.bytesToHex(buffer);
			len = Utils.StrLittleEndian(len);
			transactinput.setIndex(len);
			offset = offset + buffer.length;
			buffer = new byte[1];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			 len = Utils.bytesToHex(buffer);
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
		return new Pair<TransactionInput, byte[]>(transactinput, buffer);
	}



	@Override
	public String toString() {
		return "TransactionInput [hash=" + hash + ", index=" + index + ", sciptLeng=" + sciptLeng + ", signatureScript="
				+ signatureScript + ", sequence=" + sequence + "]";
	}

}
