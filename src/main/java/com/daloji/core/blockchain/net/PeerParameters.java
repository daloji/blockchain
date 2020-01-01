package com.daloji.core.blockchain.net;

public abstract class PeerParameters {
	
	private IPVersion version;
	
	public PeerParameters(IPVersion versionIp) {
		version =versionIp;
	}

	public IPVersion getVersion() {
		return version;
	}

	
}
