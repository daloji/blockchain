package com.daloji.blockchain.network.trame;

import java.io.Serializable;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.NetParameters;

public class DeserializerTram implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 *  Recuperation du type de commande 
	 * @param cmd
	 * @return
	 */
	public static  TrameHeader findCommande(byte[] data) {
		TrameHeader trameHeader = null;
		if(data != null) {
			byte[] buffer = new byte[12];
			int offset = 4;
			System.arraycopy(data, offset, buffer, 0, buffer.length);
			String cmd =Utils.bytesToHex(buffer);
			if(TrameType.VERACK.getInfo().equals(cmd)) {
				trameHeader = new VersionAckTrame();
				data = trameHeader.deserialise(data);
			}
			if(TrameType.VERSION.getInfo().equals(cmd)) {
				trameHeader = new VersionTrameMessage(false);
				data = trameHeader.deserialise(data);
			}
			if(TrameType.INV.getInfo().equals(cmd)) {
				
			}
			if(TrameType.VERACK.getInfo().equals(cmd)) {
				
			}
			if(TrameType.VERACK.getInfo().equals(cmd)) {
			  
		}
	 
		
		/*
		String cmd = message.substring(8, 32);
		TrameType trametype = TrameType.ERROR;
		TrameHeader trameHeader = null;
		if(TrameType.VERACK.getInfo().equals(cmd)) {
			trameHeader =new VersionAckTrame();
			trameHeader.setCommande(cmd);
			message = message.substring(32, message.length());
			String strlength = message.substring(0, 8);
			strlength = Utils.StrLittleEndian(strlength);
			int len = Integer.parseInt(strlength, 16);
			trameHeader.setLength(len);
			trameHeader.setChecksum(message.substring(0, 8));
			message = message.substring(8, message.length());
			
		}
		if(TrameType.ADDR.getInfo().equals(cmd)) {
			trametype = TrameType.ADDR;
		}
		if(TrameType.SENDHEADERS.getInfo().equals(cmd)) {
			trametype = TrameType.SENDHEADERS;
		}

		if(TrameType.GETHEADERS.getInfo().equals(cmd)) {
			trametype = TrameType.GETHEADERS;
		}
		if(TrameType.SENDCMPCT.getInfo().equals(cmd)) {
			trametype = TrameType.SENDCMPCT;
		}
		if(TrameType.TX.getInfo().equals(cmd)) {
			trametype = TrameType.TX;
		}
		if(TrameType.VERSION.getInfo().equals(cmd)) {
			trameHeader =new VersionTrameMessage(false);
			VersionTrameMessage = trameHeader.deserialise(msg)
			trameHeader.setCommande(cmd);
			message = message.substring(32, message.length());
			String strlength = message.substring(0, 8);
			strlength = Utils.StrLittleEndian(strlength);
			int len = Integer.parseInt(strlength, 16);
			trameHeader.setLength(len);
			trameHeader.setChecksum(message.substring(0, 8));
			message = message.substring(8, message.length());
			String payload = message.substring(0,len);
			byte [] bpayload = Utils.hexStringToByteArray(payload);
			if(Utils.allZero(bpayload)) {
				trameHeader.setPartialTrame(true);
}
			}else {
				

				versionReceive.setVersion(version);
			}

			
			trametype = TrameType.VERSION;
		}

		if(TrameType.PING.getInfo().equals(cmd)) {
			trametype = TrameType.PING;
		}

		if(TrameType.INV.getInfo().equals(cmd)) {
			trametype = TrameType.INV;
		}
		
		if(TrameType.BLOCK.getInfo().equals(cmd)) {
			trametype = TrameType.BLOCK;
		}


		if(TrameType.FEELFILTER.getInfo().equals(cmd)) {
			trametype = TrameType.FEELFILTER;
		}
		return trameHeader;
		*/
		return null;
	}		

	public static TrameHeader deserialize(byte[] data) {
	
		if(data != null) {
			String message = Utils.bytesToHex(data);
			if(message.startsWith(NetParameters.MainNet.getMagic())) { 
				//TrameType trameType = findCommande(message);
				
			}
			
		}
		return null;
		
	}

}
