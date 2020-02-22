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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Block;
import com.daloji.blockchain.core.InvType;
import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.commons.database.proxy.LevelDbProxy;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.AddrTrame;
import com.daloji.blockchain.network.trame.BlockTrame;
import com.daloji.blockchain.network.trame.GetBlocksTrame;
import com.daloji.blockchain.network.trame.GetDataTrame;
import com.daloji.blockchain.network.trame.InvTrame;
import com.daloji.blockchain.network.trame.MemPoolTrame;
import com.daloji.blockchain.network.trame.ObjectTrame;
import com.daloji.blockchain.network.trame.PingTrame;
import com.daloji.blockchain.network.trame.PongTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;
import com.daloji.blockchain.network.trame.TrameHeader;
import com.daloji.blockchain.network.trame.VersionAckTrame;
import com.daloji.blockchain.network.trame.VersionTrameMessage;



/**
 * Classe abstraite de gestion de la connexion pour les Threads
 * @author daloji
 *
 */
public abstract class AbstractCallable  implements Callable<Object>{


	private static final Logger logger =  LoggerFactory.getLogger(AbstractCallable.class);

	protected NetworkEventHandler networkListener;

	public BlockChainEventHandler blockChainListener;

	protected PeerNode peerNode;

	protected Socket socketClient;

	protected DataOutputStream outPut;

	protected DataInputStream input;

	protected NetParameters netParameters;

	protected STATE_ENGINE state = STATE_ENGINE.BOOT;

	protected int startHeight = 0;

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
	 * Verifie si un bloc a ete telecharge
	 * @param stacktramHeader
	 * @return
	 */

	public STATE_ENGINE isBlocDownloaded(ArrayDeque<TrameHeader> stacktramHeader) {
		List<Block> listBlock = null;
		if(state== STATE_ENGINE.GETDATA_SEND ||state == STATE_ENGINE.READY ) {
			for(TrameHeader trame:stacktramHeader) {
				if(trame instanceof BlockTrame) {
					if(listBlock == null) {
						listBlock = new ArrayList<>();
					}
					state = STATE_ENGINE.STOP;
					Block block = ((BlockTrame) trame).generateBlock();
					logger.info("Block "+block);
					listBlock.add(block);
				}
			}
		}
		if(listBlock !=null) {
			for(Block block:listBlock) {
				blockChainListener.onBlockReiceve(block);
			}
		}
		return state;
	}


