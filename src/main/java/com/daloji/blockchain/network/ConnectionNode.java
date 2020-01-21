package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.List;

import javax.naming.NamingException;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Inv;
import com.daloji.blockchain.core.InvType;
import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.DeserializerTrame;
import com.daloji.blockchain.network.trame.InvTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;
import com.daloji.blockchain.network.trame.TrameHeader;

import ch.qos.logback.classic.Logger;

public class ConnectionNode  extends AbstractCallable{

	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(ConnectionNode.class);

	private  TrameHeader lastTrame;

	public ConnectionNode(NetworkEventHandler networkListener,BlockChainEventHandler blockchaiListener,NetParameters netparam,PeerNode peerNode) throws NamingException {
		super();
		this.peerNode = peerNode;
		this.networkListener = networkListener;
		this.blockChainListener = blockchaiListener;
		this.netParameters = netparam;
	}
	

	@Override
	public Object call() throws Exception {

		byte[] data = new byte[Utils.BUFFER_SIZE];
		socketClient = new Socket(peerNode.getHost(),peerNode.getPort());
		socketClient.setSoTimeout(Utils.timeoutPeer);
		outPut = new DataOutputStream(socketClient.getOutputStream());
		input = new DataInputStream(socketClient.getInputStream()); 
		int count = 1;
		listState.add(STATE_ENGINE.BOOT);
		while(state !=STATE_ENGINE.START && count!=-1) {
			switch(state) {
			case BOOT : state = sendVersion(outPut,netParameters,peerNode);
			listState.add(state);
			break;
			case VER_ACK_RECEIVE:state = sendVerAck(outPut,netParameters,peerNode);
			listState.add(state);

			break;
			case VERSION_SEND: //state = sendVerAck(outPut,netParameters,peerNode);
			break;
			case READY : state = sendGetBlock(outPut, netParameters, peerNode);
			break;
			case GETBLOCK_SEND :
				break;
			}
			count = input.read(data);
			if(count > 0) {
				logger.info(Utils.bytesToHex(data));
				ArrayDeque<TrameHeader> deserialize = DeserializerTrame.getInstance().deserialise(lastTrame,data,peerNode);
				TrameHeader trame = deserialize.getLast();
				lastTrame = trame;
				callGetBlock(deserialize);
				state = findNExtStep(deserialize);
			}

		}	
		return null;

	}


  private void callGetBlock(ArrayDeque<TrameHeader> trameArray) {
	  
	  if(trameArray !=null) {
		  
		  for(TrameHeader trame:trameArray) {
			  if(trame instanceof InvTrame) {
				  List<Inventory> listinventory =((InvTrame) trame).getListinv();
				  for(Inventory inventory :listinventory) {
					  if(inventory.getType() ==InvType.MSG_BLOCK) {
						  blockChainListener.onBlockHeaderReceive(inventory);  
					  }
				  }
			  }
		  }
	  }
  }


}
