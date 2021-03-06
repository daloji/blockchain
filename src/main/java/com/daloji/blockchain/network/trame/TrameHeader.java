package com.daloji.blockchain.network.trame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;




/**
 * @author daloji
 *
 */
public abstract class TrameHeader implements Serializable {

	private static final Logger logger =  LoggerFactory.getLogger(TrameHeader.class);


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static final String MSG_TX ="01000000";

	protected static final String MSG_BLOCK ="02000000";

	protected static final String MSG_FILTERED_BLOCK ="03000000";

	protected static final String MSG_CMPCT_BLOCK ="04000000";

	protected static final String ERROR ="00000000";

	protected static final String IP_CONST ="00000000000000000000ffff";


	protected static final int port = 8333;


	protected static final int protocol= 70015;

	//Serveur qui permet de trouver l IP externe
	private static final String WHOIS_MY_IP="http://checkip.amazonaws.com";

	private PeerNode fromPeer;

	private String magic;

	protected long epoch;

	private String commande;

	private int length;

	private String checksum;

	private String addressReceive;

	private String addressTrans;

	private boolean partialTrame =false;
	
	

	public String getAddressTrans() {
		return addressTrans;
	}

	public void setAddressTrans(String addressTrans) {
		this.addressTrans = addressTrans;
	}

	public String getAddressReceive() {
		return addressReceive;
	}

	public void setAddressReceive(String addressReceive) {
		this.addressReceive = addressReceive;
	}


	public boolean isPartialTrame() {
		return partialTrame;
	}

	public void setPartialTrame(boolean partialTrame) {
		this.partialTrame = partialTrame;
	}

	public long getEpoch() {
		return epoch;
	}

	public void setEpoch(long epoch) {
		this.epoch = epoch;
	}

	public  TrameHeader() {

	}

	public String getMagic() {
		return magic;
	}

	public void setMagic(String magic) {
		this.magic = magic;
	}

	public String getCommande() {
		return commande;
	}

	public void setCommande(String commande) {
		this.commande = commande;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}


	public PeerNode getFromPeer() {
		return fromPeer;
	}

	public void setFromPeer(PeerNode fromPeer) {
		this.fromPeer = fromPeer;
	}



	public byte[] generateHeader() {
		String header =getMagic();
		header = header + getCommande();
		header = header + Utils.intHexpadding(getLength(), 4);
		header = header + getChecksum();
		return Utils.hexStringToByteArray(header);
	}


	public void getExternalIp() {

		Retour retour = Utils.createRetourOK();
		try {
			URL whatismyip = new URL(WHOIS_MY_IP);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			addressReceive = in.readLine();

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
			retour.setDiagnostic(Utils.FATAL_ERROR);
			retour.setLibelle("url malformed");
			retour.setResultat(Utils.RETOUR_NOK);
		} catch (IOException e) {
			logger.error(e.getMessage());
			retour.setDiagnostic(Utils.FATAL_ERROR);
			retour.setLibelle("url malformed");
			retour.setResultat(Utils.RETOUR_NOK);
		}finally {
			if( !Utils.isRetourOK(retour)) {
				//probleme lors de la recuperation IP externe remplacement par locahost
				//certain noeud refuse par contre c'est une situation de repli 
				addressReceive="127.0.0.1";
			}
		}

	}



	/**
	 * Verification de la validitée de la trame recue venant d'un noeud du reseau
	 * 
	 * @param message
	 *          trame recue 
	 * @return boolean true la trame est valide
	 */
	public boolean isValidMessage(NetParameters network,byte[] msg) {
		boolean isvalid =false;
		if(msg != null){
			String messagefrom = Utils.bytesToHex(msg);
			if(isMagicOK(network, messagefrom) && isValidCheckSum(msg) && isValidTimestamp(msg) ){
				isvalid =  true;
			}
		}

		return  isvalid;
	}


