package com.youngo.mobile.model.google;

public class GoogleTranslateResult {
	private GoogleData data;
	
	private GoogleError error;

	public GoogleData getData() {
		return data;
	}
	public void setData(GoogleData data) {
		this.data = data;
	}
	public GoogleError getError() {
		return error;
	}
	public void setError(GoogleError error) {
		this.error = error;
	}
	@Override
	public String toString() {
		return "GoogleTranslateResult [data=" + data + ", error=" + error + "]";
	}
}
