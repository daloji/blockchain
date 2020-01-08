package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.Callable;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Crypto;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.GetBlocksTrame;
import com.daloji.blockchain.network.trame.GetHeadersTrame;
import com.daloji.blockchain.network.trame.ObjectTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;
import com.daloji.blockchain.network.trame.SendHeadersTrame;
import com.daloji.blockchain.network.trame.TrameType;
import com.daloji.blockchain.network.trame.VersionAckTrame;
import com.daloji.blockchain.network.trame.VersionTrameMessage;
import com.daloji.blockchain.network.trame.VersionTrameReceive;

import ch.qos.logback.classic.Logger;



/**
 * Objet Callable qui prend en charge les connections vers les autres 
 *  Noeuds en mode client
 *  
 * @author daloji
 *
 */
public class ConnectionNode2  implements Callable<Object>{

	private static final Logger logger = (Logger) LoggerFactory.getLogger(ConnectionNode2.class);


	private NetworkHandler networkListener;

	private PeerNode peerNode;

	private Socket socketClient;

	private DataOutputStream outPut;

	private DataInputStream input;

	private NetParameters netParameters;
	
	private STATE_ENGINE state = STATE_ENGINE.BOOT;

	private Stack<ObjectTrame> pileCommand = new Stack<ObjectTrame>();


	public ConnectionNode2(NetworkHandler networkListener,NetParameters netparam,PeerNode peerNode) {
		this.peerNode = peerNode;
		this.networkListener = networkListener;
		this.netParameters = netparam;
	}


	public PeerNode getPeerNode() {
		return peerNode;
	}


	public void setPeerNode(PeerNode peerNode) {
		this.peerNode = peerNode;
	}


	public Socket getSocketClient() {
		return socketClient;
	}

	public void setSocketClient(Socket socketClient) {
		this.socketClient = socketClient;
	}

	public DataOutputStream getOutPut() {
		return outPut;
	}

	public void setOutPut(DataOutputStream outPut) {
		this.outPut = outPut;
	}

	public DataInputStream getInput() {
		return input;
	}

	public void setInput(DataInputStream input) {
		this.input = input;
	}

	/*
	@Override
	public Object call() throws Exception {
		byte[] data = new byte[Utils.BUFFER_SIZE];
		VersionTrameReceive versionTrame = null;

		try{
			socketClient = new Socket(peerNode.getHost(),peerNode.getPort());
			socketClient.setSoTimeout(Utils.timeoutPeer);
			VersionTrameMessage version = new VersionTrameMessage();
			String trame = version.generateMessage(netParameters, peerNode);
			byte[] dataoutput = Utils.hexStringToByteArray(trame);
			 outPut = new DataOutputStream(socketClient.getOutputStream());
			 outPut.write(dataoutput, 0, dataoutput.length);
			state = STATE_ENGINE.VERSION_SEND;
			input = new DataInputStream(socketClient.getInputStream()); 
			networkListener.onNodeConnected(this);
			int count = input.read(data);
			while(count>0) {
				switch(state){

				case VERSION_SEND: 
					versionTrame  = openSessionNode(outPut,netParameters,peerNode,version,data);
					if(versionTrame == null ) {
						logger.error("erreur lors de l'echange de version vers"+peerNode.getHost());
						networkListener.onNodeConnectHasError(this);
					}else {
						//construction de la blockchain
						GetHeadersTrame getblock = new GetHeadersTrame();
						String message = getblock.generateMessage(netParameters, peerNode);
						sendGetBlock(outPut, netParameters, peerNode, Utils.hexStringToByteArray(message));
					}

					break;

				case VER_ACK_SEND:
					break;

				case VER_ACK_RECEIVE:
					break;

				case TX_RECEIVE:
					break;

				case TX_SEND:
					break;


				case ADDR_RECEIVE:
					break;

				case ADDR_SEND:
					break;


				case SENDHEADERS_RECEIVE:
					SendHeadersTrame sendTrame = new SendHeadersTrame();
					sendTrame.receiveMessage(netParameters, data);
					break;


				case SENDHEADERS_SEND:
					break;

				case INV_RECEIVE:
					break;

				case INV_SEND:
					break;


				case SENDCMPCT_RECEIVE:
					break;

				case SENDCMPCT_SEND:
					break;
				case ERROR_PROTOCOLE:
					logger.error("erreur lors de l'echange de version vers "+peerNode.getHost());
					networkListener.onNodeConnectHasError(this);
					break;
				}

				count = input.read(data);
				TrameType trametype = Utils.findTrameCommande(data);
				state = whoIsNextStep(trametype);
				logger.info("["+peerNode.getHost()+"]"+"Trame recu "+Utils.bytesToHex(data));
			}

		}catch (Exception e) {
			logger.error(e.getMessage());		
		}finally {
			if(socketClient !=null) {
				socketClient.close();
			}
		}

		return null;

	}
	 */
	private STATE_ENGINE whoIsNextStep(TrameType trametype) {
		STATE_ENGINE state = STATE_ENGINE.ERROR;
		if(TrameType.ADDR == trametype) {
			state = STATE_ENGINE.ADDR_RECEIVE;
		}
		if(TrameType.SENDHEADERS == trametype) {
			state = STATE_ENGINE.SENDHEADERS_RECEIVE;
		}
		if(TrameType.TX == trametype) {
			state = STATE_ENGINE.TX_RECEIVE;
		}
		if(TrameType.VERACK == trametype) {
			state = STATE_ENGINE.VER_ACK_RECEIVE;
		}
		if(TrameType.VERSION == trametype) {
			state = STATE_ENGINE.VERSION_RECEIVE;
		}
		if(TrameType.INV == trametype) {
			state = STATE_ENGINE.INV_RECEIVE;
		}
		if(TrameType.SENDCMPCT == trametype) {
			state = STATE_ENGINE.SENDCMPCT_RECEIVE;
		}

		return state;

	}

