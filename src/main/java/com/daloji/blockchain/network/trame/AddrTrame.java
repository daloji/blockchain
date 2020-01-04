package com.daloji.blockchain.network.trame;

import java.time.Instant;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class AddrTrame  extends TrameHeader{


	private static final Logger logger = (Logger) LoggerFactory.getLogger(VersionAckTrame.class);

	private static final String commande="addr";


	private static final int maxAddr = 100;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8871174569589589645L;

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
		String message = generatePayload(network);
		return message;
	}


	

	public String createPayload() {
		String payload ="";
		//nb addr
		payload = payload + Utils.intHexpadding(maxAddr, 0);
		payload = payload +Utils.convertDateString(epoch, 8);
		logger.debug("timestamp  :"+Utils.convertDateString(epoch, 8));
		payload = payload +Utils.intHexpadding(1, 8);
		logger.debug("service  :"+Utils.intHexpadding(1, 8));	
		//adresse IP source
		String ipsource = Utils.convertIPtoHex(getAddressReceive());
		logger.debug("adresseIP source  :"+ipsource);	
		payload = payload + IP_CONST +ipsource;
		payload =payload + Integer.toHexString(port);
		logger.debug("port  :"+Integer.toHexString(port));
		return payload;
	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generatePayload(NetParameters network) {
		logger.debug("construction addr Trame");	
		String message ="";
		message = message + getMagic();
		logger.debug("magic :"+getMagic());
		message = message + Utils.convertStringToHex(commande,12);
		logger.debug("commande :"+Utils.convertStringToHex(commande,12));	
		logger.debug("version  :"+Utils.intHexpadding(1, 8));
		String payload = createPayload();
		int length =payload.length()/2;
		logger.debug("length :"+length);	
		message=message+Utils.intHexpadding(length, 4);
		byte[] payloadbyte =Utils.hexStringToByteArray(payload);
		//4 premier octet seulement pour le chechsum
		byte[] array =Crypto.doubleSha256(payloadbyte);
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		logger.debug("checksum : "+checksum);	
		message = message +checksum;
		//ajout de la payload 
		message = message +payload;
		logger.debug("Addr envoyé : "+message);	
		return message;
	}

}
