package com.daloji.core.blockchain.peers;

import java.util.Arrays;

import org.slf4j.LoggerFactory;

import com.daloji.core.blockchain.Utils;
import com.daloji.core.blockchain.commons.Pair;
import com.daloji.core.blockchain.commons.Retour;
import com.daloji.core.blockchain.net.NetParameters;
import com.daloji.core.blockchain.net.PeerNode;

import ch.qos.logback.classic.Logger;

/**
 * @author daloji
 * 
 * Message VerAck venant d'un peer Acquittement de la version
 */
public class VersionTrameAck  extends MessageHeader{

	private static final Logger logger = (Logger) LoggerFactory.getLogger(VersionTrameAck.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Pair<Retour, MessageHeader> receiveMessage(NetParameters network, byte[] msg) {

		VersionTrameAck version = new VersionTrameAck();
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
	public VersionTrameAck deserialise(byte[] msg) {
		VersionTrameAck version = new VersionTrameAck();
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
