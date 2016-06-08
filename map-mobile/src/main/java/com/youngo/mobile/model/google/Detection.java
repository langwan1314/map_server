package com.youngo.mobile.model.google;

public class Detection {
	private String language;
	private boolean isReliable;
	private double confidence;
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public boolean isReliable() {
		return isReliable;
	}
	public void setReliable(boolean isReliable) {
		this.isReliable = isReliable;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
}
