package com.daloji.core.blockchain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import com.daloji.core.blockchain.net.DnsLookUp;
import com.daloji.core.blockchain.net.IPVersion;
import com.daloji.core.blockchain.net.NetParameters;
import com.daloji.core.blockchain.net.NetworkOrchestrator;
import com.daloji.core.blockchain.net.PeerNode;
import com.daloji.core.blockchain.peers.MessageProxy;
import com.daloji.core.blockchain.peers.VersionTrameMessage;
import com.google.common.hash.Hashing;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args ) throws Exception
	{ 
		NetworkOrchestrator networkOrch = new NetworkOrchestrator();
		networkOrch.onStart();
		String payloda ="2F64616C6F6A693A302E302E312F"; 
		System.out.println(Utils.hexToAscii(payloda));
	}

}
