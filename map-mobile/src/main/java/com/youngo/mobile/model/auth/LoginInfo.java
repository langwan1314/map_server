package com.youngo.mobile.model.auth;

import com.youngo.core.model.user.UserBrief;

public class LoginInfo {
	private String token;
	
	private String passport;
	
	private UserBrief userBrief;
	
	public LoginInfo(){
	}
	
	public LoginInfo(String token, String passport) {
		super();
		this.token = token;
		this.passport = passport;
	}
	
	public UserBrief getUserBrief() {
		return userBrief;
	}
	
	public void setUserBrief(UserBrief userBrief) {
		this.userBrief = userBrief;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	@Override
	public String toString() {
		return "LoginInfo [token=" + token + ", passport=" + passport + ", userBrief=" + userBrief + "]";
	}
	
}
