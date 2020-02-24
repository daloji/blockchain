package com.daloji.blockchain.network.trame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.TxTransaction;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

public class TxTrame extends TrameHeader{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(TxTrame.class);

	private static final String commande = "tx";

	private String version;

	private TxTransaction transaction;

	@Override
	public String generatePayload(NetParameters network) {
		String info = "";
		info = info + version;
		info = info + Utils.intHexpadding((int)transaction.getTxInCount(),1);
		for(int i=0;i<transaction.getTxInCount();i++) {
			info = info +transaction.getTxIn().get(i).getHash();
			info = info + Utils.intHexpadding((int)transaction.getTxIn().get(i).getSciptLeng(),1);
			info = info + transaction.getTxIn().get(i).getSignatureScript();
			info = info + transaction.getTxIn().get(i).getSequence();
		}
		info = info + Utils.intHexpadding((int)transaction.getTxOutCount(),1);
		for(int i=0;i<transaction.getTxOutCount();i++) {
			info = info +transaction.getTxOut().get(i).getValue();
			info = info + Utils.intHexpadding((int)transaction.getTxOut().get(i).getPkScriptLength(),1);
			info = info + transaction.getTxOut().get(i).getPkScript();
		}
		info = info +Utils.intHexpadding((int)transaction.getLockTime(), 4);
		return info;
	}

	@Override
	public <T> T deserialise(byte[] msg) {
		byte[] buffer = new byte[4];
		int offset = 0;
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setMagic(Utils.bytesToHex(buffer));
		offset = offset + buffer.length;	
		buffer = new byte[12];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setCommande(Utils.bytesToHex(buffer));
		offset = offset + buffer.length;
		buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		String hex = Utils.bytesToHex(buffer);
		long length=Utils.little2big(hex);
		this.setLength((int)length);
		offset = offset + buffer.length;
		buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setChecksum(Utils.bytesToHex(buffer));
		offset = offset + buffer.length;
		if(length <msg.length) {
			byte[] info = new byte[4];
			System.arraycopy(msg, offset, info, 0, info.length);
			String magic = Utils.bytesToHex(info);
			if(!isMagicOK(NetParameters.MainNet, magic)) {
				buffer = new byte[4];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				version = Utils.bytesToHex(buffer);
				long ver=Utils.little2big(version);
				//version transaction doit etre 1 ou 2
				if(ver>=1 && ver<=2) {
					offset = offset + buffer.length;
					buffer = new byte[msg.length -offset];
					System.arraycopy(msg, offset, buffer, 0,buffer.length);
					Pair<TxTransaction,byte[]> transactionBuild = TxTransaction.buildTxTransaction(buffer);
					transaction = transactionBuild.first;
					buffer = transactionBuild.second;

					if(logger.isDebugEnabled()) {
						String extractZero =Utils.bytesToHex(msg);
						extractZero = Utils.deleteEndZero(extractZero);
						logger.debug("["+getFromPeer().getHost()+"]"+"<IN> TX   "+extractZero);
					}

				}else {
					logger.error("Rejet de la transaction version non conforme :" +ver);
					info =new byte[msg.length -(int)length];
					System.arraycopy(msg,offset, info, 0, info.length);
					buffer=info;
				}
			}else {
				info =new byte[msg.length -offset];
				System.arraycopy(msg,offset, info, 0, info.length);
				buffer=info;	
			}

		}else {
			byte[] info =new byte[msg.length -offset];
			System.arraycopy(msg,offset, info, 0, info.length);
			buffer=info;	
		}

		return (T) buffer;
	}

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		setMagic(network.getMagic());
		String message ="";
		message = message +getMagic();
		message = message + Utils.convertStringToHex(commande,12);
		String payload = generatePayload(network);
		int length = payload.length()/2;
		message= message +Utils.intHexpadding(length, 4);
		byte[] payloadbyte = Utils.hexStringToByteArray(payload);
		byte[] array = Crypto.doubleSha256(payloadbyte);
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		message = message +checksum;
		message = message +payload;
		return message;
	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	public TxTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(TxTransaction transaction) {
		this.transaction = transaction;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	public String generateHash() {
		String info = "";
		info = info + version;
		info = info + Utils.intHexpadding((int)transaction.getTxInCount(),1);
		for(int i=0;i<transaction.getTxInCount();i++) {
			info = info +transaction.getTxIn().get(i).getHash();
			info = info + Utils.intHexpadding((int)transaction.getTxIn().get(i).getSciptLeng(),1);
			info = info + transaction.getTxIn().get(i).getSignatureScript();
			info = info + transaction.getTxIn().get(i).getSequence();
		}
		info = info + Utils.intHexpadding((int)transaction.getTxOutCount(),1);
		for(int i=0;i<transaction.getTxOutCount();i++) {
			info = info +transaction.getTxOut().get(i).getValue();
			info = info + Utils.intHexpadding((int)transaction.getTxOut().get(i).getPkScriptLength(),1);
			info = info + transaction.getTxOut().get(i).getPkScript();
		}
		info = info +Utils.intHexpadding((int)transaction.getLockTime(), 4);
		byte[] hashbyte = Crypto.doubleSha256(Utils.hexStringToByteArray(info));	 
		return Utils.bytesToHex(hashbyte);
	}

}
