package com.daloji.blockchain.network.trame;

import java.time.Instant;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class GetDataTrame extends TrameHeader{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) LoggerFactory.getLogger(GetBlocksTrame.class);

	private static final String commande="getdata";

	private String hash;


	public GetDataTrame(String hash) {
		this.hash = hash;
	}

	@Override
	public String generatePayload(NetParameters network) {
		String payload = "";
		//protocole
		payload = payload +Utils.intHexpadding(protocol, 4);
		// nombre headers
		payload = payload +Utils.intHexpadding(1, 1);
		// hash
		payload = payload +hash;
		return payload;
	}

	@Override
	public <T> T deserialise(byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		setMagic(network.getMagic());
		//get Epoch Time
		epoch  = Instant.now().getEpochSecond();
		setAddressTrans(peer.getHost());
		String message ="";
		message = message +getMagic();
		message = message + Utils.convertStringToHex(commande,12);
		String payload =generatePayload(network);
		//logger.debug("payload :"+payload);
		//lenght
		int length =payload.length()/2;
		message=message+Utils.intHexpadding(length, 4);
		byte[] payloadbyte =Utils.hexStringToByteArray(payload);
		//4 premier octet seulement pour le chechsum
		byte[] array = Crypto.doubleSha256(payloadbyte);
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		message = message +checksum;
		//ajout de la payload 
		message = message +payload;
		return message;

	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	} 

}
