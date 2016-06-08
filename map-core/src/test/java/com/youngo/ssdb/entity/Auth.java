package com.youngo.ssdb.entity;

public class Auth {
	private String auth;
	private String key;
	
	public Auth(){
		
	}
	public Auth(String auth, String key) {
		super();
		this.auth = auth;
		this.key = key;
	}
	
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auth == null) ? 0 : auth.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		Auth other = (Auth) obj;
		if (auth == null) {
			if (other.auth != null)
				return false;
		} else if (!auth.equals(other.auth))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Auth [auth=" + auth + ", key=" + key + "]";
	}
}
