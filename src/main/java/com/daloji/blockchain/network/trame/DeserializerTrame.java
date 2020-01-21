package com.daloji.blockchain.network.trame;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Arrays;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class DeserializerTrame implements Serializable{

	private static final Logger logger = (Logger) LoggerFactory.getLogger(DeserializerTrame.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final DeserializerTrame instance = new DeserializerTrame();



	public static DeserializerTrame getInstance()
	{
		return instance;
	}



	/**
	 *  Recuperation du type de commande 
	 * @param cmd
	 * @return
	 */
	public  ArrayDeque<TrameHeader> deserialise(TrameHeader trame, byte[] data,PeerNode peer) {
		ArrayDeque<TrameHeader> stack = new ArrayDeque<TrameHeader>();
		TrameHeader trameHeader = null;
		if(data != null) {
			while(!Utils.allZero(data) && !(trameHeader instanceof ErrorTrame)) {
				byte[] buffer = new byte[12];
				int offset = 4;
				if(buffer.length+offset>data.length) {
					break;	
				}
				System.arraycopy(data, offset, buffer, 0, buffer.length);
				String cmd = Utils.bytesToHex(buffer);
				//System.out.println(Utils.bytesToHex(data));
				if(TrameType.VERACK.getInfo().equals(cmd)) {
					trameHeader = new VersionAckTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else if(TrameType.VERSION.getInfo().equals(cmd)) {
					trameHeader = new VersionTrameMessage(false);
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else if(TrameType.INV.getInfo().equals(cmd)) {
					trameHeader  = new InvTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);

				}else if(TrameType.SENDHEADERS.getInfo().equals(cmd)) {
					trameHeader = new SendHeadersTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else if (TrameType.PING.getInfo().equals(cmd)) {
					trameHeader = new PingTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else if(TrameType.ADDR.getInfo().equals(cmd)) {
					trameHeader = new AddrTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else if(TrameType.GETHEADERS.getInfo().equals(cmd)) {
					trameHeader = new GetHeadersTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else if(TrameType.SENDCMPCT.getInfo().equals(cmd)) {
					trameHeader = new SendCmpctTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else if(TrameType.TX.getInfo().equals(cmd)) {

				}else if(TrameType.BLOCK.getInfo().equals(cmd)) {

				}else if(TrameType.FEELFILTER.getInfo().equals(cmd)) {
					trameHeader = new FeelFilterTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else {
					if(trame.isPartialTrame()) {
						byte[] header =	trame.generateHeader();
						String info = Utils.bytesToHex(header) +  Utils.bytesToHex(data);
						data = trame.deserialise(Utils.hexStringToByteArray(info));
						trameHeader =trame;
					}else {
						if(trame instanceof InvTrame) {
							byte[] header =	trame.generateHeader();
							String info = Utils.bytesToHex(header) +  Utils.bytesToHex(data);
							data = trame.deserialise(Utils.hexStringToByteArray(info));
							trameHeader = trame;
						}else {
							logger.error(cmd);
							data = findCommand(data);
							trameHeader = new ErrorTrame();	
						}
						
					}
				}
				trame = trameHeader;
				stack.add(trameHeader);

			}
		}

		return stack;
	}

	/**
	 * cherche la prochaine trame dans un flux d'octet
	 * @param data
	 * 		data
	 * @return
	 */
	private byte[] findCommand(byte[] data) {
		logger.info(Utils.bytesToHex(data));
		byte[] value= new byte[0];
		if(data != null) {
			int offset = 0;
			while(offset<data.length) {
				byte[] buffer = new byte[4];
				System.arraycopy(data, offset, buffer, 0, buffer.length);  
				value = new byte[data.length-offset];
				if(!Arrays.equals(buffer, Utils.hexStringToByteArray(NetParameters.MainNet.getMagic()))) {
					System.arraycopy(data, offset, value, 0, value.length);  
					data =value;
				}else {
					System.arraycopy(data, offset, value, 0, value.length);  
					return value;
				}
				offset = offset +buffer.length;
			}
		}
		return value;

	}
}
