package com.youngo.mobile.model.google;

import java.util.List;

public class GoogleData {
	List<Translation> translations;
	List<Language> languages;
	List<Detection> detections;
	public List<Translation> getTranslations() {
		return translations;
	}
	public void setTranslations(List<Translation> translations) {
		this.translations = translations;
	}
	public List<Language> getLanguages() {
		return languages;
	}
	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}
	public List<Detection> getDetections() {
		return detections;
	}
	public void setDetections(List<Detection> detections) {
		this.detections = detections;
	}
	@Override
	public String toString() {
		return "GoogleData [translations=" + translations + ", languages=" + languages + ", detections=" + detections
				+ "]";
	}
}
