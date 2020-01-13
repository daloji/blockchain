package com.daloji.blockchain.network.trame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.NetParameters;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class NetworkMessagingProxy implements TrameSend {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(NetworkMessagingProxy.class);

	private static final NetworkMessagingProxy instance = new NetworkMessagingProxy();

	public static NetworkMessagingProxy getInstance()
	{
		return instance;
	}




	@Override
	public void sendVersionMessage(NetParameters netparameter, PeerNode peer) {
		logger.info("send message version to Node " );
		logger.info("Peer : "+peer.getHost());
		logger.info("port : "+peer.getPort());

		VersionTrameMessage version = new VersionTrameMessage(true);
		String message = version.generateMessage(netparameter.MainNet, peer);

		try (Socket socket = new Socket(peer.getHost(), 8333)) {
			byte[] dataoutput = Utils.hexStringToByteArray(message);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.write(dataoutput, 0, dataoutput.length);
			DataInputStream dis=new DataInputStream(socket.getInputStream());  
			byte[] data = new byte[1024];
			int count = dis.read(data);
			if(count>0) {
				version.receiveMessage(netparameter, data);
			}
			String text=Utils.bytesToHex(data);
			logger.info("Message recu : "+text);
			System.out.println(count);
			System.out.println(text);

		} catch (UnknownHostException ex) {

			System.out.println("Server not found: " + ex.getMessage());

		} catch (IOException ex) {

			System.out.println("I/O error: " + ex.getMessage());
		}
		
	}


}
