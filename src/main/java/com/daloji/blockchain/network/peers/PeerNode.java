package com.daloji.blockchain.network.peers;

import java.security.Timestamp;

import com.daloji.blockchain.network.IPVersion;

public class PeerNode extends PeerParameters{

	private String host;

	private int port;

	private Timestamp time;

	private boolean use;

	public PeerNode(IPVersion versionIp) {
		super(versionIp);
	}




	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "PeerNode [host=" + host + ", port=" + port + ", time=" + time + ", use=" + use + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + (use ? 1231 : 1237);
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
		PeerNode other = (PeerNode) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (use != other.use)
			return false;
		return true;
	}

}
