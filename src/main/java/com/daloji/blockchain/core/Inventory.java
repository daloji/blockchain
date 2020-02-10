package com.daloji.blockchain.core;

import java.io.Serializable;

/**
 *  Objet invetory bitcoin contenant le hash et le type d'inventory (BLOC,TX,..)
 * @author daloji
 *
 */

public class Inventory  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InvType type;
	
	private String hash;

	public InvType getType() {
		return type;
	}

	public void setType(InvType type) {
		this.type = type;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Inventory other = (Inventory) obj;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
	
}
