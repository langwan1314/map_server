package com.youngo.core.common;

public enum SupportedLanguage {
	zh("zh-CN"), zhCN("zh-CN"), zh_CN("zh-CN"), en("en");
	
	private String value;
	private SupportedLanguage(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
