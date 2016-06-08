package com.youngo.core.model.msg;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here

/**
 * 这个类不同与其他自动生成代码
 * 需要依赖conten与display 依赖不同的状态
 * */
// KEEP INCLUDES END

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity mapped to table Message.
 */
public class MessageEntity implements java.io.Serializable {

    protected int id;
    protected int fromId;
    protected int toId;
    /** Not-null value. */
    protected String sessionKey;
    /** Not-null value. */
    protected String content;
    protected int msgType;//1文本消息，2语音消息，3图片消息，4实时语音，详情请参照ChatConstants中的定义
    protected int displayType;
    protected int status;
    protected int created;
    protected int updated;

    // KEEP FIELDS - put your custom fields here

    protected boolean isGIfEmo;
    // KEEP FIELDS END

    public MessageEntity() {
    }

    public MessageEntity(int id) {
        this.id = id;
    }

    public MessageEntity(int id,int fromId, int toId, String sessionKey, String content, int msgType, int displayType, int status, int created, int updated) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.sessionKey = sessionKey;
        this.content = content;
        this.msgType = msgType;
        this.displayType = displayType;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMsgId() {
        return id;
    }

    public void setMsgId(int msgId)
    {
        this.id = msgId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    /** Not-null value. */
    public String getSessionKey() {
        return sessionKey;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /** Not-null value. */
    public String getContent() {
        return content;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setContent(String content) {
        this.content = content;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    // KEEP METHODS - put your custom methods here
    /**
     * -----根据自身状态判断的---------
     */
    @JsonIgnore
    public int getSessionType() {
        switch (msgType) {
            case  ChatConstants.MSG_TYPE_SINGLE_TEXT:
            case  ChatConstants.MSG_TYPE_SINGLE_AUDIO:
            case  ChatConstants.MSG_TYPE_SINGLE_REALTIMEVOICE:
                return ChatConstants.SESSION_TYPE_SINGLE;
            case ChatConstants.MSG_TYPE_GROUP_TEXT:
            case ChatConstants.MSG_TYPE_GROUP_AUDIO:
                return ChatConstants.SESSION_TYPE_GROUP;
            default:
                //todo 有问题
                return ChatConstants.SESSION_TYPE_SINGLE;
        }
    }

    @JsonIgnore
    public String getMessageDisplay() {
        switch (displayType){
            case ChatConstants.SHOW_AUDIO_TYPE:
                return ChatConstants.DISPLAY_FOR_AUDIO;
            case ChatConstants.SHOW_ORIGIN_TEXT_TYPE:
                return content;
            case ChatConstants.SHOW_IMAGE_TYPE:
                return ChatConstants.DISPLAY_FOR_IMAGE;
            case ChatConstants.SHOW_MIX_TEXT:
                return ChatConstants.DISPLAY_FOR_MIX;
            case ChatConstants.SHOW_REALTIME_VOICE:
                return ChatConstants.DISPLAY_FOR_REALTIMEVOICE;
            default:
                return ChatConstants.DISPLAY_FOR_ERROR;
        }
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", msgId=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", content='" + content + '\'' +
                ", msgType=" + msgType +
                ", displayType=" + displayType +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageEntity)) return false;

        MessageEntity that = (MessageEntity) o;

        if (created != that.created) return false;
        if (displayType != that.displayType) return false;
        if (fromId != that.fromId) return false;
        if (msgType != that.msgType) return false;
        if (status != that.status) return false;
        if (toId != that.toId) return false;
        if (updated != that.updated) return false;
        if (!content.equals(that.content)) return false;
        if (id!=that.id) return false;
        return sessionKey.equals(that.sessionKey);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + fromId;
        result = 31 * result + toId;
        result = 31 * result + sessionKey.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + msgType;
        result = 31 * result + displayType;
        result = 31 * result + status;
        result = 31 * result + created;
        result = 31 * result + updated;
        return result;
    }


    /**
     * 获取会话的sessionId
     * @param isSend
     * @return
     */
    public int getPeerId(boolean isSend){
        if(isSend){
            /**自己发出去的*/
            return toId;
        }else{
            /**接受到的*/
            switch (getSessionType()){
                case ChatConstants.SESSION_TYPE_SINGLE:
                    return fromId;
                case ChatConstants.SESSION_TYPE_GROUP:
                    return toId;
                default:
                    return toId;
            }
        }
    }

    @JsonIgnore
    public byte[] getSendContent(){
        return null;
    }

    public boolean getIsGIfEmo() {
        return isGIfEmo;
    }

    public void setIsGIfEmo(boolean isGIfEmo) {
        this.isGIfEmo = isGIfEmo;
    }

    @JsonIgnore
    public boolean isSend(int loginId){
        return (loginId==fromId)?true:false;
    }

    public String buildSessionKey(boolean isSend){
        int sessionType = getSessionType();
        int peerId = getPeerId(isSend);
        sessionKey = EntityChangeEngine.getSessionKey(peerId, sessionType);
        return sessionKey;
    }
    // KEEP METHODS END

}