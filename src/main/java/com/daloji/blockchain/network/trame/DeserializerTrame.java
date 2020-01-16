package com.daloji.blockchain.network.trame;

import java.io.Serializable;
import java.util.ArrayDeque;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class DeserializerTrame implements Serializable{

	private static final Logger logger = (Logger) LoggerFactory.getLogger(DeserializerTrame.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final DeserializerTrame instance = new DeserializerTrame();

	private static TrameHeader lastTrame;
	
	public static DeserializerTrame getInstance()
	{
		return instance;
	}

	
	/**
	 *  Recuperation du type de commande 
	 * @param cmd
	 * @return
	 */
	public  ArrayDeque<TrameHeader> deserialise(byte[] data,PeerNode peer) {
		ArrayDeque<TrameHeader> stack = new ArrayDeque<TrameHeader>();
		TrameHeader trameHeader = null;
		if(data != null) {
			while(!Utils.allZero(data) && !(trameHeader instanceof ErrorTrame)) {
				byte[] buffer = new byte[12];
				int offset = 4;
				System.arraycopy(data, offset, buffer, 0, buffer.length);
				String cmd = Utils.bytesToHex(buffer);
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
					trameHeader = new VersionAckTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else if (TrameType.PING.getInfo().equals(cmd)) {
					trameHeader = new PingTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else if(TrameType.VERACK.getInfo().equals(cmd)) {

				}else if(TrameType.ADDR.getInfo().equals(cmd)) {
					trameHeader = new AddrTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else if(TrameType.SENDHEADERS.getInfo().equals(cmd)) {

				}else if(TrameType.GETHEADERS.getInfo().equals(cmd)) {
					trameHeader = new GetHeadersTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
					System.out.println(Utils.bytesToHex(data));
				}else if(TrameType.SENDCMPCT.getInfo().equals(cmd)) {

				}else if(TrameType.TX.getInfo().equals(cmd)) {

				}else if(TrameType.BLOCK.getInfo().equals(cmd)) {

				}else if(TrameType.FEELFILTER.getInfo().equals(cmd)) {

				}else {
					if(lastTrame.isPartialTrame()) {
					byte[] header =	trameHeader.generateHeader();
				    String info = Utils.bytesToHex(header) +  Utils.bytesToHex(data);
				    data = trameHeader.deserialise(Utils.hexStringToByteArray(info));
					}else {
						trameHeader = new ErrorTrame();

					}
				}
				lastTrame = trameHeader;
				stack.add(trameHeader);

			}
		}

		return stack;
	}
}
