package com.daloji.blockchain.network.listener;

import java.util.List;

import com.daloji.blockchain.network.peers.PeerNode;

public interface InitialDownloadBlock {

	public void onRestartIDB(List<PeerNode> peer);
}
