package com.youngo.mobile.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngo.core.model.Result;
import com.youngo.mobile.BaseRestTest;

public class UserInfoControllerTest extends BaseRestTest {
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	@Ignore
	public void testCompleteInfo(){
		
		List<Map<String, Object>> goodatM = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> interestM = new ArrayList<Map<String, Object>>();
		
		//Language[] goodAtArr = new Language[2];
		//Language[] interestArr = new Language[1];
		

		Map<String, Object> chineseM = new HashMap<String, Object>(2);
		Map<String, Object> englishM = new HashMap<String, Object>(2);
		Map<String, Object> jepneseM = new HashMap<String, Object>(2);
		chineseM.put("language", "chi");
		chineseM.put("seq", -1);
		
		englishM.put("language", "eng");
		englishM.put("seq", 2);
		
		jepneseM.put("language", "jep");
		jepneseM.put("seq", 3);
		
		goodatM.add(chineseM);
		goodatM.add(englishM);
		interestM.add(jepneseM);
		
		

//		goodAtArr[0] = chinese;
//		goodAtArr[1] = english;
//		interestArr[0] = jepnese;
		
		
		HashMap<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("nickName", " zhe zhi sren3");
		userMap.put("birthday", "2015-06-21");
		userMap.put("sex", "man");
		userMap.put("country", "zh_cn");
		userMap.put("goodatLanguages",goodatM);
		userMap.put("interestedLanguages", interestM);
		userMap.put("icon", "icon");
		
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("passport", "9fedca71-f954-4e47-a840-3458cc7370dX");
		
		Result result = doPost("/user/completeInfo", userMap);
		
//		try {
//			System.out.println(objectMapper.writeValueAsString(userMap));
//			userMap.put("goodatLanguages",goodAtArr);
//			userMap.put("interestedLanguage", interestArr);
//			System.out.println(objectMapper.writeValueAsString(userMap));
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println(result.getData());
		System.out.println(result.getMsg());
		
	}
	
	@Test
	@Ignore
	public void testExists(){
		HashMap<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("nickName", "zhezhiren");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("passport", "b2a3b8c1-2726-4303-848d-1a8500aec430");
		
		Result result = doPost("/user/checkNameExsits", map, userMap);
		
//		assertEquals(new Integer(Result.exsits_username), new Integer(result.getCode()));
		System.out.println(result.getMsg());
		
	}
	
	@Test
	@Ignore
	public void testUpdateSelfIntroduction(){
		HashMap<String, Object> intro = new HashMap<String, Object>();
		//intro.put("text", "我就是我，不用再怀疑");
//		Voice voice = new Voice();
//		voice.setUrl("url");
//		voice.setLength(11);
//		intro.put("voice", voice);
//		
//		List<HashMap<String, Object>> portraitList = new ArrayList<HashMap<String, Object>>();
//		
//		HashMap<String, Object> g1 = new HashMap<String, Object>();
//		g1.put("thumbUrl", "thumbUrl1");
//		g1.put("normalUrl", "normalUrl1");
//		g1.put("originalUrl", "originalUrl1");
//		g1.put("createtime", "2015-11-27 00:00:01");
//		g1.put("seq", 1);
//		
//		HashMap<String, Object> g2 = new HashMap<String, Object>();
//		g2.put("thumbUrl", "thumbUrl2");
//		g2.put("normalUrl", "normalUrl2");
//		g2.put("originalUrl", "originalUrl2");
//		g2.put("createtime", "2015-11-27 00:00:01");
//		g2.put("seq", 2);
//		
//		HashMap<String, Object> g3 = new HashMap<String, Object>();
//		g3.put("thumbUrl", "thumbUrl2");
//		g3.put("normalUrl", "normalUrl2");
//		g3.put("originalUrl", "originalUrl2");
//		g3.put("createtime", "2015-11-27 00:00:01");
//		g3.put("seq", 3);
//		
//		HashMap<String, Object> g4 = new HashMap<String, Object>();
//		g4.put("thumbUrl", "thumbUrl2");
//		g4.put("normalUrl", "normalUrl2");
//		g4.put("originalUrl", "originalUrl2");
//		g4.put("createtime", "2015-11-27 00:00:01");
//		g4.put("seq", 4);
//		
//		portraitList.add(g1);
//		portraitList.add(g2);
//		portraitList.add(g3);
//		portraitList.add(g4);
//		
//		intro.put("portraitList", portraitList);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("passport", "0bc9cf40-84fe-4071-b947-2b97273e66f3");
		
		Result result = doPost("/user/updateSelfIntroduction", intro);
		
		System.out.println(result.getMsg());
	}
	
	@Test
	@Ignore
	public void testgetSelfIntroduction(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("passport", "b2a3b8c1-2726-4303-848d-1a8500aec430");
		
		Map<String, String> body = new HashMap<String, String>();
		
		Result result = doPost("/user/getSelfIntroduction", map, body);
		
		System.out.println(result.getMsg());
		System.out.println(result.getData());
		
	}
	
	@Test
	@Ignore
	public void testGetBaseInfo(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("passport", "b2a3b8c1-2726-4303-848d-1a8500aec430");
		
		Map<String, String> body = new HashMap<String, String>();
		
		Result result = doPost("/user/getBaseInfo", map, body);
		
		System.out.println(result.getMsg());
		System.out.println(result.getData());
	}
	
	@Test
	@Ignore
	public void testupdateBaseInfo(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("passport", "b2a3b8c1-2726-4303-848d-1a8500aec430");
		
		Map<String, String> body = new HashMap<String, String>();
		
		Result result = doPost("/user/updateBaseInfo", map, body);
		
		System.out.println(result.getMsg());
		System.out.println(result.getData());
	}
	
	@Test
	@Ignore
	public void testGetLanguages() throws JsonProcessingException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("local", "zh-CN");
		
		Result result = doPost("/user/getLanguages", map);
		
		System.out.println(objectMapper.writeValueAsString(result.getData()));
		
		
	}

	@Test
	@Ignore
	public void testGetUserBrief() throws JsonProcessingException{
		List<String> ids = new ArrayList<String>();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map.put("userIds", ids);
		ids.add("159");
		ids.add("160");
		Result result = doPost("/user/getUserBrief", map);
		System.out.println(objectMapper.writeValueAsString(result.getData()));
		
		
	}
	
	@Test
	@Ignore
	public void testGetUserInfo() throws JsonProcessingException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", "5");
		map.put("local", "zh");
		Result result = doPost("/user/getUserInfo", map);
		System.out.println(objectMapper.writeValueAsString(result.getData()));
		
		
	}
	
	@Test
	@Ignore
	public void testGetPushUserInfo() throws JsonProcessingException{
		Map<String, String> map = new HashMap<String, String>();
//		map.put("userId", "5");
//		map.put("local", "zh");
		Result result = doPost("/user/getPushUser", map);
//		System.out.println(objectMapper.writeValueAsString(result.getData()));
		System.out.println(objectMapper.writeValueAsString(result.getCode()));
		System.out.println(objectMapper.writeValueAsString(result.getMsg()));
		
		
	}
	
	@Test
//	@Ignore
	public void testDownloadImage(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("imageUrl", "http://www.sd.xinhuanet.com/news/yule/2016-05/04/1118797057_14623241600791n.jpg");
		Result result = doPost("/user/downloadImage", map);
		System.out.println(result.getCode());
		System.out.println(result.getMsg());
		System.out.println(result.getData());
		
	}
	
	
	
}
