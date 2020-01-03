package com.daloji.blockchain.network.peers;

import com.daloji.blockchain.network.IPVersion;

public abstract class PeerParameters {
	
	private IPVersion version;
	
	public PeerParameters(IPVersion versionIp) {
		version =versionIp;
	}

	public IPVersion getVersion() {
		return version;
	}

	
}
