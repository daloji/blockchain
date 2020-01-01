package com.daloji.core.blockchain.peers;

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

import com.daloji.core.blockchain.Utils;
import com.daloji.core.blockchain.net.DnsLookUp;
import com.daloji.core.blockchain.net.NetParameters;
import com.daloji.core.blockchain.net.PeerNode;

import ch.qos.logback.classic.Logger;

public class MessageProxy implements MessageSend {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(MessageProxy.class);

	private static final MessageProxy instance = new MessageProxy();

	public static MessageProxy getInstance()
	{
		return instance;
	}



	public byte[] decodeHexString(String hexString) {
		if (hexString.length() % 2 == 1) {
			throw new IllegalArgumentException(
					"Invalid hexadecimal String supplied.");
		}

		byte[] bytes = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
		}
		return bytes;
	}


	public byte hexToByte(String hexString) {
		int firstDigit = toDigit(hexString.charAt(0));
		int secondDigit = toDigit(hexString.charAt(1));
		return (byte) ((firstDigit << 4) + secondDigit);
	}
	private int toDigit(char hexChar) {
		int digit = Character.digit(hexChar, 16);
		if(digit == -1) {
			throw new IllegalArgumentException(
					"Invalid Hexadecimal Character: "+ hexChar);
		}
		return digit;
	}



	@Override
	public void sendVersionMessage(NetParameters netparameter, PeerNode peer) {
		logger.info("send message version to Node " );
		logger.info("Peer : "+peer.getHost());
		logger.info("port : "+peer.getPort());

		VersionTrameMessage version = new VersionTrameMessage();
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
