package com.daloji.blockchain.network.trame;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.InvType;
import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;


public class GetDataTrame extends TrameHeader{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger =  LoggerFactory.getLogger(GetDataTrame.class);

	private static final String commande="getdata";

	private List<Inventory> listInv;


	public GetDataTrame(Inventory inv) {
		listInv = new ArrayList<Inventory>();
		listInv.add(inv);
	}

	public GetDataTrame(List<Inventory> listInv) {
		this.listInv = new ArrayList<Inventory>();
		this.listInv.addAll(listInv);
	}
	
	@Override
	public String generatePayload(NetParameters network) {
		String payload = "";
		String length = "0";
		int size = listInv.size();
		if(size<Utils.FD_HEXA) {
			length = Utils.intHexpadding(size,1);				
		}else if(size>=Utils.FD_HEXA && size<=Utils.FFFF_HEXA) {
			length = Utils.intHexpadding(size,3);				
		}else if(size>Utils.FFFF_HEXA) {
			length = Utils.intHexpadding(size,5);
		}
		payload = payload + length;
		for(int i=0;i<listInv.size();i++) {
			Inventory inv = listInv.get(i);
			payload = payload + Utils.intHexpadding(inv.getType().getValue(), 4);
			payload = payload + inv.getHash();

		}

		return payload;
	}

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
		if(length <msg.length) {
			byte[] info = new byte[4];
			System.arraycopy(msg, offset, info, 0, info.length);
			String magic = Utils.bytesToHex(info);
			if(!isMagicOK(NetParameters.MainNet, magic)) {
				buffer = new byte[1];
				System.arraycopy(msg, offset, buffer, 0, buffer.length);
				String len = Utils.bytesToHex(buffer);
				long size = Integer.parseInt(len,16);
				Pair<Long, Integer> compactsize = Utils.getCompactSize(size, offset, msg);
				size = compactsize.first;
				offset = compactsize.second;
				listInv = new ArrayList<Inventory>();	
				for(int i=0;i<size;i++) {
					buffer = new byte[4];
					if((offset+buffer.length)<msg.length) {
						System.arraycopy(msg, offset, buffer, 0,buffer.length);
						if(isValidType(buffer)) {
							String type = Utils.bytesToHex(buffer);
							offset = offset + buffer.length;
							int decimalType = Integer.parseInt(Utils.StrLittleEndian(type),16);
							buffer = new byte[32];
							if((offset+buffer.length-1)<msg.length) {
								System.arraycopy(msg, offset, buffer, 0,buffer.length);
								offset = offset + buffer.length;
								String hash = Utils.bytesToHex(buffer);
								Inventory inventory = new Inventory();
								switch (decimalType) {
								case 0:
									inventory.setType(InvType.ERROR);
									inventory.setHash(hash);
									listInv.add(inventory);
									break;
								case 1: 
									inventory.setType(InvType.MSG_TX);
									inventory.setHash(hash);
									listInv.add(inventory);
									break;
								case 2: 
									inventory.setType( InvType.MSG_BLOCK);
									inventory.setHash(hash);
									listInv.add(inventory);
									break;
								case 3: 
									inventory.setType(InvType.MSG_FILTERED_BLOCK);
									inventory.setHash(hash);
									listInv.add(inventory);
									break;
								case 4: 
									inventory.setType(InvType.MSG_CMPCT_BLOCK);
									inventory.setHash(hash);
									listInv.add(inventory);
									break;

								default:
									break;
								}	
							}

						}else {
							offset = offset + buffer.length;
						}
					}	
				}
			}else {
				offset = offset + buffer.length;	
			}


		}
		byte[] info =new byte[offset];
		System.arraycopy(msg,0, info, 0, info.length);
		if(logger.isDebugEnabled()) {
			logger.debug("["+getFromPeer().getHost()+"]"+"<IN> Inv   "+Utils.bytesToHex(info));
		}
		if(offset<msg.length) {

			buffer = new byte[msg.length -offset];
			System.arraycopy(msg, offset, buffer, 0,buffer.length);
		}else {
			buffer = new byte[0];
		}

		return (T) buffer;
	}

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		setMagic(network.getMagic());
		//get Epoch Time
		epoch  = Instant.now().getEpochSecond();
		setAddressTrans(peer.getHost());
		String message ="";
		message = message +getMagic();
		message = message + Utils.convertStringToHex(commande,12);
		String payload = generatePayload(network);
		//lenght
		int length = payload.length()/2;
		message= message +Utils.intHexpadding(length, 4);
		byte[] payloadbyte = Utils.hexStringToByteArray(payload);
		//4 premier octet seulement pour le chechsum
		byte[] array = Crypto.doubleSha256(payloadbyte);
		String checksum =Utils.bytesToHex(array);
		checksum =checksum.substring(0, 8);
		message = message +checksum;
		//ajout de la payload 
		message = message +payload;
		return message;

	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Inventory> getListInv() {
		return listInv;
	}

	public void setListInv(List<Inventory> listInv) {
		this.listInv = listInv;
	} 

}
