package com.daloji.blockchain.network.trame;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.InvType;
import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

/**
 * Gestion des messages Inventory
 * @author daloji
 *
 */
public class InvTrame extends TrameHeader{



	private static final Logger logger = (Logger) LoggerFactory.getLogger(InvTrame.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private List<Inventory> listinv = new ArrayList<Inventory>();


	@Override
	public String generatePayload(NetParameters network) {
		// TODO Auto-generated method stub
		return null;
	}



	public List<Inventory> getListinv() {
		return listinv;
	}



	public void setListinv(List<Inventory> listinv) {
		this.listinv = listinv;
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
				if(size<Utils.FD_HEXA) {
					buffer = new byte[1];
					offset = offset + buffer.length;				
				}else if(size>=Utils.FD_HEXA && size<=Utils.FFFF_HEXA) {
					buffer = new byte[2];
					offset = offset + 1;
					System.arraycopy(msg, offset, buffer, 0,buffer.length);
					len = Utils.bytesToHex(buffer);
					len = Utils.StrLittleEndian(len);
					size =Integer.parseInt(len,16);
					offset = offset + buffer.length;	
				}else if(size>Utils.FFFF_HEXA) {
					buffer = new byte[4];
					offset = offset + 1;
					System.arraycopy(msg, offset, buffer, 0,buffer.length);
					len = Utils.bytesToHex(buffer);
					len = Utils.StrLittleEndian(len);
					size =Integer.parseInt(len,16);
					offset = offset + buffer.length;
				}


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
									listinv.add(inventory);
									break;
								case 1: 
									inventory.setType(InvType.MSG_TX);
									inventory.setHash(hash);
									listinv.add(inventory);
									break;
								case 2: 
									inventory.setType( InvType.MSG_BLOCK);
									inventory.setHash(hash);
									listinv.add(inventory);
									break;
								case 3: 
									inventory.setType(InvType.MSG_FILTERED_BLOCK);
									inventory.setHash(hash);
									listinv.add(inventory);
									break;
								case 4: 
									inventory.setType(InvType.MSG_CMPCT_BLOCK);
									inventory.setHash(hash);
									listinv.add(inventory);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String toString() {
		return "InvTrame [listinv=" + listinv + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((listinv == null) ? 0 : listinv.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvTrame other = (InvTrame) obj;
		if (listinv == null) {
			if (other.listinv != null)
				return false;
		} else if (!listinv.equals(other.listinv))
			return false;
		return true;
	}
	

}
