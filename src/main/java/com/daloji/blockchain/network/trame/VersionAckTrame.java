package com.daloji.blockchain.network.trame;

import java.time.Instant;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.Utils;
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
		String message =generatePayload(network);
		return message;
	}





	@Override
	public String generatePayload(NetParameters network) {
		String message ="";
		message = message + getMagic();
		message = message + Utils.convertStringToHex(commande,12);
		byte[] payload = new byte[0]; 
		byte[] array = Crypto.doubleSha256(payload);
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		message = message + "00000000" +checksum;
		return message;
	}
	/*
	 * (non-Javadoc)
	 * @see com.daloji.blockchain.network.trame.TrameHeader#deserialise(byte[])
	 */

	@Override
	public <T> T deserialise(byte[] msg) {
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
		byte[] info =new byte[offset];
		System.arraycopy(msg,0, info, 0, info.length);
		logger.info("["+getFromPeer().getHost()+"]"+"<IN> Verack "+Utils.bytesToHex(info));
		if(offset<msg.length) {
			buffer = new byte[msg.length-offset];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);

		}else {
			buffer = new byte[0];
		}

		return (T) buffer;
	}

	/*
	 * (non-Javadoc)
	 * @see com.daloji.blockchain.network.trame.TrameHeader#receiveMessage(com.daloji.blockchain.network.NetParameters, byte[])
	 */
	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
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

		return (T) version;
	}

}