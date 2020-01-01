package com.daloji.core.blockchain.net;

public enum NetParameters {
	
	MainNet(8333,"F9BEB4D9"),

	TestNet(18333,null);

	protected long port;
	
	protected String magic;

	private  NetParameters(final long portNet,String magic) {
		port =	portNet;
		this.magic =magic;
	}

	public String getMagic() {
		return magic;
	}

}
