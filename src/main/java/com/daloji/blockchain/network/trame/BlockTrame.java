package com.daloji.blockchain.network.trame;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Transaction;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class BlockTrame  extends TrameHeader{


	private static final Logger logger = (Logger) LoggerFactory.getLogger(BlockTrame.class);

	/* taille de block 82 octet **/
	private static final int SIZE_BLOCK = 82;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5410184196898052322L;


	private String version;

	private String previousHash;

	private String merkelRoot;

	private long time;

	private long nBits;

	private long nonce;

	private List<Transaction> listTransacation;


	@Override
	public String generatePayload(NetParameters network) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T deserialise(byte[] msg) {
		int offset =0;
		byte[] buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setMagic(Utils.bytesToHex(buffer));
		offset = offset +  buffer.length;
		buffer = new byte[12];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setCommande(Utils.bytesToHex(buffer));
		offset = offset +buffer.length;
		buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		String hex = Utils.bytesToHex(buffer);
		long length = Utils.little2big(hex);
		this.setLength((int)length);
		offset = offset +buffer.length;
		buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setChecksum(Utils.bytesToHex(buffer));
		offset = offset +buffer.length;
		buffer = new byte[(int)length];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		String payload = Utils.bytesToHex(buffer);
		//TODO protection taille de buffer
		if(!Utils.allZero(Utils.hexStringToByteArray(payload))){
			if((buffer.length+offset)<msg.length) {
				buffer = new byte[4];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				String version = Utils.bytesToHex(buffer);
				this.version = version;
				offset = offset +buffer.length;
				buffer = new byte[32];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				this.previousHash = Utils.bytesToHex(buffer);
				offset = offset +buffer.length;
				buffer = new byte[32];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				this.merkelRoot = Utils.bytesToHex(buffer);
				offset = offset +buffer.length;
				buffer = new byte[4];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				String epoch = Utils.bytesToHex(buffer);
				this.time = Utils.little2big(epoch);
				offset = offset +buffer.length;
				buffer = new byte[4];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				String nbits = Utils.bytesToHex(buffer);
				this.nBits = Utils.little2big(nbits);
				offset = offset +buffer.length;
				buffer = new byte[4];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				String nonce = Utils.bytesToHex(buffer);
				this.nonce = Utils.little2big(nonce);
				offset = offset +buffer.length;
				buffer = new byte[1];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				String len = Utils.bytesToHex(buffer);
				long size = Integer.parseInt(len,16);
				if(size<Utils.FD_HEXA) {
					buffer = new byte[1];
					offset = offset + buffer.length;				
				}else if(size>=Utils.FD_HEXA && size<=Utils.FFFF_HEXA) {
					buffer = new byte[2];
					offset = offset + 1;
					System.arraycopy(msg, offset, buffer, 0,buffer.length);
					len = Utils.bytesToHex(buffer);
					len = Utils.StrLittleEndian(len);
					size =Integer.parseInt(len,16);
					offset = offset + buffer.length;	
				}else if(size>Utils.FFFF_HEXA) {
					buffer = new byte[4];
					offset = offset + 1;
					System.arraycopy(msg, offset, buffer, 0,buffer.length);
					len = Utils.bytesToHex(buffer);
					len = Utils.StrLittleEndian(len);
					size =Integer.parseInt(len,16);
					offset = offset + buffer.length;
				}
				listTransacation =  new ArrayList<Transaction>();
				int sizesplit = msg.length - offset;
				buffer = new byte[sizesplit];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				for(int i=0;i<size;i++) {
					
					Pair<Transaction,byte[]> transactionBuild = Transaction.buildTransaction(buffer);
					if(transactionBuild != null) {
						listTransacation.add(transactionBuild._first);	
					}
					if(transactionBuild._second != null) {
						buffer = transactionBuild._second;
					}
					
				}
				
				byte[] info =new byte[offset];
				System.arraycopy(msg,0, info, 0, info.length);
				logger.info("["+getFromPeer().getHost()+"]"+"<IN> Block : " +this.getMerkelRoot());
			}else {
				
			}

		}else {
			this.setPartialTrame(true);
			buffer = new byte[0];
		}
		return (T) buffer;
	}



	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public String getMerkelRoot() {
		return merkelRoot;
	}

	public void setMerkelRoot(String merkelRoot) {
		this.merkelRoot = merkelRoot;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getnBits() {
		return nBits;
	}

	public void setnBits(long nBits) {
		this.nBits = nBits;
	}

	public long getNonce() {
		return nonce;
	}

	public void setNonce(long nonce) {
		this.nonce = nonce;
	}

	public List<Transaction> getListTransacation() {
		return listTransacation;
	}

	public void setListTransacation(List<Transaction> listTransacation) {
		this.listTransacation = listTransacation;
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

	@Override
	public String toString() {
		return "BlockTrame [version=" + version + ", previousHash=" + previousHash + ", merkelRoot=" + merkelRoot
				+ ", time=" + time + ", nBits=" + nBits + ", nonce=" + nonce + ", listTransacation=" + listTransacation
				+ "]";
	}

}
