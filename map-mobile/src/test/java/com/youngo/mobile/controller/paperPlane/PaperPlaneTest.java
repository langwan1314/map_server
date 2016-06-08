package com.youngo.mobile.controller.paperPlane;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import com.youngo.core.model.Result;
import com.youngo.mobile.BaseRestTest;

public class PaperPlaneTest  extends BaseRestTest{
	
	@Test
//	@Ignore
	public void testAdd(){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("content", "1112");
		String[] images = new String[1];
		images[0] = "http://2.im.guokr.com/DCuSvi5iK7rz0yUdSjnGRxEOZYwkeXnRPSfdSvdNTQyAAgAAqQEAAEpQ.jpg";
		param.put("images", images);
		Result result = doPost("/paperPlane/add", param);

		System.out.println("testAdd");
		System.out.println(result.getCode());
		System.out.println(result.getMsg());
		System.out.println(result.getData());
		
	}
	
	@Test
	@Ignore
	public void testGetPlanes(){
		HashMap<String, Object> param = new HashMap<String, Object>();
		
//		param.put("firstPlaneId", "paperPlane:info:16");
//		param.put("lastPlaneId", "paperPlane:info:1");
		Result result = doPost("/paperPlane/getPaperPlanes", param);
		System.out.println("testGetPlanes");
		System.out.println(result.getCode());
		System.out.println(result.getMsg());
		System.out.println(result.getData());
	}
	
	@Test
	@Ignore
	public void testGetPlane(){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("planeId", "paperPlane:info:9");
		param.put("local", "zh-CN");
		Result result = doPost("/paperPlane/getPaperPlane", param);
		System.out.println("testGetPlane");
		System.out.println(result.getCode());
		System.out.println(result.getMsg());
		System.out.println(result.getData());
	}
	
	@Test
	@Ignore
	public void testGetRandom(){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("planeId", "paperPlane:info:1");
		Result result = doPost("/paperPlane/randomPaperPlane", param);
		System.out.println("random");
		System.out.println(result.getCode());
		System.out.println(result.getMsg());
		System.out.println(result.getData());
	}
	
	
	@Test
	@Ignore
	public void testGetMyPlanes(){
		System.out.println("testGetMyPlanes");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("planeId", "paperPlane:info:16");
		Result result = doPost("/paperPlane/myPaperPlane", param);
		System.out.println(result.getCode());
		System.out.println(result.getMsg());
		System.out.println(result.getData());
	}
	
	@Test
	@Ignore
	public void testDeleteMyPlane(){
		System.out.println("testDeleteMyPlane");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("planeId", "paperPlane:info:15");
		Result result = doPost("/paperPlane/deleteMyPlane", param);
		System.out.println(result.getCode());
		System.out.println(result.getMsg());
		System.out.println(result.getData());
	}
	
}
