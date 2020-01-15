package com.daloji.blockchain.network.trame;

import java.time.Instant;
import java.util.Arrays;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;


/**
 * @author daloji
 * 
 * Message Version envoyé a un Noeud (node) au demarage du service
 */
public class VersionTrameMessage  extends TrameHeader{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static final Logger logger = (Logger) LoggerFactory.getLogger(VersionTrameMessage.class);


	/**
	 *  User Agent (/daloji:0.0.1/)
	 */
	private static final String sub_version ="/daloji:0.0.1/";

	/** commande **/
	private static final String commande="version";

	private static final int protocol= 70015;


	private String userAgent;

	private long version;

	private String service;


	private String nonce;

	private int portReceive;

	private int portTrans;


	private int startHeigth;


	private boolean relay;


	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public boolean isRelay() {
		return relay;
	}

	public void setRelay(boolean relay) {
		this.relay = relay;
	}

	public int getStartHeigth() {
		return startHeigth;
	}

	public void setStartHeigth(int startHeigth) {
		this.startHeigth = startHeigth;
	}

	public int getPortTrans() {
		return portTrans;
	}

	public void setPortTrans(int portTrans) {
		this.portTrans = portTrans;
	}

	public int getPortReceive() {
		return portReceive;
	}

	public void setPortReceive(int portReceive) {
		this.portReceive = portReceive;
	}

