package com.youngo.core.model.msg;

/**
 * @author : yingmu on 15-1-5.
 * @email : yingmu@mogujie.com.
 */
public interface ChatConstants
{

    /**
     * 性别
     * 1. 男性 2.女性
     */
    int SEX_MAILE = 1;
    int SEX_FEMALE = 2;

    /**
     * msgType
     */
    int MSG_TYPE_SINGLE_TEXT = 0x01;
    int MSG_TYPE_SINGLE_AUDIO = 0x02;
    int MSG_TYPE_SINGLE_IMAGE = 0x03;
    int MSG_TYPE_SINGLE_REALTIMEVOICE  = 0x04;
    int MSG_TYPE_SINGLE_BUDDYAGREE  = 0x05;
    int MSG_TYPE_GROUP_TEXT = 0x11;
    int MSG_TYPE_GROUP_AUDIO = 0x12;
    int MSG_TYPE_GROUP_IMAGE = 0x13;
    int MSG_TYPE_GROUP_REALTIMEVOICE = 0x14;
    int MSG_TYPE_GROUP_BUDDYAGREE = 0x15;


    /**
     * msgDisplayType
     * 保存在DB中，与服务端一致，图文混排也是一条
     * 1. 最基础的文本信息
     * 2. 纯图片信息
     * 3. 语音
     * 4. 图文混排
     */
    int SHOW_ORIGIN_TEXT_TYPE = 1;
    int SHOW_IMAGE_TYPE = 2;
    int SHOW_AUDIO_TYPE = 3;
    int SHOW_MIX_TEXT = 4;
    int SHOW_GIF_TYPE = 5;
    int SHOW_REALTIME_VOICE=6;


    String DISPLAY_FOR_IMAGE = "[图片]";
    String DISPLAY_FOR_MIX = "[图文消息]";
    String DISPLAY_FOR_AUDIO = "[语音]";
    String DISPLAY_FOR_ERROR = "[未知消息]";
    String DISPLAY_FOR_REALTIMEVOICE = "[语音通话]";


    /**
     * sessionType
     */
    int SESSION_TYPE_SINGLE = 1;
    int SESSION_TYPE_GROUP = 2;
    int SESSION_TYPE_ERROR = 3;

    String adminId="0";//官方账号,用户ID

    int PROTOCOL_HEADER_LENGTH = 16;// 默认消息头的长度

    String MOBILE_SERVER_NETTY_PASSPORT = "DE0~3POBVRKLb65";// HTTP服务器连消息服务器时候用的passport
    String ADMIN_SERVER_NETTY_PASSPORT = "DE0~3POBVRKLb66";// HTTP服务器连消息服务器时候用的passport
    String TEST_CLIENT_NETTY_PASSPORT = "DE0~3POBVRKLb67";// 测试客户端（如用来查看服务器状态的客户端）使用的passport



}
