package com.youngo.mobile.controller.chat;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import com.youngo.core.model.Result;
import com.youngo.mobile.BaseRestTest;

public class TranslateControllerTest  extends BaseRestTest{
	
	@Test
//	@Ignore
	public void testTranslate(){
		HashMap<String, String> auth = new HashMap<String, String>();
		auth.put("source", "Th");
		auth.put("target", "zh-CN");
		Result result = doPost("/translate", auth);
		
		System.out.println(result.getMsg());
		System.out.println(result.getData());
	}
	
	@Ignore
	@Test
	public void testTts(){
		HashMap<String, String> auth = new HashMap<String, String>();
		auth.put("source", "you are a SB fasdfasdf蠢逼");
		Result result = doPost("/tts", auth);
		
		System.out.println(result.getMsg());
		System.out.println(result.getData());
	}
	
}
