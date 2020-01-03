package com.daloji.blockchain.network.trame;

import java.time.Instant;
import java.util.Arrays;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

/**
 * @author daloji
 * 
 * Message VerAck venant d'un peer Acquittement de la version
 */
public class VersionAckTrame  extends TrameHeader{

	private static final Logger logger = (Logger) LoggerFactory.getLogger(VersionAckTrame.class);
	
	private static final String commande="verack";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		setMagic(network.getMagic());
		//get Epoch Time
		epoch  = Instant.now().getEpochSecond();
		setAddressTrans(peer.getHost());
		String message =versionTrameAck(network);
		return message;
	}
	
	
	private String versionTrameAck(NetParameters network) {
		logger.debug("construction du Header Verack");	
		String message ="";
		message = message + getMagic();
		logger.debug("magic :"+getMagic());	
		message = message + Utils.convertStringToHex(commande,12);
		logger.debug("commande :"+Utils.convertStringToHex(commande,12));	
	    byte[] payload = new byte[0]; 
		logger.debug("payload :" +payload);	
		byte[] array = Utils.checksum(payload);
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		logger.debug("checksum : "+checksum);	
		message = message + "00000000" +checksum;
		logger.debug("Trame Verack envoyé : "+message);	
		return message;

	}


	@Override
	public Pair<Retour, TrameHeader> receiveMessage(NetParameters network, byte[] msg) {

		VersionAckTrame version = new VersionAckTrame();
		byte[] buffer = new byte[4];
		System.arraycopy(msg, 0, buffer, 0, 4);
		version.setMagic(Utils.bytesToHex(buffer));
		buffer = new byte[12];
		System.arraycopy(msg, 4, buffer, 0, 12);
		version.setCommande(Utils.bytesToHex(buffer));
		buffer = new byte[4];
		System.arraycopy(msg, 16, buffer, 0, 4);
		String hex = Utils.bytesToHex(buffer);
		long length=Utils.little2big(hex);
		version.setLength((int)length);
		return null;
	}


	@Override
	public VersionAckTrame deserialise(byte[] msg) {
		VersionAckTrame version = new VersionAckTrame();
		byte[] buffer = new byte[4];
		int offset = 0;
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		version.setMagic(Utils.bytesToHex(buffer));
		buffer = new byte[12];
		offset = offset + 4;
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		version.setCommande(Utils.bytesToHex(buffer));
		if(COMMANDE_VERACK.equals(Utils.hexToAscii(version.getCommande()))){
			byte[] checksum = new byte[4];
			byte[] checksumCompute = new byte[4];
			offset = offset + 16;
			System.arraycopy(msg, offset, checksum, 0, checksum.length);
			//init buffer zero
			byte [] zero = new byte[0];
			//checksum comput
			byte[] generateZeroChecksum=Utils.checksum(zero);
			System.arraycopy(generateZeroChecksum, 0, checksumCompute, 0, 4);
			if(Arrays.equals(checksumCompute, checksum)) {
				version.setChecksum(Utils.bytesToHex(checksum));
			}else {
				logger.error("checksum invalid" );
				logger.error("cheksum read :  "+Utils.bytesToHex(checksum));
				logger.error("cheksum compute :  "+Utils.bytesToHex(checksumCompute));
				version = null;
			}

		}else {
			version = null;	
		}
		//buffer = new byte[4];

		return version;
	}






}