	/**
	 *  Recuperation du type de commande 
	 * @param cmd
	 * @return
	 */
	public  STATE_ENGINE findNExtStepAddrBroadcast(ArrayDeque<TrameHeader> stacktramHeader) {

		List<STATE_ENGINE> stateReady = new ArrayList<>();
		stateReady.add(STATE_ENGINE.BOOT);
		stateReady.add(STATE_ENGINE.VER_ACK_RECEIVE);
		stateReady.add(STATE_ENGINE.VER_ACK_SEND);
		stateReady.add(STATE_ENGINE.VERSION_SEND);
		stateReady.add(STATE_ENGINE.VERSION_RECEIVE);
		Iterator<TrameHeader> iterator= stacktramHeader.iterator();
		while(iterator.hasNext()){
			TrameHeader trame = iterator.next();
			if(listState.containsAll(stateReady)) {
				state = STATE_ENGINE.READY;	
				if(listState.contains(STATE_ENGINE.ADDR_SEND)) {
					state = STATE_ENGINE.START;	
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
					state = STATE_ENGINE.VERSION_RECEIVE;
					listState.add(state);
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
	public  STATE_ENGINE findNExtStepServer(ArrayDeque<TrameHeader> stacktramHeader) {

		List<STATE_ENGINE> stateReady = new ArrayList<>();
		stateReady.add(STATE_ENGINE.BOOT);
		stateReady.add(STATE_ENGINE.VER_ACK_RECEIVE);
		stateReady.add(STATE_ENGINE.VER_ACK_SEND);
		stateReady.add(STATE_ENGINE.VERSION_SEND);
		stateReady.add(STATE_ENGINE.VERSION_RECEIVE);
		Iterator<TrameHeader> iterator= stacktramHeader.iterator();
		while(iterator.hasNext()){
			TrameHeader trame = iterator.next();
			if(listState.containsAll(stateReady)) {
				state = STATE_ENGINE.READY;		

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
	 * envoi de Message VERACK
	 * @param outPut
	 * @param netparam
	 * @param peernode
	 * @return
	 * @throws IOException
	 */
	protected STATE_ENGINE sendVerAck(DataOutputStream outPut,NetParameters netparam,PeerNode peernode) throws IOException {
		state = STATE_ENGINE.VER_ACK_SEND;
		VersionAckTrame verAck = new VersionAckTrame();
		String trame = verAck.generateMessage(netparam, peernode);
		byte[] dataoutput = Utils.hexStringToByteArray(trame);
		outPut.write(dataoutput, 0, dataoutput.length);
		if(logger.isDebugEnabled()) {
			logger.debug("<OUT>  Verack " +trame);
		}
		return state;
	}

	/**
	 * Reponse aux messages recus
	 * @param arrayTrame
	 * @param outPut
	 * @param netparam
	 * @param peernode
	 * @throws IOException
	 */
	protected void replyAllRequest(ArrayDeque<TrameHeader> arrayTrame,DataOutputStream outPut,NetParameters netparam,PeerNode peernode) throws IOException {
		List<TrameHeader> list	=new ArrayList<TrameHeader>();	
		List<Inventory>  listBlock = new ArrayList<Inventory>();
		List<Inventory>  listTx = new ArrayList<Inventory>();
		if(arrayTrame !=null) {
			for(TrameHeader trame:arrayTrame) {
				if(trame instanceof PingTrame) {
					list.add(trame);
					PongTrame pong = new PongTrame();
					pong.setNonce(((PingTrame) trame).getNonce());
					String pongtrame = pong.generateMessage(netParameters, peerNode);
					byte[] dataoutput = Utils.hexStringToByteArray(pongtrame);
					outPut.write(dataoutput, 0, dataoutput.length);
					if(logger.isDebugEnabled()) {
						logger.debug("<OUT>  Pong " +pongtrame);
					}
				}
				if(trame instanceof AddrTrame) {
					networkListener.onAddresseReceive(((AddrTrame) trame).getListAddr());
				}
				if(trame instanceof GetBlocksTrame) {
					//TODO	
				}
				if(trame instanceof GetDataTrame) {
					replyGetData(trame);		
				}
				if(trame instanceof InvTrame) {
					List<Inventory> listinventory = ((InvTrame) trame).getListinv();
					for(Inventory inventory:listinventory) {
						if(InvType.MSG_TX.equals(inventory.getType())) {
							listTx.add(inventory);
						}
						if(inventory.getType() ==InvType.MSG_BLOCK) {
							listBlock.add(inventory);
						}
					}
				}

			}
			arrayTrame.removeAll(list);
			if(!listBlock.isEmpty()) {
				blockChainListener.onBlockHeaderReceive(listBlock);
			}
			if(!listTx.isEmpty()){
				sendGetData(outPut, netparam, peernode, listTx);	
				arrayTrame.removeAll(listTx);
			}


		}
	}

	/**
	 * Reponse a un message GetData
	 * @param trameGetData
	 * @throws IOException
	 */
	private void replyGetData(TrameHeader trameGetData) throws IOException {
		List<Inventory> listInv = ((GetDataTrame) trameGetData).getListInv();
		for(Inventory inv:listInv) {
			Block block = LevelDbProxy.getInstance().findBlock(inv.getHash());
			if(block!=null) {
				BlockTrame blockTrame = Block.buildBlockTrame(block);
				String trame = blockTrame.generateMessage(netParameters, peerNode);
				byte[] data = Utils.hexStringToByteArray(trame);
				outPut.write(data, 0, data.length);	
				if(logger.isDebugEnabled()) {
					logger.debug("<REPLY>  GetData :"+Utils.bytesToHex(data));
				}

			}

		}
	}

	/**
	 * Envoi de message GETBLOCK
	 * @param outPut
	 * @param netparam
	 * @param peernode
	 * @param hash
	 * @return
	 * @throws IOException
	 */
	protected STATE_ENGINE sendGetBlock(DataOutputStream outPut,NetParameters netparam,PeerNode peernode,String hash) throws IOException {
		state = STATE_ENGINE.GETBLOCK_SEND;
		//construction de la blockchain
		GetBlocksTrame getblock = new GetBlocksTrame();
		getblock.setHashToLoad(hash);
		String trame = getblock.generateMessage(netParameters, peerNode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length);	
		if(logger.isDebugEnabled()) {
			logger.debug("<OUT>  GetBlocks :"+Utils.bytesToHex(data));
		}
		return state;
	}


	protected STATE_ENGINE sendMemPool(DataOutputStream outPut,NetParameters netparam,PeerNode peernode) throws IOException {
		//construction de la blockchain
		MemPoolTrame mempool = new MemPoolTrame();
		String trame = mempool.generateMessage(netParameters, peerNode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length);	
		if(logger.isDebugEnabled()) {
			logger.debug("<OUT>  MemPool :"+Utils.bytesToHex(data));
		}
		return state;
	}
	/**
	 * envoi Get DATA Trame
	 * @param outPut
	 * DataOutputStream
	 * @param netparam
	 * NetParameters
	 * @param peernode
	 * peer
	 * @return STATE_ENGINE
	 * @throws IOException
	 */

	protected STATE_ENGINE sendGetData(DataOutputStream outPut,NetParameters netparam,PeerNode peernode,Inventory inv) throws IOException {
		state = STATE_ENGINE.GETDATA_SEND;
		GetDataTrame getData = new GetDataTrame(inv);
		String trame = getData.generateMessage(netParameters, peerNode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length);	
		if(logger.isDebugEnabled()) {
			logger.debug("<OUT>  GetData :"+Utils.bytesToHex(data));
		}
		return state;
	}


	/**
	 * envoi Get DATA Trame
	 * @param outPut
	 * DataOutputStream
	 * @param netparam
	 * NetParameters
	 * @param peernode
	 * peer
	 * @return STATE_ENGINE
	 * @throws IOException
	 */

	protected STATE_ENGINE sendGetData(DataOutputStream outPut,NetParameters netparam,PeerNode peernode,List<Inventory> listInv) throws IOException {
		state = STATE_ENGINE.GETDATA_SEND;
		GetDataTrame getData = new GetDataTrame(listInv);
		String trame = getData.generateMessage(netParameters, peerNode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length);	
		if(logger.isDebugEnabled()) {
			logger.debug("<OUT>  GetData :"+Utils.bytesToHex(data));
		}
		return state;
	}

	/**
	 * envoi Version Trame
	 * @param outPut
	 * DataOutputStream
	 * @param netparam
	 * NetParameters
	 * @param peernode
	 * peer
	 * @return STATE_ENGINE
	 * @throws IOException
	 */
	protected STATE_ENGINE sendVersion(DataOutputStream outPut,NetParameters netparam,PeerNode peernode) throws IOException {

		STATE_ENGINE state = STATE_ENGINE.VERSION_SEND;
		VersionTrameMessage version = new VersionTrameMessage(true);
		version.setStartHeigth(startHeight);
		String trame = version.generateMessage(netparam, peernode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length);
		if(logger.isDebugEnabled()) {
			logger.debug("["+peernode.getHost()+"]" +" <OUT>  Version " +trame);
		}
		return state;
	}


	/**
	 * send Addr 
	 * @param outPut
	 * @param netparam
	 * @param peernode
	 * @return
	 * @throws IOException
	 */
	protected STATE_ENGINE sendAddr(DataOutputStream outPut,NetParameters netparam,PeerNode peernode) throws IOException {

		STATE_ENGINE state = STATE_ENGINE.ADDR_SEND;
		AddrTrame addr = new AddrTrame(true);
		String trame = addr.generateMessage(netparam, peernode);
		byte[] data = Utils.hexStringToByteArray(trame);
		outPut.write(data, 0, data.length);
		if(logger.isDebugEnabled()) {
			logger.debug("["+peernode.getHost()+"]" +" <OUT>  Addr " +trame);
		}
		return state;
	}

}
