package com.daloji.blockchain.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.Inventory;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.listener.BlockChainEventHandler;
import com.daloji.blockchain.network.listener.NetworkEventHandler;
import com.daloji.blockchain.network.peers.PeerNode;
import com.daloji.blockchain.network.trame.DeserializerTrame;
import com.daloji.blockchain.network.trame.STATE_ENGINE;
import com.daloji.blockchain.network.trame.TrameHeader;


public class BlockChainHandler  extends AbstractCallable{

	private static final Logger logger = LoggerFactory.getLogger(BlockChainHandler.class);

	private STATE_ENGINE state = STATE_ENGINE.BOOT;

	private  TrameHeader lastTrame;

	private volatile Timer timer = new Timer(true);

	private Inventory inventory;

	private volatile boolean isStopping = false;

	public BlockChainHandler(DataOutputStream dataOut,DataInputStream dataInput){
		outPut = dataOut;
		input = dataInput;
	}

	public BlockChainHandler(NetworkEventHandler networkListener,BlockChainEventHandler blockchaiListener,NetParameters netparam,PeerNode peerNode,Inventory inventory){
		super();
		this.peerNode = peerNode;
		this.networkListener = networkListener;
		this.blockChainListener = blockchaiListener;
		this.netParameters = netparam;
		this.inventory = inventory;
	}

	@Override
	public Object call() throws Exception {
		logger.info("start BlockChainHandler");
		timer.schedule(new TimeOutTimer(Thread.currentThread()), Utils.TIMEOUT_BLOCKCHAIN_THREAD);
		byte[] data = new byte[Utils.BUFFER_SIZE];
		try {
			socketClient = new Socket(peerNode.getHost(),peerNode.getPort());
			socketClient.setSoTimeout(Utils.timeoutPeer);
			outPut = new DataOutputStream(socketClient.getOutputStream());
			input = new DataInputStream(socketClient.getInputStream()); 
			int count = 1;
			listState.add(STATE_ENGINE.BOOT);
			while(state !=STATE_ENGINE.STOP && count!=-1 && !isStopping ) {
				switch(state) {
				case BOOT : state = sendVersion(outPut,netParameters,peerNode);
				listState.add(state);
				break;
				case VER_ACK_RECEIVE:state = sendVerAck(outPut,netParameters,peerNode);
				listState.add(state);
				break;
				case GETDATA_SEND : state = sendGetData(outPut, netParameters, peerNode,inventory);
				listState.add(state);
				break;
				case GETBLOCK_SEND :
					break;
				}

				count = input.read(data);
				if(count > 0) {
					logger.info(Utils.bytesToHex(data));
					ArrayDeque<TrameHeader> deserialize = DeserializerTrame.getInstance().deserialise(lastTrame,data,peerNode);
					if(deserialize.size()>0) {
						TrameHeader trame = deserialize.getLast();
						lastTrame = trame;
						state = findNExtStepBlock(deserialize);
						state = isBlocDownloaded(deserialize);
					}
				}
			}

		}catch (Exception e) {
			logger.error(e.getMessage());	
		}
		timer.cancel();
		timer.purge();
		logger.info("End download Block ");	
		if(networkListener !=null) {
			networkListener.onNodeDisconnected(this);
		}
		return null;
	}

	class TimeOutTimer extends TimerTask {
		Thread thread;

		TimeOutTimer(Thread thread) {
			this.thread = thread;
		}

		@Override
		public void run() {
			logger.info("Timer handle " +thread.getName() );
			if (thread != null &&thread.isAlive()) {
				isStopping = true;
				thread.interrupt();
			}
		}
	}


}
