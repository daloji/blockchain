package com.daloji.blockchain.network.trame;

import java.io.IOException;
import java.time.Instant;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.Utils;
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

	private static final Logger logger = (Logger) LoggerFactory.getLogger(VersionAckTrame.class);

	private static final String commande="getheaders";

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
