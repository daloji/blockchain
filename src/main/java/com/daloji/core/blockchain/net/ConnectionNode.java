package com.daloji.core.blockchain.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import com.daloji.core.blockchain.Utils;
import com.daloji.core.blockchain.peers.VersionTrameMessage;
import com.daloji.core.blockchain.peers.VersionTrameReceive;

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
		Socket socket =null;
		try{
			socket = new Socket(peerNode.getHost(),peerNode.getPort());
			socket.setSoTimeout(Utils.timeoutPeer);
			VersionTrameMessage version = new VersionTrameMessage();
			String trame = version.generateMessage(netParameters, peerNode);
			byte[] dataoutput = Utils.hexStringToByteArray(trame);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.write(dataoutput, 0, dataoutput.length);
			DataInputStream dis=new DataInputStream(socket.getInputStream()); 
			networkListener.onNodeConnected(this);
			int count = dis.read(data);
			while(count>0) {
				VersionTrameReceive versionTrame = version.receiveMessage(netParameters, data);
				count = dis.read(data);
			
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



}
