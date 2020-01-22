package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Callable;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.GetBlocksTrame;
import com.daloji.blockchain.network.trame.GetDataTrame;
import com.daloji.blockchain.network.trame.ObjectTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;
import com.daloji.blockchain.network.trame.TrameHeader;
import com.daloji.blockchain.network.trame.VersionAckTrame;
import com.daloji.blockchain.network.trame.VersionTrameMessage;

import ch.qos.logback.classic.Logger;


/**
 * Classe abstraite de gestion de la connexion pour les Threads
 * @author daloji
 *
 */
public abstract class AbstractCallable  implements Callable<Object>{


	private static final Logger logger = (Logger) LoggerFactory.getLogger(AbstractCallable.class);

	protected NetworkEventHandler networkListener;

	public BlockChainEventHandler blockChainListener;

	protected PeerNode peerNode;

	protected Socket socketClient;

	protected DataOutputStream outPut;

	protected DataInputStream input;

	protected NetParameters netParameters;

	protected STATE_ENGINE state = STATE_ENGINE.BOOT;


	protected List<STATE_ENGINE> listState = new ArrayList<>();

	protected Stack<ObjectTrame> pileCommand = new Stack<ObjectTrame>();



	public NetworkEventHandler getNetworkListener() {
		return networkListener;
	}

	public void setNetworkListener(NetworkEventHandler networkListener) {
		this.networkListener = networkListener;
	}

	public BlockChainEventHandler getBlockChainListener() {
		return blockChainListener;
	}

