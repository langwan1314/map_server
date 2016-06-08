package com.youngo.mobile.model.auth;

public class Auth {
	private String name;


	private String mobile;
	private String password;
	
	private String imei;
	
	private String token;
	
	public Auth() {
	}

	public Auth(String name, String mobile, String password, String imei, String token) {
		this.name = name;
		this.mobile = mobile;
		this.password = password;
		this.imei = imei;
		this.token = token;
	}

	public Auth(String name, String password, String imei, String token) {
		super();
		this.name = name;
		this.password = password;
		this.imei = imei;
		this.token = token;
	}
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "Auth [name=" + name + ", password=" + password + ", imei=" + imei + ", token=" + token + "]";
	}
	
}
