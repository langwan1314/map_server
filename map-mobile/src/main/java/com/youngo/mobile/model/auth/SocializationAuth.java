package com.youngo.mobile.model.auth;

public class SocializationAuth {
	private String userId;
	
	private byte type;// 1 为微信 ， 2为facebook

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SocializationAuth [openId=" + userId + ", type=" + type + "]";
	}
}