	/**
	 *  ouverture de la session (envoi de la trame version)
	 * @param outPut
	 *  dataoutput
	 * @param netparam
	 *     type de reseau
	 * @param peernode
	 *   noeud bitcoin
	 * @param version
	 *  message version
	 * @param data
	 * byte array
	 * @return objet version receive
	 * @throws IOException
	 */
	private VersionTrameReceive openSessionNode(DataOutputStream outPut,NetParameters netparam,PeerNode peernode,VersionTrameMessage version,byte[] data) throws IOException {
		VersionTrameReceive versionTrame = version.receiveMessage(netparam, data);

		if(versionTrame != null) {
			//check VerAck trame receive
			if(versionTrame.getVersion() != null) {
				state = STATE_ENGINE.VERSION_RECEIVE;
			}
			if(versionTrame.getVersionAck()!= null) {
				state = STATE_ENGINE.VER_ACK_RECEIVE;
				VersionAckTrame verAck = new VersionAckTrame();
				String trame = verAck.generateMessage(netparam, peernode);
				byte[] dataoutput = Utils.hexStringToByteArray(trame);
				outPut.write(dataoutput, 0, dataoutput.length);

			}
		}

		return  versionTrame;
	}


	private STATE_ENGINE sendGetBlock(DataOutputStream outPut,NetParameters netparam,PeerNode peernode) throws IOException {
		//construction de la blockchain
		GetBlocksTrame getblock = new GetBlocksTrame();
		String trame = getblock.generateMessage(netParameters, peerNode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length);	
		logger.info("<OUT>  :"+Utils.bytesToHex(data));
		return state.GETBLOCK_SEND;
	}



	private STATE_ENGINE sendVersion(DataOutputStream outPut,NetParameters netparam,PeerNode peernode) throws IOException {

		STATE_ENGINE state = STATE_ENGINE.BOOT;
		VersionTrameMessage version = new VersionTrameMessage();
		String trame = version.generateMessage(netParameters, peerNode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length); 
		return state.VERSION_SEND;
	}


