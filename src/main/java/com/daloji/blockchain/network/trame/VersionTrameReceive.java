package com.daloji.blockchain.network.trame;

import java.io.Serializable;

/**
 * @author daloji
 *
 * Objet representant l'acquitement de la trame version venant d'un noeud
 */
public class VersionTrameReceive implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private VersionTrameMessage version;
	
	private VersionAckTrame versionAck;

	public VersionTrameMessage getVersion() {
		return version;
	}

	public void setVersion(VersionTrameMessage version) {
		this.version = version;
	}

	public VersionAckTrame getVersionAck() {
		return versionAck;
	}

	public void setVersionAck(VersionAckTrame versionAck) {
		this.versionAck = versionAck;
	}
	
	

}
