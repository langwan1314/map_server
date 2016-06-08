package com.youngo.mobile.controller.chat;

import com.youngo.core.exception.RestException;
import com.youngo.core.model.Result;
import com.youngo.ssdb.core.SsdbConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by fuchen on 2015/11/27.
 * 聊天会话
 */
public class ChatSessionUtil
{
    private final Logger logger = LoggerFactory.getLogger(ChatSessionUtil.class);
    private String sessionId;
    private String userId;
    private String targetId;
    public ChatSessionUtil(String sessionId)
    {
        this.sessionId = sessionId;
        if (StringUtils.isEmpty(sessionId))
        {
            throw new RestException(Result.param_error, "sessionId不能为空");
        }
        if (!sessionId.contains(SsdbConstants.Chat.chatSessionPrefix))
        {
            logger.error("错误的聊天sessionId:" + sessionId);
            throw new RestException(Result.param_error, "sessionId格式错误");
        }
        int index = sessionId.indexOf(SsdbConstants.Chat.chatSessionPrefix);
        String mutiUserId = sessionId.substring(index+SsdbConstants.Chat.chatSessionPrefix.length());
        if (!mutiUserId.contains(":"))
        {
            logger.error("错误的聊天sessionId:" + sessionId);
            throw new RestException(Result.param_error, "sessionId格式错误");
        }
        String[] split = StringUtils.split(mutiUserId, ":");
        if (split.length != 2)
        {
            logger.error("错误的聊天sessionId:" + sessionId);
            throw new RestException(Result.param_error, "sessionId格式错误");
        }
        this.userId = split[0];
        this.targetId = split[1];
    }

    public ChatSessionUtil(String userId, String targetId)
    {
        this.sessionId = SsdbConstants.Chat.chatSessionPrefix + userId + ":" + targetId;
        this.userId=userId;
        this.targetId = targetId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getTargetId()
    {
        return targetId;
    }

    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    @Override
    public String toString()
    {
        return sessionId;
    }
}
