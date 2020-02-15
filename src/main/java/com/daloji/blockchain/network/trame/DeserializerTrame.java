package com.daloji.blockchain.network.trame;

import java.io.Serializable;
import java.util.ArrayDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;


public class DeserializerTrame implements Serializable{



	private static final Logger logger = LoggerFactory.getLogger(DeserializerTrame.class);

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
					trameHeader = new AddrTrame(false);
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
					logger.error("***************** TX ****************************************");
				}else if(TrameType.BLOCK.getInfo().equals(cmd)) {
					BlockTrame	blocktrameHeader = new BlockTrame();
					blocktrameHeader.setFromPeer(peer);
					data = blocktrameHeader.deserialise(data);
					if(blocktrameHeader.isIncorrect()) {
						trameHeader = new ErrorTrame();
					}else {
						trameHeader = blocktrameHeader;
					}
					
				}
				else if(TrameType.REJECT.getInfo().equals(cmd)) {
					logger.error("***************** REJECT ****************************************");

				}else if(TrameType.FEELFILTER.getInfo().equals(cmd)) {
					trameHeader = new FeelFilterTrame();
					trameHeader.setFromPeer(peer);
					data = trameHeader.deserialise(data);
				}else {
					if(trame !=null) {
						if(trame.isPartialTrame() || trame instanceof InvTrame) {
							byte[] header =	trame.generateHeader();
							if(validCheckSum(trame.getChecksum(),data)) {
								String info = Utils.bytesToHex(header) +  Utils.bytesToHex(data);
								data = trame.deserialise(Utils.hexStringToByteArray(info));
								trameHeader =trame;
							}else {
								data = findCommand(data);
							}
						
						}else {
							logger.error(cmd);
							data = findCommand(data);
						}
					}else {
						data = findCommand(data);	
					}
				}
				if(trameHeader !=null) {
					trame = trameHeader;
					stack.add(trameHeader);	
				}
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
		byte[] info = new byte[0];
		String value = Utils.bytesToHex(data);
		int indexMagic = value.indexOf(NetParameters.MainNet.getMagic());
		if(indexMagic>0) {
			value = value.substring(indexMagic, value.length());
			info = Utils.hexStringToByteArray(value);
		}
		return info;

	}


	private boolean validCheckSum(String checksum,byte[] data) {
		boolean valid= false;
		if(data !=null) {
			int offset = 0;
			byte[] buffer = new byte[4];
			if(offset+24<data.length) {
				System.arraycopy(data, offset, buffer, 0, buffer.length);
				String strlength = Utils.bytesToHex(buffer);
				long length = Utils.little2big(strlength);
				offset = offset + 24;
				if(length>data.length) {
					length= data.length - offset;
				}
				buffer = new byte[(int)length ];
				if(length<data.length) {
					System.arraycopy(data, offset, buffer, 0, buffer.length);
					byte[] bytecheck = Crypto.doubleSha256(buffer);
					String checksumCompute = Utils.bytesToHex(bytecheck);
					if(checksumCompute.equals(checksum)) {
						valid = true;
					}
				}


			}

		}
		return valid;
	}
}
