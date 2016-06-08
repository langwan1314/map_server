package com.youngo.core.common;

/**
 * 静态常量类
 */
public interface CommonStaticConstant
{
	int PASSPORT_TIME_TO_LIVE = 60 * 60 * 24 * 2;
	
    String USER_ID = "userId"; // 将用户id
    String PASSPORT = "passport"; // 通行证
    String USER_NAME = "userName"; // 将用户id
    String IMEI = "imei";// 用户手机唯一标识位
    

    String CLIENT_VERSION = "clientVersion"; // 客户端版本号 从1.0.0 开始传
    
    int PAPERPLANE_USER_SEND_MAX = 10;//用户能够发送的最大纸飞机数量。
    int PAPERPLANE_ONE_TIME = 60;//每次返回给客户端的飞机数量
    int PAPERPLANE_ACTIVY_TIME = 5 * 24 * 60 * 60 * 1000;//飞机存活时间，单位毫秒 --5天
    
    String inValidateLocation = "4.94065645841247E-324";
    
    int LOGIN_LOCK_TIME =  10 * 1000;//登陆锁定时间  10秒
    
    
}
