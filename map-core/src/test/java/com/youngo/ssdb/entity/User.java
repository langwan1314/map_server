package com.youngo.ssdb.entity;

import java.util.List;

public class User{

	private String name;
	
	private String password;
	
	private String address;
	
	private Auth auth;
	
	byte[] btyes;
	
	public User() {
	}
	
	public User(String name, String password, String address) {
		super();
		this.name = name;
		this.password = password;
		this.address = address;
	}

	
	public User(String name, String password, String address, Auth auth) {
		super();
		this.name = name;
		this.password = password;
		this.address = address;
		this.auth = auth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

	public byte[] getBtyes() {
		return btyes;
	}

	public void setBtyes(byte[] btyes) {
		this.btyes = btyes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((auth == null) ? 0 : auth.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
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
		User other = (User) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (auth == null) {
			if (other.auth != null)
				return false;
		} else if (!auth.equals(other.auth))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", password=" + password + ", address=" + address + ", auth=" + auth + ", btyes="
				+ btyes + "]";
	}

}