	private Pair<STATE_ENGINE,VersionTrameReceive> receiveVersion(DataOutputStream outPut,NetParameters netparam,PeerNode peernode,final byte[] data) throws IOException {
		state = STATE_ENGINE.VERSION_SEND;
		VersionTrameMessage version = new VersionTrameMessage();
		VersionTrameReceive versionTrame = version.receiveMessage(netparam, data);
		if(versionTrame != null) {
			//check VerAck trame receive
			if(versionTrame.getVersion() != null) {
				if(versionTrame.getVersion().isPartialTrame()) {
					state = STATE_ENGINE.VERSION_PARTIAL_RECEIVE;	
				}else {
					state = STATE_ENGINE.VERSION_RECEIVE;
				}
			}
			if(versionTrame.getVersionAck()!= null) {
				state = STATE_ENGINE.VER_ACK_RECEIVE;
				VersionAckTrame verAck = new VersionAckTrame();
				String trame = verAck.generateMessage(netparam, peernode);
				byte[] dataoutput = Utils.hexStringToByteArray(trame);
				outPut.write(dataoutput, 0, dataoutput.length);

			}
		}
		return new Pair<STATE_ENGINE, VersionTrameReceive>(state, versionTrame);

	}

	private STATE_ENGINE readPartialVersion(DataOutputStream outPut,VersionTrameReceive version,final byte[] data) throws IOException {

		VersionAckTrame verAck = new VersionAckTrame();
		String trame = verAck.generateMessage(netParameters, peerNode);
		byte[] dataoutput = Utils.hexStringToByteArray(trame);
		outPut.write(dataoutput, 0, dataoutput.length);
		state = STATE_ENGINE.VER_ACK_RECEIVE;
		return state;

	}
	
	private void getCommandFromData(final byte[] data){
		byte[] msg = new byte[data.length];
		System.arraycopy(data,0, msg, 0, msg.length);
		String message = Utils.bytesToHex(msg);
		if(message.startsWith(NetParameters.MainNet.getMagic())) {
			byte[] buffer = new byte[data.length-4];
			System.arraycopy(data,4, buffer, 0, buffer.length);
			buffer = new byte[12];
			System.arraycopy(data,0,buffer , 0, buffer.length);
		//	String cmd = Utils.bytesToHex(commande);

		}
	}

/*
		public static TrameType findTrameCommande(byte[] data) {
			byte[] commande = new byte[12];
			TrameType trametype = TrameType.ERROR;

			String message = Utils.bytesToHex(data);
			if(message.startsWith(NetParameters.MainNet.getMagic())) {
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
		
	}*/


	@Override
	public Object call() throws Exception {
		byte[] data = new byte[Utils.BUFFER_SIZE];
		int count = 1;
		try{
			socketClient = new Socket(peerNode.getHost(),peerNode.getPort());
			socketClient = new Socket(peerNode.getHost(),peerNode.getPort());
			socketClient.setSoTimeout(Utils.timeoutPeer);
			outPut = new DataOutputStream(socketClient.getOutputStream());
			input = new DataInputStream(socketClient.getInputStream()); 
			VersionTrameReceive versionTrame = null;
			while(state !=STATE_ENGINE.START && count>0) {
				switch(state) {
				case BOOT : state = sendVersion(outPut,netParameters,peerNode);
				break;

				case SENDHEADERS_RECEIVE:
					break;

				case VERSION_SEND :
					break;

				case VERSION_PARTIAL_RECEIVE :

					break;

				case VERSION_RECEIVE:Pair<STATE_ENGINE,VersionTrameReceive> pairReceive = receiveVersion(outPut,netParameters,peerNode,data);
				state = pairReceive._first;
				versionTrame = pairReceive._second;
				if(state == STATE_ENGINE.VERSION_PARTIAL_RECEIVE) {
					count = input.read(data);
					logger.info("["+peerNode.getHost()+"] <IN> "+Utils.bytesToHex(data));
					state = readPartialVersion(outPut,versionTrame,data);
				}

				break;
				default:
					break;

				}
				if(state == STATE_ENGINE.VER_ACK_RECEIVE) {
					state =	sendGetBlock(outPut,netParameters,peerNode);
				}
				count = input.read(data);
				if(count>0) {
					logger.debug("["+peerNode.getHost()+"] <IN> " +Utils.bytesToHex(data) );
					TrameType trametype = Utils.findTrameCommande(data);
					state = whoIsNextStep(trametype);
				}else {
					logger.error("erreur lors de l'echange de version vers "+peerNode.getHost());
					//networkListener.onNodeConnectHasError(this);
				}
			}

		}catch (Exception e) {
			// TODO: handle exception
		}finally {

		}

		return null;
	}



}
