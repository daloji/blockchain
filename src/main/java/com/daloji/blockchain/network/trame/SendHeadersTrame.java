package com.daloji.blockchain.network.trame;

import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

public class SendHeadersTrame extends TrameHeader{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		offset = offset + buffer.length;
		buffer = new byte[4];
		System.arraycopy(msg, offset, buffer, 0, buffer.length);
		this.setChecksum(Utils.bytesToHex(buffer));
		offset = offset + buffer.length;
		if(offset<msg.length) {
			buffer = new byte[msg.length-offset];
			System.arraycopy(msg, offset, buffer, 0, buffer.length);
		}else {
			buffer = new byte[0];
		}
		  return (T)buffer;
	}

	@Override
	public String generateMessage(NetParameters network, PeerNode peer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T receiveMessage(NetParameters network, byte[] msg) {
		SendHeadersTrame sendHeadersTrame = null;
		if(!isValidMessage(network, msg)) {
	//		retour = Utils.createRetourNOK(Utils.ERREUR, Messages.getString("ErreurReceive"));
		//	logger.error("erreur de la trame recue "+Messages.getString("ErreurReceive"));	
		}else {
			sendHeadersTrame = deserialise(msg);
		}
		return (T) sendHeadersTrame;
	}

	@Override
	public String generatePayload(NetParameters network) {
		// TODO Auto-generated method stub
		return null;
	}


}
