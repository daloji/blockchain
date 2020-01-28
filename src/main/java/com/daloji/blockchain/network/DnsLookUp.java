package com.daloji.blockchain.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.LoggerFactory;

import com.daloji.blockchain.core.commons.Pair;
import com.daloji.blockchain.core.commons.Retour;
import com.daloji.blockchain.core.utils.BlockChainWareHouseThreadFactory;
import com.daloji.blockchain.core.utils.Utils;
import com.daloji.blockchain.network.peers.PeerNode;

import ch.qos.logback.classic.Logger;

public class DnsLookUp {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(DnsLookUp.class);

	protected final ReentrantLock lock = BlockChainWareHouseThreadFactory.lockThisObject(DnsLookUp.class);
	
	private static final DnsLookUp instance = new DnsLookUp();
	

	
	/**
	 * singleton
	 * 
	 * @return
	 */
	public static DnsLookUp getInstance()
	{
		return instance;
	}


	/**
	 *  recuperation de la liste des Noeuds du réseau via le DNS SEED Bitcoin
	 *  
	 * @return Retour OK et la liste des noeuds du reseaux bitcoin 
	 */
	public Pair<Retour,List<PeerNode>> getAllNodePeer(){
		List<PeerNode> listNodePeer =null;
		Retour retour = Utils.createRetourOK();
		logger.info("Node discovery :  ");

		try {
			InetAddress[] listhost=InetAddress.getAllByName(Utils.DNS_SEED);
			if(listhost!=null) {
				listNodePeer = new ArrayList<PeerNode>();
				for(InetAddress netAddr:listhost) {

					IPVersion ipversion = getVersionIp(netAddr.getHostAddress());
					PeerNode peer = new PeerNode(ipversion);
					peer.setHost(netAddr.getHostAddress());
					peer.setPort(8333);
					listNodePeer.add(peer);
					logger.info("peer  " +peer.getHost()  +"        " + peer.getVersion() );
				}
			}
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
			retour = Utils.createRetourNOK(Utils.FATAL_ERROR, e.getMessage());
		}
		return new Pair<Retour, List<PeerNode>>(retour, listNodePeer);

	}

	/**
	 *  prends un noeud aleatoire seulement IPV4
	 *  
	 * @return noeud bitcoin
	 */

	public PeerNode getBestPeer(List<PeerNode> listpeer) {
		PeerNode peernode = null;
		lock.lock();
		if(listpeer !=null) {
			boolean find = false;	
			int randomNum = ThreadLocalRandom.current().nextInt(0, listpeer.size() + 1);
			boolean peerFree = allPeerisUsed(listpeer);
			if(!peerFree) {
				while(!find) {
					randomNum = ThreadLocalRandom.current().nextInt(0, listpeer.size() + 1);
					if(randomNum<listpeer.size()) {
						peernode = listpeer.get(randomNum);
					}
					if(peernode !=null) {
						if(IPVersion.IPV4.equals(peernode.getVersion())) {
							if(!peernode.isUse()) {
								listpeer.remove(peernode);
								peernode.setUse(true);
								listpeer.add(peernode);
								find = true;
							}else {
								peernode = null;
							}
							
							
						
						}
					}
				}
			}
		}
		lock.unlock();
		return peernode;
	}


	private boolean allPeerisUsed(List<PeerNode> listpeer) {
		boolean allused = true;
		if(listpeer !=null) {
			for(PeerNode peer:listpeer) {
				if(!peer.isUse()) {
					allused = false;
				}
			}
		}
		return allused;
	}

	/**
	 * recuperation du type d'adresse IP (IPV4 ou IPV6)
	 * 
	 * @param host
	 * adresse IP
	 * @return Type d'adresse IP (IPV4 ou IPV6)
	 */

	private IPVersion getVersionIp(final String host) {
		IPVersion ipversion = IPVersion.IPV4;
		if(host!=null) {
			if(host.contains(":")) {
				ipversion = IPVersion.IPV6;
			}
		}
		return ipversion;
	}


}
