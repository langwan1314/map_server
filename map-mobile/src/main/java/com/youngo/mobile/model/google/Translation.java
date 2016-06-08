package com.youngo.mobile.model.google;

public class Translation {
	private String translatedText;
	private String detectedSourceLanguage;
	
	public String getTranslatedText() {
		return translatedText;
	}
	public void setTranslatedText(String translatedText) {
		this.translatedText = translatedText;
	}
	public String getDetectedSourceLanguage() {
		return detectedSourceLanguage;
	}
	public void setDetectedSourceLanguage(String detectedSourceLanguage) {
		this.detectedSourceLanguage = detectedSourceLanguage;
	}
	@Override
	public String toString() {
		return "Translation [translatedText=" + translatedText + ", detectedSourceLanguage=" + detectedSourceLanguage
				+ "]";
	}
}
