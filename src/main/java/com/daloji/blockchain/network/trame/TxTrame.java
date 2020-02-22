package com.daloji.blockchain.network.trame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T deserialise(byte[] msg) {
		logger.info("TX =>" + Utils.bytesToHex(msg));
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
				offset = offset + buffer.length;
				buffer = new byte[msg.length -offset];
				System.arraycopy(msg, offset, buffer, 0,buffer.length);
				Pair<TxTransaction,byte[]> transactionBuild = TxTransaction.buildTxTransaction(buffer);
				transaction = transactionBuild.first;
				buffer = transactionBuild.second;
								
			}

			info =new byte[msg.length -buffer.length];
			System.arraycopy(msg,0, info, 0, info.length);
			if(logger.isDebugEnabled()) {
				String extractZero =Utils.bytesToHex(info);
				extractZero = Utils.deleteEndZero(extractZero);
				logger.debug("["+getFromPeer().getHost()+"]"+"<IN> TX   "+extractZero);
			}
		}
		return (T) buffer;
	}

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		// TODO Auto-generated method stub
		return null;
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


}
