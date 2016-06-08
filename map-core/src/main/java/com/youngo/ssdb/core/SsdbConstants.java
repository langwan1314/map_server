package com.youngo.ssdb.core;

/**
 * Created by fuchen on 2015/11/26.
 * 很多地方需要用到Ssdb，为避免key冲突，key的前缀统一在这里定义
 */
public interface SsdbConstants
{
    interface Chat
    {
        String chatSessionPrefix = "chat:session:";//聊天，会话
        String chatDetailPrefix = "chat:detail:";//聊天，真实消息保存
        String chatSessionListPrefix = "chat:slist:";//聊天，记录各会话的排序
        String chatCountPrefix = "chat:count";//聊天，会话消息计数
        String chatUnReadPrefix = "chat:unread:";//聊天，会话消息计数
        String chatSessionConfigPrefix = "chat:sConfig:";//聊天session设置，如map{userId,{sessionId,SessionConfigure}}
    }
    
    interface Language
    {
    	String Language_EN = "languages_en";
    	String Language_ZH = "languages_zh";
    }

    interface FriendInvite
    {
        String friendInviteUnread = "friendinvite:unread:";//未读的好友申请数
    }

    /**
     * 身份认证，包括passport、token、密码等
     */
    interface Authentication
    {
        String SSDB_PASSPORT_PREFIX = "passport:{0}";  //sessionId(passport)对应的userId内容 。  {0}为passport
        String SSDB_TOKEN_PREFIX = "token:{0}";  //token对应的用户信息。 {0}为token
        String SSDB_USER_PASSPORT_SUFFIX = "user:passport:{0}";//用户对应的sessionId(passport)信息。{0}为userId
        String SSDB_USER_TOKEN_SUFFIX = "user:token:{0}"; //用户对应的token信息。 {0}为userId
        String SSDB_USER_BUFFPASSWORD_SUFFFIX="user:buffPassword:{0}"; //用户点击找回密码后产生的临时密码。 {0}为userId
        String LOGIN_LOCK = "login:lock:{0}";
    }

    interface PaperPlaneKey{
    	String paperPlaneInfoPrefix = "paperPlane:info:"; //飞机的信息key的前缀。 kv
    	String paperPlaneInfoKeys = "paperPlane:info:keys"; //飞机的id列表  zlist
    	String paperPlaneActivy = "paperPlane:activy"; //当前存活的飞机id列表 zlist
    	String paperPlaneScanedByPrefix = "paperPlane:scanedBy:"; //飞机的浏览用户列表 前缀   zlist
    	String paperPlaneAutoIncreate = "paperPlane:autoIncreate"; //飞机id生成键，保存当前最大的飞机id。  kv
    	String paperPlaneSurplus = "paperPlane:user:Surplus"; //用户发送的飞机条数 hashMap
    	String paperPlaneUserBrief = "paperPlane:userBrief";//飞机的用户的头像信息  hashmap
    	String paperPlaneUserScanedPrefix ="paperPlane:user:scaned:";//用户已浏览列表。  zlist 用用户拆飞机的时间作为score。
    	String paperPlaneUserScanedKeys ="paperPlane:user:scaned:keys";//用户已浏览列表的key的列表。  zlist 用用户拆飞机的时间作为score。
    	String paperPlaneUserCreatePrefix = "paperPlane:user:created:"; //用户创建的飞机列表 zlist
    	String locationNameLanguagePrefix = "locationName:language:"; //保存不同语言的城市名称 hashMap (第一版仅支持zh-Cn, en)
    }
    
    String userPushedPrefix = "user:pushed:"; //某个用户已经推送了得用户id列表 zlist
    String pushQueuePrefix = "push:stack:";//推送队列，zset {"serviceId":1,"commandId":2,"msgId":3} ,根据serviceId和commandId得知需要推送的消息类型，再根据msgId获取消息内容进行推送

    interface Snubb
    {
        String buddySnubbRecommend = "buddy:snubb_recommend:";//找人聊天，没有被搭理的用户，记录服务器主动给ta推荐好友的次数以及最后一次推荐的时间，map(userId,{"snubbedTimes":1,"lastSnubbTime":15478741187})
    }

}