package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.GetBlocksTrame;
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
public class ConnectionNode  implements Callable<Object>{

	private static final Logger logger = (Logger) LoggerFactory.getLogger(ConnectionNode.class);


	private NetworkHandler networkListener;

	private PeerNode peerNode;

	private Socket socketClient;

	private DataOutputStream outPut;

	private DataInputStream input;

	private NetParameters netParameters;

	private STATE_ENGINE state = STATE_ENGINE.START;

	public ConnectionNode(NetworkHandler networkListener,NetParameters netparam,PeerNode peerNode) {
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
					}
					//construction de la blockchaine
					GetBlocksTrame getblock = new GetBlocksTrame();
					String message = getblock.generateMessage(netParameters, peerNode);
					sendGetBlock(outPut, netParameters, peerNode, Utils.hexStringToByteArray(message));
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
					logger.error("erreur lors de l'echange de version vers"+peerNode.getHost());
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

	
	private void sendGetBlock(DataOutputStream outPut,NetParameters netparam,PeerNode peernode,byte[] data) throws IOException {
		if(data!=null) {
			outPut.write(data, 0, data.length);
		}
	}
	
	

}
