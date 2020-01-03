package com.daloji.blockchain.core.commons;

import java.io.Serializable;


/**
 * Class generique  pour les pairs 
 * @author daloji
 *
 * @param <F>
 *  	Objet generique
 * @param <S>
 * 		Objet Generique
 */
public class Pair<F, S> implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public F _first; 


	public S _second;

	public Pair(F first_p, S second_p)
	{
		_first = first_p;
		_second = second_p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_first == null) ? 0 : _first.hashCode());
		result = prime * result + ((_second == null) ? 0 : _second.hashCode());
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
		Pair other = (Pair) obj;
		if (_first == null) {
			if (other._first != null)
				return false;
		} else if (!_first.equals(other._first))
			return false;
		if (_second == null) {
			if (other._second != null)
				return false;
		} else if (!_second.equals(other._second))
			return false;
		return true;
	}



}