package com.daloji.blockchain.network.trame;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

/**
 * Trame de Recuperation d'un ou de plusieurs blocks de la blockchainee a partir d'un Pair (noeud voisin)
 *  si on demarre avec un bockchain vide typiquement lors du demarrage de l'application. en mode full node ou
 *  Noeud complet on recupere toutes la  blockchaine plus de 200 Go de Données soit plus de 500.000 blocks on demarrage 
 *  le telechargement a partir du premier block (Genesis Block)
 *
 * @author daloji
 *
 */
public class GetHeadersTrame extends TrameHeader{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) LoggerFactory.getLogger(GetHeadersTrame.class);

	private static final String commande="getheaders";

	private String version;

	private long hashCount;

	private List<String> listHash;

	private String hashStop;

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
		buffer = new byte[msg.length - offset];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		String payload = Utils.bytesToHex(buffer);
		if(!Utils.allZero(Utils.hexStringToByteArray(payload))){
			buffer = new byte[4];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			this.version = Utils.bytesToHex(buffer);
			offset = offset +buffer.length;
			buffer = new byte[1];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			String len = Utils.bytesToHex(buffer);
			long size = Integer.parseInt(len,16);
			Pair<Long, Integer> compactsize = Utils.getCompactSize(size, offset, msg);
			size = compactsize.first;
			offset = compactsize.second;
			listHash = new ArrayList<String>();
			for(int i=0;i<size;i++) {
				buffer = new byte[32];
				if(offset +buffer.length <= msg.length) {
					System.arraycopy(msg, offset, buffer, 0, buffer.length);
					listHash.add(Utils.bytesToHex(buffer));
					offset = offset + buffer.length;
				}else {
					this.setPartialTrame(true);
					break;
				}
			}

			if(offset +buffer.length <= msg.length) {
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				hashStop =Utils.bytesToHex(buffer);
				offset = offset + buffer.length;
			}
			byte[] info =new byte[offset];
			System.arraycopy(msg,0, info, 0, info.length);
			if(logger.isDebugEnabled()) {
				logger.debug("["+getFromPeer().getHost()+"]"+"<IN> GetHeaders : "+Utils.bytesToHex(info));
			}
			if(offset<msg.length) {
				buffer = new byte[msg.length-offset];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);

			}else {
				buffer = new byte[0];
			}


		}else {
			byte[] info =new byte[offset];
			System.arraycopy(msg,0, info, 0, info.length);
			offset = offset + (int)length;
			if(logger.isDebugEnabled()) {
				logger.debug("["+getFromPeer().getHost()+"]"+"<IN> feelfilter : "+Utils.bytesToHex(info));
			}
			this.setPartialTrame(true);
			buffer = new byte[0];
		}

		return (T) buffer;
	}

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		setMagic(network.getMagic());
		//get Epoch Time
		epoch  = Instant.now().getEpochSecond();
		setAddressTrans(peer.getHost());
		logger.debug("construction du Header GetHeaders");	
		String message ="";
		message = message +getMagic();
		logger.debug("magic :"+getMagic());	
		message = message + Utils.convertStringToHex(commande,12);
		logger.debug("commande :"+Utils.convertStringToHex(commande,12));	
		String payload =generatePayload(network);
		//logger.debug("payload :"+payload);

		//lenght
		int length =payload.length()/2;
		logger.debug("length :"+length);	
		message=message+Utils.intHexpadding(length, 4);
		byte[] payloadbyte =Utils.hexStringToByteArray(payload);
		//4 premier octet seulement pour le chechsum
		byte[] array = Crypto.doubleSha256(payloadbyte);
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		logger.debug("checksum : "+checksum);	
		message = message +checksum;
		//ajout de la payload 
		message = message +payload;
		logger.debug("Message envoyé : "+message);
		return message;
	}





	/*
	 * (non-Javadoc)
	 * @see com.daloji.blockchain.network.trame.TrameHeader#receiveMessage(com.daloji.blockchain.network.NetParameters, byte[])
	 */
	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generatePayload(NetParameters network) {
		String payload = "";
		//protocole
		payload = payload +Utils.intHexpadding(protocol, 4);
		logger.debug("version  :"+Utils.intHexpadding(protocol, 4));	
		// nombre headers
		payload = payload +Utils.intHexpadding(1, 1);
		logger.debug("hash count  :"+Utils.intHexpadding(2, 1));
		// hash
		Block bl = new Block();
		payload = payload +bl.getHashGenesisBloc();
		String stop ="00000000000000000000000000000000"+
				"00000000000000000000000000000000";
		payload = payload + stop;

		return payload;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public long getHashCount() {
		return hashCount;
	}

	public void setHashCount(long hashCount) {
		this.hashCount = hashCount;
	}

	public List<String> getListHash() {
		return listHash;
	}

	public void setListHash(List<String> listHash) {
		this.listHash = listHash;
	}

	public String getHashStop() {
		return hashStop;
	}

	public void setHashStop(String hashStop) {
		this.hashStop = hashStop;
	}

}
