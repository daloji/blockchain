package com.daloji.blockchain.network.trame;

public enum TrameType {
	
	VERSION("76657273696F6E0000000000"),
	        
	VERACK("76657261636B000000000000"),

	ADDR("616464720000000000000000"),
	
	TX("747800000000000000000000"),

	SENDHEADERS("73656E646865616465727300"),

	ERROR("error");
	
	protected String info;

	private  TrameType(final String info) {
		this.info =	info;
	}

	public String getInfo() {
		return info;
	}


}
