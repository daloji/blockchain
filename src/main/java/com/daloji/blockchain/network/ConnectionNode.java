package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

import com.daloji.blockchain.core.Messages;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.STATE_ENGINE;
import com.daloji.blockchain.network.trame.TrameType;
import com.daloji.blockchain.network.trame.VersionAckTrame;
import com.daloji.blockchain.network.trame.VersionTrameMessage;
import com.daloji.blockchain.network.trame.VersionTrameReceive;


/**
 * Objet Callable qui prend en charge les connections vers les autres 
 *  Noeuds en mode client
 *  
 * @author daloji
 *
 */
public class ConnectionNode  implements Callable<Object>{

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
		Socket socket =null;
		try{
			socket = new Socket(peerNode.getHost(),peerNode.getPort());
			socket.setSoTimeout(Utils.timeoutPeer);
			VersionTrameMessage version = new VersionTrameMessage();
			String trame = version.generateMessage(netParameters, peerNode);
			byte[] dataoutput = Utils.hexStringToByteArray(trame);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.write(dataoutput, 0, dataoutput.length);
			state = STATE_ENGINE.VERSION_SEND;
			DataInputStream dis = new DataInputStream(socket.getInputStream()); 
			networkListener.onNodeConnected(this);
			int count = dis.read(data);
			while(count>0) {
				switch(state){

				case VERSION_SEND:versionTrame  = openSessionNode(out,netParameters,peerNode,version,data);
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
					break;


				case SENDHEADERS_SEND:
					break;

				}
				/*
				if(versionTrame == null) {
					//send Version Trame
					versionTrame  = openSessionNode(out,netParameters,peerNode,version,data);

				}else {

				}

				count = dis.read(data);
				System.out.println(Utils.bytesToHex(data));

				 */
				count = dis.read(data);
				TrameType trametype = Utils.findTrameCommande(data);
				state = whoIsNextStep(trametype);
				System.out.println(Utils.bytesToHex(data));
			}

		}catch (Exception e) {
			System.err.println(e.getMessage());		
		}finally {
			if(socket !=null) {
				socket.close();
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
			state = STATE_ENGINE.VER_ACK_SEND;

		}

		return  versionTrame;
	}


}
