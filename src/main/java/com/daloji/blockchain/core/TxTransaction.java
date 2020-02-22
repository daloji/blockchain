package com.daloji.blockchain.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.utils.Utils;

public class TxTransaction  implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	/**
	 *  Unspnde UTXO
	 */
	private  List<TxInput> txIn = new ArrayList<TxInput>();

	

	private  List<TxOutput> txOut= new ArrayList<TxOutput>();

	private long txOutCount;
	
	private long txInCount;
	
	private  String version;
	
	private  long lockTime;
	

	public String getVersion() {
		return version;
	}
	
	

	public  long getLockTime() {
		return lockTime;
	}



	public  void setLockTime(long lockTime) {
		this.lockTime = lockTime;
	}



	public void setVersion(String version) {
		this.version = version;
	}

	public List<TxInput> getTxIn() {
		return txIn;
	}

	public void setTxIn(List<TxInput> txIn) {
		this.txIn = txIn;
	}

	public List<TxOutput> getTxOut() {
		return txOut;
	}

	public void setTxOut(List<TxOutput> txOut) {
		this.txOut = txOut;
	}

	public long getTxOutCount() {
		return txOutCount;
	}

	public void setTxOutCount(long txOutCount) {
		this.txOutCount = txOutCount;
	}

	public long getTxInCount() {
		return txInCount;
	}

	public void setTxInCount(long txInCount) {
		this.txInCount = txInCount;
	}
	

	public static Pair<TxTransaction,byte[]> buildTxTransaction(byte[] data) {
		TxTransaction transaction = new TxTransaction();
		byte[] buffer = null;
		int offset = 0;
		if(data !=null) {
			buffer = new byte[1];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			String len = Utils.bytesToHex(buffer);
			long sizeinput = Integer.parseInt(len,16);
			Pair<Long, Integer> compactsize = Utils.getCompactSize(sizeinput, offset, data);
			sizeinput = compactsize.first;
			offset = compactsize.second;
			buffer = new byte[data.length-offset];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			transaction.setTxInCount(sizeinput);
			List<TxInput> listInput = new ArrayList<TxInput>();
			for(int i=0;i<sizeinput;i++) {
				Pair<TxInput,byte[]> txInputBuild = TxInput.buildTXInput(buffer);
				TxInput tansactionInput = txInputBuild.first;
				if(tansactionInput != null) {
					listInput.add(tansactionInput);
				}
				if(txInputBuild.second !=null) {
					data = 	txInputBuild.second;
				}

			}
			transaction.setTxIn(listInput);
			offset = 0;
			buffer = new byte[1];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			len = Utils.bytesToHex(buffer);
			long sizeoutput = Integer.parseInt(len,16);
			compactsize = Utils.getCompactSize(sizeoutput, offset, data);
			sizeoutput = compactsize.first;
			offset = compactsize.second;
			buffer = new byte[data.length-offset];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			transaction.setTxOutCount(sizeoutput);
			List<TxOutput> listout = new ArrayList<TxOutput>();
 			for(int i=0;i<sizeoutput;i++) {
				Pair<TxOutput, byte[]> transactionInputRep = TxOutput.buildTxOutput(buffer);
				TxOutput tansactionInput = transactionInputRep.first;
				if(tansactionInput != null) {
					listout.add(tansactionInput);
				}
				if(transactionInputRep.second !=null) {
					data = 	transactionInputRep.second;
					buffer = data;
				}
			}
			System.out.println(Utils.bytesToHex(data));
			transaction.setTxOut(listout);
			offset = 0;
			buffer = new byte[4];
			System.arraycopy(data,offset, buffer, 0, buffer.length);
			len = Utils.bytesToHex(buffer);
			len = Utils.StrLittleEndian(len);
			long lock = Integer.parseInt(len,16);
			transaction.setLockTime(lock);
			offset = offset + buffer.length;
			buffer = new byte[data.length - offset];
			System.arraycopy(data,offset, buffer, 0, buffer.length);
			
		}
		return new Pair<TxTransaction, byte[]>(transaction, buffer);
	}
}
