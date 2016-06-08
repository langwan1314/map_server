package com.youngo.mobile.model.google;

public class ErrorDetail {
	String domain;
	String reason;
	String message;
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "ErrorDetail [domain=" + domain + ", reason=" + reason + ", message=" + message + "]";
	}
}
