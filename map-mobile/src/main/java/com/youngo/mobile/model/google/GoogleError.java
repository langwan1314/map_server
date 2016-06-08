package com.youngo.mobile.model.google;

import java.util.List;

public class GoogleError {
	List<ErrorDetail> errors;
	Integer code;
	String message;

	public List<ErrorDetail> getErrors() {
		return errors;
	}
	public void setErrors(List<ErrorDetail> errors) {
		this.errors = errors;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "GoogleError [errors=" + errors + ", code=" + code + ", message=" + message + "]";
	}
}
