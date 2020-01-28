package com.daloji.blockchain.network.trame;

import java.io.IOException;
import java.time.Instant;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.Crypto;
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
public class GetBlocksTrame extends TrameHeader{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) LoggerFactory.getLogger(GetBlocksTrame.class);

	private static final String commande="getblocks";

	@Override
	public <T> T deserialise(byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		this.setMagic(network.getMagic());
		//get Epoch Time
		this.epoch  = Instant.now().getEpochSecond();
		this.setAddressTrans(peer.getHost());
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
		this.setChecksum(checksum);
		message = message +checksum;
		//ajout de la payload 
		message = message +payload;
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
		// nombre headers
		payload = payload +Utils.intHexpadding(1, 1);
		// hash
		Block bl = new Block();
		try {
			payload = payload +bl.getHashGenesisBloc();
		} catch (IOException e) {
			payload = null;
		}

		String stop ="00000000000000000000000000000000"+
				     "00000000000000000000000000000000";
		payload = payload + stop;

		return payload;
	}

}
