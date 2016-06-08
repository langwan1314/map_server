package com.youngo.mobile.controller.auth;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.youngo.core.model.Result;
import com.youngo.mobile.BaseRestTest;
import com.youngo.mobile.model.auth.Auth;
import com.youngo.mobile.model.auth.ChagePassword;
import com.youngo.mobile.model.auth.SocializationAuth;

public class LoginControllerTesth extends BaseRestTest{
	
	@Test
	@Ignore
	public void testRegister(){
		Auth auth = new Auth();
		auth.setName("qqq@qq.com");
		auth.setPassword("123456");
		Result result = doPost("/login/regist", auth);
		//doPost("/login/regist",)
		
		System.out.println(result.getData());
		System.out.println(result.getMsg());
		
	}
	
	
	@Test
	@Ignore
	public void testGetUserInfo(){
		HashMap<String, String> user = new HashMap<String, String>();
		user.put("userId", "1");
		Result result = doPost("/user/getUserInfo", user);
		
		System.out.println(result.getData());
		
	}
	
	@Test
	@Ignore
	public void testLoginByName(){
		Auth auth = new Auth();
		auth.setName("qq@qq.com");
		auth.setPassword("123456");
		
		Result result = doPost("/login/nameAuth", auth);
		System.out.println(result.getData());
	}
	
	@Test
	@Ignore
	public void testLoginByToken(){
		Auth auth = new Auth();
		auth.setToken("e46be61f0050f9cc3a98d5d2192cb0eb");
		Result result = doPost("/login/tokenAuth", auth);
		System.out.println(result.getData());
	}
	
	@Test
	@Ignore
	public void testLogout(){
		Auth auth = new Auth();
		Result result = doPost("/login/logout", auth);
		System.out.println(result);
	}
	
	@Test
	@Ignore
	public void testForgetPassword(){
		Auth auth = new Auth();
		auth.setName("451179870@qq.com");
		Result result = doPost("/login/forgetPassword", auth);
		System.out.println(result.getData());
		
	}
	
	@Test
	@Ignore
	public void testChangePassword(){
		ChagePassword cp = new ChagePassword();
		cp.setNewPassword("123321");
		cp.setOldPassword("123456");
		Map<String, String> map = new HashMap<String, String>();
		map.put("passport", "b2a3b8c1-2726-4303-848d-1a8500aec430");
		Result result = doPost("/login/changePassword", map, cp);
		System.out.println(result.getData());
	}
	
	@Test
//	@Ignore
	public void testLogin(){
		SocializationAuth auth = new SocializationAuth();
		byte b = 1;
		auth.setType(b);
		auth.setUserId("test2");
		Result result = doPost("/login/ocializationAuth", auth);
		System.out.println(result.getMsg());
		System.out.println(result.getCode());
		System.out.println(result.getData());
	}
	
	
	
}