	/**
	 * Verification de la validité du timestamp de l'envoyeur
	 * le timestamp   doit être dans les deux heures dans le futur sinon la trame est rejetée
	 *
	 * @param message
	 *          trame recue
	 * @return boolean true si le timestamp  est correct
	 */
	public boolean isValidTimestamp(final byte[] msg) {
		boolean valid = false;
		byte[] buffer = new byte[4];
		System.arraycopy(msg,36, buffer, 0, 4);
		String time = Utils.bytesToHex(buffer);
		long epoch = Utils.little2big(time);
		Instant instant = Instant.ofEpochSecond(epoch);
		LocalDateTime datetimestamp = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime datenow = LocalDateTime.now();
		//verification validité de la date
		datetimestamp.plusHours(2);
		if(datenow.isAfter(datetimestamp)) {
			valid = true;	
		}

		return valid;


	}


	public boolean isValidCheckSum(final byte[] msg) {
		boolean value =false;
		if(msg.length>16) {
			byte[] lengthArray = new byte[4];
			System.arraycopy(msg, 16, lengthArray, 0, 4);
			String strlength = Utils.bytesToHex(lengthArray);
			long length = Utils.little2big(strlength);
			byte[] checksum = new byte[4];
			System.arraycopy(msg, 20, checksum, 0, 4); 
			//payload
			byte[] payload = new byte[(int)length];
			System.arraycopy(msg, 24, payload, 0,(int) length);
			//checksum compute
			byte[] checksumpayload = Crypto.doubleSha256(payload);
			//first 4 byte
			byte[] checksumcompute = new byte[4];
			System.arraycopy(checksumpayload, 0, checksumcompute, 0, 4);

			if(Arrays.equals(checksumcompute, checksum)) {
				value =true;	
			}else {

				logger.error("checksum invalid" );
				logger.error("cheksum read :  "+Utils.bytesToHex(checksum));
				logger.error("cheksum compute :  "+Utils.bytesToHex(checksumcompute));
				logger.error("chaine complete :" +Utils.bytesToHex(msg) );

			}

		}

		return value;
	}

	/**
	 * Verification de l'entete Magique (MAGIC Header)
	 *
	 * @param network
	 *          type de reseau (MainNet, ou TestNet)
	 * @param message
	 *          message 
	 * @return boolean true si l'entete est correct
	 */
	public boolean isMagicOK(NetParameters network,String message) {
		boolean value = false;
		if(message.startsWith(NetParameters.MainNet.getMagic())) {
			value = true;
		}
		return value;


	}
	


	protected boolean isValidType(byte[] data) {
		boolean value = false;
		if(data !=null) {
			if(MSG_BLOCK.equals(Utils.bytesToHex(data)) || MSG_CMPCT_BLOCK.equals(Utils.bytesToHex(data))||MSG_FILTERED_BLOCK.equals(Utils.bytesToHex(data))||MSG_TX.equals(Utils.bytesToHex(data))) {
				value = true; 
			}

		}
		return value;

	}
	
	
	protected byte[] deserialiseHeader(byte[] data) {
		byte[] buffer = null;
		int offset =0;
		if(data!=null) {
			buffer = new byte[4];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			this.setMagic(Utils.bytesToHex(buffer));
			offset = offset +  buffer.length;
			buffer = new byte[12];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			this.setCommande(Utils.bytesToHex(buffer));
			offset = offset +buffer.length;
			buffer = new byte[4];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			String hex = Utils.bytesToHex(buffer);
			long length = Utils.little2big(hex);
			this.setLength((int)length);
			offset = offset +buffer.length;
			buffer = new byte[4];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			this.setChecksum(Utils.bytesToHex(buffer));
			offset = offset +buffer.length;
			buffer = new byte[(int)length];
			System.arraycopy(data, offset, buffer, 0, buffer.length);
		}
				
		return buffer;
	}
	
	

	/**
	 *  generation de la payload de la trame
	 * 
	 * @param network
	 *     network
	 * @return payload
	 */
	public abstract String generatePayload(NetParameters network);

	/**
	 *  deserialisation de la message recu
	 * @param msg
	 *  message recu
	 * @return
	 */
	public abstract <T> T deserialise(final byte[] msg);

	public abstract String generateMessage(NetParameters network,PeerNode peer);

	public abstract <T> T receiveMessage(NetParameters network,byte[] msg);
}
