package com.youngo.core.model.msg;

/**
 * @author : yingmu on 15-1-11.
 * @email : yingmu@mogujie.com.
 */
public interface MessageConstant {

    /**基础消息状态，表示网络层收发成功*/
    int   MSG_SENDING = 1;
    int    MSG_FAILURE = 2;
    int    MSG_SUCCESS = 3;

    /**图片消息状态，表示下载到本地、上传到服务器的状态*/
    int  IMAGE_UNLOAD=1;
    int  IMAGE_LOADING=2;
    int IMAGE_LOADED_SUCCESS =3;
    int IMAGE_LOADED_FAILURE =4;


    /**语音状态，未读与已读*/
    int   AUDIO_UNREAD =1;
    int   AUDIO_READED = 2;

    /**图片消息的前后常量*/
    String IMAGE_MSG_START = "&$#@~^@[{:";
    String IMAGE_MSG_END = ":}]&$~@#@";

}