	public void setBlockChainListener(BlockChainEventHandler blockChainListener) {
		this.blockChainListener = blockChainListener;
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

	public NetParameters getNetParameters() {
		return netParameters;
	}

	public void setNetParameters(NetParameters netParameters) {
		this.netParameters = netParameters;
	}

	public STATE_ENGINE getState() {
		return state;
	}

	public void setState(STATE_ENGINE state) {
		this.state = state;
	}


	public STATE_ENGINE  getData(Stack<ObjectTrame> stack) {
		STATE_ENGINE state = STATE_ENGINE.GETDATA_SEND;
		if(stack!=null) {
			if(!stack.isEmpty()) {
				for(int i=0;i<stack.size();i++) {


				}
			}
		}
		return state;
	}
	
	/**
	 *  Recuperation du type de commande 
	 * @param cmd
	 * @return
	 */
	public  STATE_ENGINE findNExtStepBlock(ArrayDeque<TrameHeader> stacktramHeader) {

		List<STATE_ENGINE> stateReady = new ArrayList<>();
		stateReady.add(STATE_ENGINE.BOOT);
		stateReady.add(STATE_ENGINE.VER_ACK_RECEIVE);
		stateReady.add(STATE_ENGINE.VER_ACK_SEND);
		stateReady.add(STATE_ENGINE.VERSION_SEND);
		stateReady.add(STATE_ENGINE.VERSION_RECEIVE);


		STATE_ENGINE localstate = STATE_ENGINE.ERROR;
		Iterator<TrameHeader> iterator= stacktramHeader.iterator();
		while(iterator.hasNext()){
			TrameHeader trame = iterator.next();
			if(listState.containsAll(stateReady)) {
				if(listState.contains(STATE_ENGINE.READY)) {
					if(listState.contains(STATE_ENGINE.GETDATA_SEND)) {
						state = STATE_ENGINE.READY;		
					}else {
						state = STATE_ENGINE.GETDATA_SEND;	
					}
				}else {
					state = STATE_ENGINE.READY;	
					listState.add(state);	
				}

			}else {

				if(trame instanceof VersionAckTrame) {
					state = STATE_ENGINE.VER_ACK_RECEIVE;
					listState.add(state);
				}
				if(trame instanceof VersionTrameMessage) {
					if(trame.isPartialTrame()) {
						state = STATE_ENGINE.PARTIAL_TRAME;
					}else {
						state = STATE_ENGINE.VERSION_RECEIVE;
						listState.add(state);

					}

				}
			}

		}


		return state;
	}


	/**
	 *  Recuperation du type de commande 
	 * @param cmd
	 * @return
	 */
	public  STATE_ENGINE findNExtStep(ArrayDeque<TrameHeader> stacktramHeader) {

		List<STATE_ENGINE> stateReady = new ArrayList<>();
		stateReady.add(STATE_ENGINE.BOOT);
		stateReady.add(STATE_ENGINE.VER_ACK_RECEIVE);
		stateReady.add(STATE_ENGINE.VER_ACK_SEND);
		stateReady.add(STATE_ENGINE.VERSION_SEND);
		stateReady.add(STATE_ENGINE.VERSION_RECEIVE);


		STATE_ENGINE localstate = STATE_ENGINE.ERROR;
		Iterator<TrameHeader> iterator= stacktramHeader.iterator();
		while(iterator.hasNext()){
			TrameHeader trame = iterator.next();
			if(listState.containsAll(stateReady)) {
				if(listState.contains(STATE_ENGINE.READY)) {
					if(listState.contains(STATE_ENGINE.GETBLOCK_SEND)) {
						state = STATE_ENGINE.READY;		
					}else {
						state = STATE_ENGINE.GETBLOCK_SEND;	
					}
					
				}else {
					state = STATE_ENGINE.READY;	
					listState.add(state);	
				}

			}else {

				if(trame instanceof VersionAckTrame) {
					state = STATE_ENGINE.VER_ACK_RECEIVE;
					listState.add(state);
				}
				if(trame instanceof VersionTrameMessage) {
					if(trame.isPartialTrame()) {
						state = STATE_ENGINE.PARTIAL_TRAME;
					}else {
						state = STATE_ENGINE.VERSION_RECEIVE;
						listState.add(state);

					}

				}
			}

		}


		return state;
	}		

	protected STATE_ENGINE sendVerAck(DataOutputStream outPut,NetParameters netparam,PeerNode peernode) throws IOException {
		state = STATE_ENGINE.VER_ACK_SEND;
		VersionAckTrame verAck = new VersionAckTrame();
		String trame = verAck.generateMessage(netparam, peernode);
		byte[] dataoutput = Utils.hexStringToByteArray(trame);
		outPut.write(dataoutput, 0, dataoutput.length);
		logger.info("<OUT>  Verack " +trame);
		return state;
	}



	protected STATE_ENGINE sendGetBlock(DataOutputStream outPut,NetParameters netparam,PeerNode peernode) throws IOException {
		state = STATE_ENGINE.GETBLOCK_SEND;
		//construction de la blockchain
		GetBlocksTrame getblock = new GetBlocksTrame();
		String trame = getblock.generateMessage(netParameters, peerNode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length);	
		logger.info("<OUT>  GetBlocks :"+Utils.bytesToHex(data));
		return state;
	}


	protected STATE_ENGINE sendGetData(DataOutputStream outPut,NetParameters netparam,PeerNode peernode,Inventory inv) throws IOException {
		state = STATE_ENGINE.GETDATA_SEND;
		//construction de la blockchain
		GetDataTrame getData = new GetDataTrame(inv.getHash());
		String trame = getData.generateMessage(netParameters, peerNode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length);	
		logger.info("<OUT>  GetData :"+Utils.bytesToHex(data));
		return state;
	}



	protected STATE_ENGINE sendVersion(DataOutputStream outPut,NetParameters netparam,PeerNode peernode) throws IOException {

		STATE_ENGINE state = STATE_ENGINE.VERSION_SEND;
		VersionTrameMessage version = new VersionTrameMessage(true);
		String trame = version.generateMessage(netParameters, peerNode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length);
		logger.info("["+peernode.getHost()+"]" +" <OUT>  Version " +trame);
		return state;
	}

	/*
	protected Stack<ObjectTrame>  receiveInventory(final byte[] data) throws IOException {
		Stack<ObjectTrame> stakcommand = new Stack<ObjectTrame>();
		byte[] copydata = new byte[data.length];
		System.arraycopy(data,0, copydata, 0, data.length);
		String message = Utils.bytesToHex(copydata);
		if(!message.startsWith(NetParameters.MainNet.getMagic())) { 
			String padding = Utils.generateNonce(16);
			//ajout padding avant le message probleme de deserialisation
			message = message + padding;
			Pair<String,Inv> inv = extractInvMessage(message);

		}else {
			stakcommand = processMessage(data);
		}
		return stakcommand;

	}
	 *


	private Pair<String,Inv> extractInvMessage(String msg){
		Pair<String,Inv> retourInv = null;
		String length = msg.substring(0, 8);
		long decimal = Utils.little2big(length);
		//taille de l'inv Variable length string
		int sizelenght =1;
		try {
			//if(decimal<msg.length()) {
			//msg = msg.substring(16, (int)(decimal*2));
			msg = msg.substring(16, msg.length());
			String len = msg.substring(0,2);
			long size = Integer.parseInt(len,16);
			//FD value
			if(size<Utils.FD_HEXA) {
				len = msg.substring(0,2);
				msg = msg.substring(2, msg.length());
			}else if(size>=Utils.FD_HEXA && size<=Utils.FFFF_HEXA) {
				len = msg.substring(2,6);
				msg = msg.substring(6, msg.length());
				len=Utils.StrLittleEndian(len);
				size =Integer.parseInt(len,16);

			}else if(size>Utils.FFFF_HEXA) {
				len = msg.substring(2,10);
				len=Utils.StrLittleEndian(len);
				size =Integer.parseInt(len,16);
				msg = msg.substring(10, msg.length());
			}
			Inv inv =new Inv();
			List<Inventory> listinv = new ArrayList<Inventory>();

			for(int i=0;i<size;i++) {

				String type = msg.substring(0,2);
				int decimalType=Integer.parseInt(type,16);
				msg = msg.substring(8, msg.length());
				Inventory inventory = new Inventory();
				String hash = "";
				msg = Utils.paddingHexa(msg, 64);
				hash = msg.substring(0, 64);
				switch (decimalType) {
				case 0:
					inventory.setType(InvType.ERROR);
					inventory.setHash(hash);
					break;
				case 1: 
					inventory.setType(InvType.MSG_TX);
					inventory.setHash(hash);

					break;
				case 2: 
					inventory.setType(InvType.MSG_BLOCK);
					inventory.setHash(hash);
					sendGetData(outPut, netParameters, peerNode, inventory);

					// *************************** TEST *************************************
					byte[] datas = new byte[Utils.BUFFER_SIZE];
					int count = input.read(datas); 
					if(count>0) {
						Stack<ObjectTrame> stack = processMessage(datas);
					}

						// *************************** TEST *************************************



					//blockChainListener.onBlockHeaderReceive(inventory);
					break;
				case 3: 
					inventory.setType(InvType.MSG_FILTERED_BLOCK);
					inventory.setHash(hash);
					break;
				case 4: 
					inventory.setType(InvType.MSG_CMPCT_BLOCK);
					inventory.setHash(hash);
					break;

				default:
					break;
				}

				if(msg.length()>64) {
					msg = msg.substring(64, msg.length());
				}else {
					msg =Utils.paddingHexa(msg, 64);
				}
				listinv.add(inventory);

			}
			inv.setPayload(msg);
			inv.setListInventory(listinv);
			retourInv = new Pair<String, Inv>(msg, inv);

			//}

		}catch (Exception e) {
			retourInv = new Pair<String, Inv>(msg, null);		
		}

		return retourInv;
	}
/*
	protected Stack<ObjectTrame> processMessage(final byte[] data) throws IOException{
		Stack<ObjectTrame> stakcommand = new Stack<ObjectTrame>();
		if(data!=null) {
			byte[] copydata = new byte[data.length];
			System.arraycopy(data,0, copydata, 0, data.length);
			String message = Utils.bytesToHex(copydata);
			TrameType trameType = TrameType.START;
			while(!Utils.allZero(Utils.hexStringToByteArray(message)) && (trameType != TrameType.ERROR)) {
				if(message.startsWith(NetParameters.MainNet.getMagic())) { 
					String cmd = message.substring(8, 32);
					trameType = findCommande(cmd);
					if(trameType == TrameType.VERSION) {
						logger.info("[" +peerNode.getHost() + "]"+"<IN>  " +TrameType.VERSION.name() +" " +message);
						message = message.substring(32, message.length());
						String length = message.substring(0, 8);
						long decimal = Utils.little2big(length);
						decimal = decimal*2;
						message = message.substring(16, message.length());
						String payload = message.substring(0,(int) decimal);
						byte [] bpayload = Utils.hexStringToByteArray(payload);
						ObjectTrame objetTrame = new ObjectTrame();
						if(Utils.allZero(bpayload)) {
							objetTrame.setPartialTrame(true);
							byte[] datainput = new byte[Utils.BUFFER_SIZE];
							int count = input.read(datainput);
							if(count>0) {
								objetTrame.setPayload(Utils.bytesToHex(datainput));
							}
						}else {
							objetTrame.setPayload(payload);	
						}
						objetTrame.setType(trameType);
						stakcommand.add(objetTrame);
						message = message.substring((int)decimal,message.length());

					}
					if(trameType == TrameType.VERACK) {
						logger.info("[" +peerNode.getHost() + "]"+"<IN>  " +TrameType.VERACK.name() +" " +message);
						message = message.substring(48, message.length());
						ObjectTrame objetTrame = new ObjectTrame();
						objetTrame.setPayload("00000");
						objetTrame.setType(trameType);
						stakcommand.add(objetTrame);

					}

					if(trameType == TrameType.SENDHEADERS) {
						logger.info("[" +peerNode.getHost() + "]"+"<IN>  " +TrameType.SENDHEADERS.name() +" " +message);
						message = message.substring(32, message.length());
						message = message.substring(16, message.length());
						ObjectTrame objetTrame = new ObjectTrame();
						objetTrame.setType(trameType);
						stakcommand.add(objetTrame);
					}

					if(trameType == TrameType.FEELFILTER) {
						logger.info("[" +peerNode.getHost() + "]"+"<IN>  " +TrameType.FEELFILTER.name() +" " +message);
						message = message.substring(32, message.length());
						String length = message.substring(0, 8);
						long decimal = Utils.little2big(length);
						message = message.substring(16, message.length());
						message = message.substring((int)decimal*2, message.length());
						logger.info("["+peerNode.getHost()+"] <IN> FEELFILTER "+message);
						ObjectTrame objetTrame = new ObjectTrame();
						objetTrame.setType(trameType);
						stakcommand.add(objetTrame);
					}


					if(trameType == TrameType.SENDCMPCT) {
						logger.info("[" +peerNode.getHost() + "]"+"<IN>  " +TrameType.SENDCMPCT.name() +" " +message);
						message = message.substring(32, message.length());
						String length = message.substring(0, 8);
						long decimal = Utils.little2big(length);
						decimal = decimal*2;
						message = message.substring(16, message.length());
						String payload = message.substring(0,(int) decimal);
						byte [] bpayload = Utils.hexStringToByteArray(payload);
						ObjectTrame objetTrame = new ObjectTrame();
						if(Utils.allZero(bpayload)) {
							objetTrame.setPartialTrame(true);
							byte[] datainput = new byte[Utils.BUFFER_SIZE];
							int count = input.read(datainput);
							if(count>0) {
								objetTrame.setPayload(Utils.bytesToHex(datainput));
								message = Utils.bytesToHex(datainput);

							}
						}else {
							objetTrame.setPayload(payload);
						}
						message = message.substring((int)decimal,message.length());
						objetTrame.setType(trameType);
						stakcommand.add(objetTrame);
					}

					if(trameType == TrameType.PING) {
						logger.info("[" +peerNode.getHost() + "]"+"<IN>  " +TrameType.PING.name() +" " +message);
						message = message.substring(32, message.length());
						String length = message.substring(0, 8);
						long decimal = Utils.little2big(length);
						decimal = decimal*2;
						message = message.substring(16, message.length());
						String payload = message.substring(0,(int) decimal);
						byte [] bpayload = Utils.hexStringToByteArray(payload);
						ObjectTrame objetTrame = new ObjectTrame();
						if(Utils.allZero(bpayload)) {
							objetTrame.setPartialTrame(true);
							byte[] datainput = new byte[Utils.BUFFER_SIZE];
							int count = input.read(datainput);
							if(count>0) {
								objetTrame.setPayload(Utils.bytesToHex(datainput));
								message = Utils.bytesToHex(datainput);
							}
						}
						objetTrame.setPayload(payload);
						objetTrame.setType(trameType);
						stakcommand.add(objetTrame);
						message = message.substring((int)decimal,message.length());
					}

					if(trameType == TrameType.ADDR) {
						logger.info("[" +peerNode.getHost() + "]"+"<IN>  " +TrameType.ADDR.name() +" " +message);
						message = message.substring(32, message.length());
						String length = message.substring(0, 8);
						long decimal = Utils.little2big(length);
						decimal = decimal*2;
						message = message.substring(16, message.length());
						String payload = message.substring(0,(int) decimal);
						byte [] bpayload = Utils.hexStringToByteArray(payload);
						ObjectTrame objetTrame = new ObjectTrame();
						if(Utils.allZero(bpayload)) {
							objetTrame.setPartialTrame(true);
							byte[] datainput = new byte[Utils.BUFFER_SIZE];
							int count = input.read(datainput);
							if(count>0) {
								objetTrame.setPayload(Utils.bytesToHex(datainput));
								message = Utils.bytesToHex(datainput);
							}
						}
						objetTrame.setPayload(payload);
						objetTrame.setType(trameType);
						stakcommand.add(objetTrame);
						message = message.substring((int)decimal,message.length());
					}
					if(trameType == TrameType.GETHEADERS) {
						logger.info("[" +peerNode.getHost() + "]"+"<IN>  " +TrameType.GETHEADERS.name() +" " +message);
						message = message.substring(32, message.length());
						message = message.substring(24, message.length());
						String length = message.substring(0, 2);
						int size = Integer.parseInt(length,16);
						message = message.substring(2, message.length());
						int end =(size+1)*64;
						String payload = message.substring(0,end);
						ObjectTrame objetTrame = new ObjectTrame();
						objetTrame.setPayload(payload);
						objetTrame.setType(trameType);
						stakcommand.add(objetTrame);
						message = message.substring(end,message.length());

					}

					if(trameType == TrameType.INV) {
						logger.info("[" +peerNode.getHost() + "]"+"<IN>  " +TrameType.INV.name() +" " +message);
						message = message.substring(32, message.length());
						String length = message.substring(0, 8);
						long decimal = Utils.little2big(length);
						Pair<String,Inv> inv = extractInvMessage(message);
						if(inv._second != null) {
							InvTrameObject invtramObj = new InvTrameObject();
							invtramObj.setInventory(inv._second);
							stakcommand.add(invtramObj);
						}
						if(inv._first !=null) {
							message = inv._first;
						}
						state = STATE_ENGINE.INV_RECEIVE;

					}
					if(trameType == TrameType.BLOCK) {
						logger.info("dddddddddddddddd");
					}
					if(trameType == TrameType.ERROR) {

					}

				}else {
					logger.error(message);
					trameType = TrameType.ERROR	;
				}
			}
		}

		return stakcommand;
	}

	 */
}
