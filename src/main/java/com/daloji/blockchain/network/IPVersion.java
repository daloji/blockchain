package com.daloji.blockchain.network;

public enum IPVersion {


	IPV4("IPV4"),

	IPV6("IPV6");

	protected String version;


	private  IPVersion(final String iPversion) {
		version =	iPversion;
	}

}
