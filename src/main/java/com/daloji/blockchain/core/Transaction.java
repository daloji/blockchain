package com.daloji.blockchain.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.daloji.blockchain.core.commons.Pair;

/**
 *  Objet Transaction
 * @author daloji
 *
 */
public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String FLAG = "0001";

	private String version;

	private int flag;

	private long txInCount;

	private List<TransactionInput> txIn;

	private long txOutCount;

	private List<TransactionOutput> txOut;


	private long lockTime;


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public int getFlag() {
		return flag;
	}


	public void setFlag(int flag) {
		this.flag = flag;
	}



	public long getTxInCount() {
		return txInCount;
	}


	public void setTxInCount(long txInCount) {
		this.txInCount = txInCount;
	}


	public List<TransactionInput> getTxIn() {
		return txIn;
	}


	public void setTxIn(List<TransactionInput> txIn) {
		this.txIn = txIn;
	}


	public long getTxOutCount() {
		return txOutCount;
	}


	public void setTxOutCount(long txOutCount) {
		this.txOutCount = txOutCount;
	}


	public List<TransactionOutput> getTxOut() {
		return txOut;
	}


	public void setTxOut(List<TransactionOutput> txOut) {
		this.txOut = txOut;
	}


	public long getLockTime() {
		return lockTime;
	}


	public void setLockTime(long lockTime) {
		this.lockTime = lockTime;
	}


	public static Pair<Transaction,byte[]> buildTransaction(byte[] data) {
		Transaction transaction = null;
		boolean flagPresent = false;
		byte[] buffer = null;
		int offset = 0;
		if(data !=null) {
			transaction =  new Transaction();
			buffer = new byte[4];
			offset = offset + buffer.length;
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			transaction.setVersion(Utils.bytesToHex(buffer));
			buffer = new byte[2];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			if(FLAG.equals(Utils.bytesToHex(buffer))) {
				String flag = Utils.StrLittleEndian(FLAG);
				int flagint =Integer.parseInt(flag,16);
				transaction.setFlag(flagint);
				offset = offset + buffer.length;
				flagPresent = true;
			}
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
			transaction.setTxInCount(size);
			List<TransactionInput> list = new ArrayList<TransactionInput>();
			int sizesplit = data.length - offset;
			buffer = new byte[sizesplit];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			for(int i=0;i<size;i++) {
				Pair<TransactionInput, byte[]> transactionInputRep = TransactionInput.buildTransactionInput(buffer);
				TransactionInput tansactionInput = transactionInputRep._first;
				if(tansactionInput != null) {
					list.add(tansactionInput);
				}
				if(transactionInputRep._second !=null) {
					data = 	transactionInputRep._second;
				}
			}
			transaction.setTxIn(list);
			buffer = new byte[1];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			len = Utils.bytesToHex(buffer);
			size = Integer.parseInt(len,16);
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
			transaction.setTxOutCount(size);
			List<TransactionOutput> listOupt = new ArrayList<TransactionOutput>();
			sizesplit = data.length - buffer.length;
			offset = buffer.length;
			buffer = new byte[sizesplit];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			for(int i=0;i<size;i++) {
				Pair<TransactionOutput, byte[]> transactionoutputRep = TransactionOutput.buildTransactionOutput(buffer);
				TransactionOutput tansactionOutput = transactionoutputRep._first;
				if(tansactionOutput != null) {
					listOupt.add(tansactionOutput);
				}
				if(transactionoutputRep._second !=null) {
					data = 	transactionoutputRep._second;
				}
			}
			transaction.setTxOut(listOupt);
			if(!flagPresent) {
				buffer = new byte[4];
				System.arraycopy(data, 0, buffer, 0, buffer.length);
				offset = buffer.length;
				len = Utils.bytesToHex(buffer);
				len = Utils.StrLittleEndian(len);
				size =Long.parseLong(len,16);
				transaction.setLockTime(size);
			}
			
			size = data.length - offset;
			buffer = new byte[(int)size];
			System.arraycopy(data, offset, buffer, 0,buffer.length);

		}
		return new Pair<Transaction, byte[]>(transaction, buffer);
	}

}
