package com.daloji.blockchain.network.trame;

import java.util.ArrayList;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Addr;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class BlockTrame  extends TrameHeader{

	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(BlockTrame.class);

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
			byte[] info =new byte[offset];
			System.arraycopy(msg,0, info, 0, info.length);
			logger.info("["+getFromPeer().getHost()+"]"+"<IN> Block : "+Utils.bytesToHex(info));
			if(offset<msg.length) {
				buffer = new byte[msg.length-offset];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);

			}else {
				buffer = new byte[0];
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

}