	public VersionTrameMessage(boolean getIp) {
		if(getIp) {
			getExternalIp();
		}
	}



	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}


	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public static String getSubVersion() {
		return sub_version;
	}

	/*
	 * (non-Javadoc)
	 * @see com.daloji.blockchain.network.trame.TrameHeader#generateMessage(com.daloji.blockchain.network.NetParameters, com.daloji.blockchain.network.peers.PeerNode)
	 */
	@Override
	public String generateMessage(NetParameters network,PeerNode peer) {
		setMagic(network.getMagic());
		//get Epoch Time
		epoch  = Instant.now().getEpochSecond();
		setAddressTrans(peer.getHost());
		String message = versionMessageHeader(network);
		return message;
	}



	private String versionMessageHeader(NetParameters network) {
		String message ="";
		message = message +getMagic();
		message = message+Utils.convertStringToHex(commande,12);
		String payload = generatePayload(network);
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



	/*
	 * (non-Javadoc)
	 * @see com.daloji.blockchain.network.trame.TrameHeader#deserialise(byte[])
	 */
	/*
	@Override
	public <T> T deserialise(byte[] msg) {
		VersionTrameReceive versionReceive = new VersionTrameReceive();
		VersionTrameMessage version = new VersionTrameMessage(false);
		int offset =0;
		byte[] buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		version.setMagic(Utils.bytesToHex(buffer));
		buffer = new byte[12];
		offset = offset +4;
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		version.setCommande(Utils.bytesToHex(buffer));
		offset = offset +buffer.length;
		buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, 4);
		String hex = Utils.bytesToHex(buffer);
		long length=Utils.little2big(hex);
		version.setLength((int)length);
		buffer = new byte[(int)length];
		System.arraycopy(msg, offset+8, buffer, 0,(int) length);
		String payload = Utils.bytesToHex(buffer);
		if(!Utils.allZero(Utils.hexStringToByteArray(payload))){
			buffer = new byte[4];
			offset = offset +buffer.length;
			System.arraycopy(msg, offset, buffer, 0, 4);
			version.setChecksum(Utils.bytesToHex(buffer));
			offset = offset +buffer.length;;
			System.arraycopy(msg, offset, buffer, 0, 4);
			hex = Utils.bytesToHex(buffer);
			this.version = Utils.little2big(hex);
			offset = offset +buffer.length;
			buffer = new byte[8];
			System.arraycopy(msg, offset, buffer, 0, 8);
			this.service = Utils.bytesToHex(buffer);
			buffer = new byte[4];
			System.arraycopy(msg,36, buffer, 0, 4);
			String time = Utils.bytesToHex(buffer);
			long epoch = Utils.little2big(time);
			this.epoch = epoch;
			buffer = new byte[16];
			System.arraycopy(msg,52, buffer, 0, 16);
			this.setAddressReceive(Utils.bytesToHex(buffer));
			buffer = new byte[2];
			System.arraycopy(msg,68, buffer, 0, 2);
			String strport = Utils.bytesToHex(buffer);
			long port = Long.parseLong(strport,16);
			this.portReceive = (int)port;
			buffer = new byte[16];
			System.arraycopy(msg,78, buffer, 0, 16);
			this.setAddressTrans(Utils.bytesToHex(buffer));
			buffer = new byte[2];
			System.arraycopy(msg,94, buffer, 0, 2);
			strport = Utils.bytesToHex(buffer);
			port = Long.parseLong(strport,16); 
			this.portTrans=(int)port;
			buffer = new byte[8];
			System.arraycopy(msg,96, buffer, 0, 8);
			this.nonce=Utils.bytesToHex(buffer);
			buffer = new byte[1];
			System.arraycopy(msg,104, buffer, 0, 1);
			String sizeUserAgentStr = Utils.bytesToHex(buffer);
			long sizeUserAgent = Long.parseLong(sizeUserAgentStr,16);
			buffer = new byte[(int)sizeUserAgent];
			System.arraycopy(msg,105, buffer, 0,(int) sizeUserAgent);
			String useragent = Utils.bytesToHex(buffer);
			this.userAgent=Utils.hexToAscii(useragent);
			buffer = new byte[4];
			offset =(int) (105+sizeUserAgent);
			System.arraycopy(msg,offset, buffer, 0, 4);
			String startheigthStr = Utils.bytesToHex(buffer);
			long startheigth = Long.parseLong(startheigthStr,16);
			this.startHeigth = (int)startheigth;
			offset =offset+4;
			if(msg.length>offset) {
				offset =offset+1;
				if(msg[offset]>0){
					this.relay = true;
				}
			}

			versionReceive.setVersion(version);
			//fin du block version selon le protocole 
			/**
	 * le contenu de l'objet version est definie dans 
	 * See <a href="https://bitcoin.org/en/developer-reference#version"</a>.
	 *
			if(msg.length-offset>0) {
				VersionAckTrame verak = new VersionAckTrame();
				buffer = new byte[msg.length-offset];
				System.arraycopy(msg,offset, buffer, 0, buffer.length);
				VersionAckTrame deserialiseVerak = verak.deserialise(buffer);
				versionReceive.setVersionAck(deserialiseVerak);

			}
		}else {
			this.setPartialTrame(true);

		}


		return (T) this;
	}*/

	/**
	 * Dans certain cas un noeud peut renvoyer sa Trame version en 2 parties
	 * @return true si on est dans ce cas
	 */
	private boolean checkifPartialData(final byte[] data) {
		String checksumPartial = "2E694B3F";
		boolean value =false;
		if(data.length>16) {
			byte[] lengthArray = new byte[4];
			System.arraycopy(data, 16, lengthArray, 0, 4);
			String strlength = Utils.bytesToHex(lengthArray);
			long length = Utils.little2big(strlength);
			byte[] checksum = new byte[4];
			System.arraycopy(data, 20, checksum, 0, 4); 
			//payload
			byte[] payload = new byte[(int)length];
			System.arraycopy(data, 24, payload, 0,(int) length);
			//checksum compute
			byte[] checksumpayload = Crypto.doubleSha256(payload);
			//first 4 byte
			byte[] checksumcompute = new byte[4];
			System.arraycopy(checksumpayload, 0, checksumcompute, 0, 4);
			if(Arrays.equals(checksumcompute, Utils.hexStringToByteArray(checksumPartial))) {
				value = true;	
			}
		}
		return value;

	}

	/*
	 * (non-Javadoc)
	 * @see com.daloji.blockchain.network.trame.TrameHeader#receiveMessage(com.daloji.blockchain.network.NetParameters, byte[])
	 */
	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		Retour retour = Utils.createRetourOK();
		TrameHeader message = null;
		VersionTrameReceive deserialise = null;
		if(!isValidMessage(network, msg)) {
			//verifier si la trame est envoyé en deux fois
			if(checkifPartialData(msg)) {
				VersionTrameMessage versionTrame = getVersionTrameHeader(msg);
				versionTrame.setPartialTrame(true);
				deserialise = new VersionTrameReceive();
				deserialise.setVersion(versionTrame);
			}
		}else {
			message = getTypeMessage(msg);
			if(message instanceof VersionTrameMessage) {
				deserialise = deserialise(msg);
			}
		}
		return (T) deserialise;

	}

	/**
	 *  recuperation entete Trame version
	 * @param data
	 * @return
	 */
	public VersionTrameMessage getVersionTrameHeader(final byte[] data) {
		VersionTrameMessage version = new VersionTrameMessage(true);
		int offset =0;
		byte[] buffer = new byte[4];
		System.arraycopy(data, offset, buffer, 0, buffer.length);
		version.setMagic(Utils.bytesToHex(buffer));
		buffer = new byte[12];
		offset = offset +4;
		System.arraycopy(data, offset, buffer, 0, buffer.length);
		version.setCommande(Utils.bytesToHex(buffer));
		offset = offset +buffer.length;
		buffer = new byte[4];
		System.arraycopy(data, offset, buffer, 0, 4);
		String hex = Utils.bytesToHex(buffer);
		long length=Utils.little2big(hex);
		version.setLength((int)length);
		offset = offset +buffer.length;
		System.arraycopy(data, offset, buffer, 0, 4);
		version.setChecksum(Utils.bytesToHex(buffer));
		return version;

	}

	@Override
	public String toString() {
		return "VersionTrameMessage [userAgent=" + userAgent + ", version=" + version + ", service=" + service
				+ ", nonce=" + nonce + ", portReceive=" + portReceive + ", portTrans=" + portTrans + ", startHeigth="
				+ startHeigth + ", relay=" + relay + "]";
	}
	/*
	 * (non-Javadoc)
	 * @see com.daloji.blockchain.network.trame.TrameHeader#generatePayload(com.daloji.blockchain.network.NetParameters)
	 */
	@Override
	public String generatePayload(NetParameters network) {
		String payload ="";
		payload = payload +Utils.intHexpadding(protocol,4);
		//service 1
		payload = payload +Utils.intHexpadding(1, 8);
		// time
		payload = payload +Utils.convertDateString(epoch, 8);
		//version
		payload = payload +Utils.intHexpadding(1, 8);
		//adresse IP destination
		String ipdest = Utils.convertIPtoHex(getAddressTrans());
		payload = payload + IP_CONST +ipdest;
		payload =payload + Integer.toHexString(port);
		//version
		payload = payload +Utils.intHexpadding(1, 8);
		//adresse IP source
		String ipsource = Utils.convertIPtoHex(getAddressReceive());
		payload = payload + IP_CONST +ipsource;
		payload =payload + Integer.toHexString(port);
		//nonce
		String nonce =Utils.generateNonce(16);
		payload =payload + nonce;
		//sub version protocole
		String subversion = Utils.convertStringToHex(sub_version, 15);
		payload = payload + "0f"+subversion ;
		//start_height
		payload = payload +Utils.intHexpadding(0,4);	
		//payload = payload +Utils.intHexpadding(0,1);
		return payload;
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
		buffer = new byte[(int)length];
		System.arraycopy(msg, offset+4, buffer, 0, buffer.length);
		String payload = Utils.bytesToHex(buffer);
		if(!Utils.allZero(Utils.hexStringToByteArray(payload))){
			buffer = new byte[4];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			this.setChecksum(Utils.bytesToHex(buffer));
			offset = offset +buffer.length;
			buffer = new byte[4];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			hex = Utils.bytesToHex(buffer);
			this.version = Utils.little2big(hex);
			offset = offset +buffer.length;
			buffer = new byte[8];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			this.service = Utils.bytesToHex(buffer);
			offset = offset +buffer.length;
			buffer = new byte[4];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			String time = Utils.bytesToHex(buffer);
			long epoch = Utils.little2big(time);
			this.epoch = epoch;
			offset = offset +buffer.length;
			offset = offset +buffer.length;
			buffer = new byte[24];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			this.setAddressReceive(Utils.bytesToHex(buffer));
			offset = offset +buffer.length;
			buffer = new byte[2];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			String strport = Utils.bytesToHex(buffer);
			long port = Long.parseLong(strport,16);
			this.portReceive = (int)port;
			offset = offset +buffer.length;
			buffer = new byte[24];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			this.setAddressTrans(Utils.bytesToHex(buffer));
			offset = offset +buffer.length;
			buffer = new byte[2];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			strport = Utils.bytesToHex(buffer);
			port = Long.parseLong(strport,16); 
			this.portTrans=(int)port;
			offset = offset +buffer.length;
			buffer = new byte[8];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			this.nonce=Utils.bytesToHex(buffer);
			offset = offset +buffer.length;
			buffer = new byte[1];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			String sizeUserAgentStr = Utils.bytesToHex(buffer);
			long sizeUserAgent = Long.parseLong(sizeUserAgentStr,16);
			offset = offset +buffer.length;
			buffer = new byte[(int)sizeUserAgent];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			String useragent = Utils.bytesToHex(buffer);
			this.userAgent=Utils.hexToAscii(useragent);
			offset = offset +buffer.length;
			buffer = new byte[4];
			//offset =offset + (int) sizeUserAgent;
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
			String startheigthStr = Utils.bytesToHex(buffer);
			long startheigth = Long.parseLong(startheigthStr,16);
			this.startHeigth = (int)startheigth;
			offset = offset +buffer.length;
			if(msg.length>offset) {
				if(msg[offset]>0){
					this.relay = true;
				}
				offset = offset + 1;

			}
			byte[] info =new byte[offset];
			System.arraycopy(msg,0, info, 0, info.length);
			logger.info("["+getFromPeer().getHost()+"]"+"<IN> Version  "+Utils.bytesToHex(info));

			buffer = new byte[msg.length-offset];
			System.arraycopy(msg,offset, buffer, 0, buffer.length);
			
		}else {
			this.setPartialTrame(true);
			buffer = new byte[0];
		}


		return (T) buffer;
	}


}



