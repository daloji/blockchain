package com.daloji.core.blockchain.commons;

import java.io.Serializable;

public class Retour implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String resultat;
    
	private String diagnostic;
   
	private String libelle;
	
	
	public Retour (String resultat) {
		this.resultat= resultat;
	}

	public Retour (String resultat,String diagnostic,String libelle) {
		this.resultat= resultat;
		this.libelle =libelle;
		this.diagnostic=diagnostic;
	}
	public String getResultat() {
		return resultat;
	}

	public void setResultat(String resultat) {
		this.resultat = resultat;
	}

	public String getDiagnostic() {
		return diagnostic;
	}

	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diagnostic == null) ? 0 : diagnostic.hashCode());
		result = prime * result + ((libelle == null) ? 0 : libelle.hashCode());
		result = prime * result + ((resultat == null) ? 0 : resultat.hashCode());
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
		Retour other = (Retour) obj;
		if (diagnostic == null) {
			if (other.diagnostic != null)
				return false;
		} else if (!diagnostic.equals(other.diagnostic))
			return false;
		if (libelle == null) {
			if (other.libelle != null)
				return false;
		} else if (!libelle.equals(other.libelle))
			return false;
		if (resultat == null) {
			if (other.resultat != null)
				return false;
		} else if (!resultat.equals(other.resultat))
			return false;
		return true;
	}
   

}
