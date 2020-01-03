package com.daloji.blockchain.network.trame;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Messages;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.core.commons.Pair;
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


	private static final Logger logger = (Logger) LoggerFactory.getLogger(NetworkMessagingProxy.class);


	/**
	 *  User Agent (/daloji:0.0.1/)
	 */
	private static final String sub_version ="/daloji:0.0.1/";

	private static final String IP_CONST ="00000000000000000000ffff";

	private static final String commande="version";

	private static final int port=8333;

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
	public VersionTrameMessage() {
		super();
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

	@Override
	public String generateMessage(NetParameters network,PeerNode peer) {
		setMagic(network.getMagic());
		//get Epoch Time
		epoch  = Instant.now().getEpochSecond();
		setAddressTrans(peer.getHost());
		String message =versionMessageHeader(network);
		return message;
	}



	private String versionMessageHeader(NetParameters network) {
		logger.debug("construction du Header Version");	
		String message ="";
		message = message +getMagic();
		logger.debug("magic :"+getMagic());	
		message = message+Utils.convertStringToHex(commande,12);
		logger.debug("commande :"+Utils.convertStringToHex(commande,12));	
		String payload =generatePayload();
		logger.debug("payload :"+payload);	
		//lenght
		int length =payload.length()/2;
		logger.debug("length :"+length);	
		message=message+Utils.intHexpadding(length, 4);
		byte[] payloadbyte =Utils.hexStringToByteArray(payload);
		//4 premier octet seulement pour le chechsum
		byte[] array = Utils.checksum(payloadbyte);
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		logger.debug("checksum : "+checksum);	
		message = message +checksum;
		//ajout de la payload 
		message = message +payload;
		logger.debug("Message envoyé : "+message);	

		return message;
	}


	private String generatePayload() {
		String payload ="";
		logger.debug("payload :");	
		payload = payload +Utils.intHexpadding(protocol,4);
		logger.debug("version protocol :"+Utils.intHexpadding(protocol,4));	
		//service 1
		payload = payload +Utils.intHexpadding(1, 8);
		logger.debug("service  :"+Utils.intHexpadding(1, 8));	
		// time
		payload = payload +Utils.convertDateString(epoch, 8);
		logger.debug("timestamp  :"+Utils.convertDateString(epoch, 8));	
		//version
		payload = payload +Utils.intHexpadding(1, 8);
		//adresse IP destination
		String ipdest = Utils.convertIPtoHex(getAddressTrans());
		logger.debug("adresseIP destination  :"+Utils.convertIPtoHex(getAddressTrans()));	
		payload = payload + IP_CONST +ipdest;
		payload =payload + Integer.toHexString(port);
		logger.debug("port  :"+Integer.toHexString(port));	
		//version
		payload = payload +Utils.intHexpadding(1, 8);
		logger.debug("version  :"+Utils.intHexpadding(1, 8));
		//adresse IP source
		String ipsource = Utils.convertIPtoHex(getAddressReceive());
		logger.debug("adresseIP source  :"+ipsource);	

		payload = payload + IP_CONST +ipsource;
		payload =payload + Integer.toHexString(port);
		logger.debug("port  :"+Integer.toHexString(port));	
		//nonce
		String nonce =Utils.generateNonce(16);
		payload =payload + nonce;
		logger.debug("nonce  :"+nonce);	

		//sub version protocole
		String subversion = Utils.convertStringToHex(sub_version, 15);
		logger.debug("sub version   :"+subversion);	
		payload = payload + "0f"+subversion ;

		//start_height
		logger.debug("start height :"+Utils.intHexpadding(0,4));	
		payload = payload +Utils.intHexpadding(0,4);
		
		//relay
		logger.debug("relay :"+Utils.intHexpadding(0,1));	
	   //payload = payload +Utils.intHexpadding(0,1);
		return payload;

	}




	@Override
	public <T> T deserialise(byte[] msg) {
		VersionTrameReceive versionReceive = new VersionTrameReceive();
		VersionTrameMessage version = new VersionTrameMessage();
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
		offset = offset +buffer.length;
		System.arraycopy(msg, offset, buffer, 0, 4);
		version.setChecksum(Utils.bytesToHex(buffer));
		offset = offset +buffer.length;;
		System.arraycopy(msg, offset, buffer, 0, 4);
		hex = Utils.bytesToHex(buffer);
		long versionprotocol=Utils.little2big(hex);
		version.setVersion(versionprotocol);
		offset = offset +buffer.length;
		buffer = new byte[8];
		System.arraycopy(msg, offset, buffer, 0, 8);
		version.setService(Utils.bytesToHex(buffer));
		buffer = new byte[4];
		System.arraycopy(msg,36, buffer, 0, 4);
		String time = Utils.bytesToHex(buffer);
		long epoch = Utils.little2big(time);
		version.setEpoch(epoch);
		buffer = new byte[16];
		System.arraycopy(msg,52, buffer, 0, 16);
		version.setAddressReceive(Utils.bytesToHex(buffer));
		buffer = new byte[2];
		System.arraycopy(msg,68, buffer, 0, 2);
		String strport = Utils.bytesToHex(buffer);
		long port = Long.parseLong(strport,16); 
		version.setPortReceive((int)port);
		buffer = new byte[16];
		System.arraycopy(msg,78, buffer, 0, 16);
		version.setAddressTrans(Utils.bytesToHex(buffer));
		buffer = new byte[2];
		System.arraycopy(msg,94, buffer, 0, 2);
		strport = Utils.bytesToHex(buffer);
		port = Long.parseLong(strport,16); 
		version.setPortTrans((int)port);
		buffer = new byte[8];
		System.arraycopy(msg,96, buffer, 0, 8);
		version.setNonce(Utils.bytesToHex(buffer));
		buffer = new byte[1];
		System.arraycopy(msg,104, buffer, 0, 1);
		String sizeUserAgentStr = Utils.bytesToHex(buffer);
		long sizeUserAgent = Long.parseLong(sizeUserAgentStr,16);
		buffer = new byte[(int)sizeUserAgent];
		System.arraycopy(msg,105, buffer, 0,(int) sizeUserAgent);
		String useragent = Utils.bytesToHex(buffer);
		version.setUserAgent(Utils.hexToAscii(useragent));
		buffer = new byte[4];
		 offset =(int) (105+sizeUserAgent);
		System.arraycopy(msg,offset, buffer, 0, 4);
		String startheigthStr = Utils.bytesToHex(buffer);
		long startheigth = Long.parseLong(startheigthStr,16);
		version.setStartHeigth((int)startheigth);
		offset =offset+4;

		if(msg.length>offset) {
			offset =offset+1;
			if(msg[offset]>0){
				version.setRelay(true);
			}
		}

		logger.debug("Message Version recu  : "+Utils.bytesToHex(msg));	
		versionReceive.setVersion(version);
		//fin du block version selon le protocole 
		/**
		 * le contenu de l'objet version est definie dans 
		 * See <a href="https://bitcoin.org/en/developer-reference#version"</a>.
		 */
		if(msg.length-offset>0) {
			VersionAckTrame verak = new VersionAckTrame();
			buffer = new byte[msg.length-offset];
			System.arraycopy(msg,offset, buffer, 0, buffer.length);
			VersionAckTrame deserialiseVerak = verak.deserialise(buffer);
			versionReceive.setVersionAck(deserialiseVerak);

		}
		return (T) versionReceive;
	}
	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		Retour retour = Utils.createRetourOK();
		TrameHeader message = null;
		VersionTrameReceive deserialise = null;
		VersionTrameMessage version;
		if(!isValidMessage(network, msg)) {
			retour = Utils.createRetourNOK(Utils.ERREUR, Messages.getString("ErreurReceive"));
			logger.error("erreur de la trame recue "+Messages.getString("ErreurReceive"));	
		}else {
			message = getTypeMessage(msg);
			if(message instanceof VersionTrameMessage) {
				deserialise = deserialise(msg);

			}
		}
		return (T) deserialise;

	}

}



