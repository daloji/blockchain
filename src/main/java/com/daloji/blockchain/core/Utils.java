package com.daloji.blockchain.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.network.NetParameters;
/**
 * 
 * Classe Utilitaires
 * 
 * @author daloji
 *
 */
import com.daloji.blockchain.network.trame.TrameType;
public class Utils {

	public static String RETOUR_OK ="OK";

	public static String RETOUR_NOK ="NOK";

	public static String FATAL_ERROR ="FATAL_ERROR";

	public static String ERREUR = "Erreur";

	public static final String DNS_SEED = "seed.bitcoin.sipa.be";

	public static final int BUFFER_SIZE = 1024;

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();


	/* timeout peer */
	public static final int timeoutPeer = 1000*3600*2;

	/**
	 * 
	 * Conversion d'une chaine de caractere en hexadecimal
	 * 
	 * @param str
	 *    chaine de caractere
	 * @param size
	 *        nombre d'octet 
	 * @return chaine en hexadecimal
	 */
	public static String convertStringToHex(String str,int size){

		String result="";
		char[] chars = str.toCharArray();
		StringBuffer hex = new StringBuffer();
		for(int i = 0; i < chars.length; i++){
			hex.append(Integer.toHexString((int)chars[i]));
		}
		result =hex.toString();
		while(result.length()<size*2) {
			result=result +"0";	
		}
		return result;
	}
	/**
	 * conversion d'une donnée hexadecimal en tableau d'octet
	 * @param s
	 *  chaine hexadecimal
	 * @return  tableau d'octet
	 */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}

	/**
	 * conversion d'un entier en chaine hexadecimal 
	 * @param value
	 * valeur entiere
	 * @param padding
	 * padding
	 * @return chaine hexadecimal
	 */
	public static String intHexpadding(int value,int padding) {
		String rep =Integer.toHexString(value);
		if (rep.length() % 2  != 0) {

			rep ="0"+rep;	
		}
		rep = toLittleEndian(rep);
		while(rep.length()<padding*2) {
			rep=rep +"0";	
		}

		return rep;
	}

	/**
	 * Generation aleatoire d'un nombre avec une taille en octet specifiée
	 *   (utilisé comme nonce dans la blockchain)
	 * @param size
	 * taille en octet
	 * @return chaine en hexadecimal
	 */
	public static String generateNonce(int size) {
		SecureRandom r = new SecureRandom();
		StringBuffer sb = new StringBuffer();
		while(sb.length() < size){
			sb.append(Integer.toHexString(r.nextInt()));
		}

		return sb.toString().substring(0, size);
	}

	/**
	 * Conversion d'une date (eoch unix ) en chaine hexadecimal
	 * @param unixdate
	 *      epoch
	 * @param size
	 *       taille en octet
	 * @return chaine hexadecimal
	 */
	public static String convertDateString(long unixdate,int size) {
		String result = Long.toString(unixdate, 16);
		result = toLittleEndian(result);
		while(result.length()<size*2) {
			result=result +"0";	
		}
		return result;
	}


	/**
	 * Conversion d'un chaine hexadecimal au format big-endian
	 * @param var
	 * chaine hexadecimal
	 * @return chaine au format big-endian
	 */
	public static long little2big(String  var) {
		long decimal=Long.parseLong(var,16); 
		return little2big(decimal);
	}


	/**
	 * Conversion d'un entier   format big-endian
	 * @param i
	 * valeur entiere
	 * @return chaine au format big-endian
	 */
	public static long little2big(long i) {
		return (i&0xff)<<24 | (i&0xff00)<<8 | (i&0xff0000)>>8 | (i>>24)&0xff;
	}


	/**
	 * Conversion d'une adresse IP en chaine hexadecimal
	 * @param ipAddr
	 * adresse IP
	 * @return chaine hexadecimal representant l'adresse IP
	 */
	public static String convertIPtoHex(final String  ipAddr) {
		String hex="";
		String[] part = ipAddr.split("[\\.,]");
		if (part.length < 4) {
			return "";
		}
		for (int i = 0; i < 4; i++) {
			int decimal = Integer.parseInt(part[i]);
			if (decimal < 16) {
				hex += "0" + String.format("%01X", decimal);
			} else {
				hex += String.format("%01X", decimal);
			}
		}
		return hex;
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}





	public static String toLittleEndian(final String hex) {

		String hexLittleEndian = "";
		if (hex.length() % 2 != 0) return "";
		for (int i = hex.length() - 2; i >= 0; i -= 2) {
			hexLittleEndian += hex.substring(i, i + 2);
		}
		return hexLittleEndian;

	}

	public static Retour createRetourOK() {
		return new Retour(RETOUR_OK); 
	}

	public static Retour createRetourNOK(String diagnostic,String info) {
		return new Retour(RETOUR_NOK, diagnostic, info);
	}

	public static boolean isRetourOK(Retour retour) {
		boolean value = false;
		if(RETOUR_OK.equals(retour.getResultat())){
			value=true;
		}
		return value;
	}


	public static String hexToAscii(String hexStr) {
		StringBuilder output = new StringBuilder("");
		for (int i = 0; i < hexStr.length(); i += 2) {
			String str = hexStr.substring(i, i + 2);
			if(!"00".equals(str)) {
				output.append((char) Integer.parseInt(str, 16));
			}
		}
		return output.toString();
	}


	public static byte[] checksums(byte[] input) {
		return checksum(input, 0, input.length);
	}



	public static byte[] checksum(byte[] input, int offset, int length) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.update(input, offset, length);
			return digest.digest(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}

	}


	public static TrameType findTrameCommande(byte[] data) {
		byte[] commande = new byte[12];
		TrameType trametype = TrameType.ERROR;

		String message = Utils.bytesToHex(data);
		if(message.startsWith(NetParameters.MainNet.getMagic())) {
			System.arraycopy(data,4, commande, 0, commande.length);
			String cmd = Utils.bytesToHex(commande);
			if(TrameType.VERACK.getInfo().equals(cmd)) {
				trametype = TrameType.VERACK;
			}
			if(TrameType.ADDR.getInfo().equals(cmd)) {
				trametype = TrameType.ADDR;
			}
			if(TrameType.SENDHEADERS.getInfo().equals(cmd)) {
				trametype = TrameType.SENDHEADERS;
			}
			if(TrameType.TX.getInfo().equals(cmd)) {
				trametype = TrameType.TX;
			}
			if(TrameType.VERSION.getInfo().equals(cmd)) {
				trametype = TrameType.VERSION;
			}
		}

		return trametype;
	}


}
