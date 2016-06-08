package com.youngo.mobile.controller.chat;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.exception.RestException;
import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.Login;
import com.youngo.core.model.Result;
import com.youngo.core.model.msg.*;
import com.youngo.core.model.user.User;
import com.youngo.core.service.msg.ChatListService;
import com.youngo.core.service.msg.ChatService;
import com.youngo.core.service.msg.SessionConfigureService;
import com.youngo.core.service.msg.UnReadMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by fuchen on 2015/11/26.
 * 聊天列表
 */
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/chatlist")
public class ChatListController
{
    @Autowired
    private ChatListService chatListservice;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ChatService chatService;
    @Autowired
    private SessionConfigureService sconfigService;
    @Autowired
    private UnReadMsgService unReadMsgService;
    private final Logger logger = LoggerFactory.getLogger(ChatListController.class);

    @Login
    @RequestMapping(method = RequestMethod.GET, value = "list")
    public List<SessionEntity> list(HttpServletRequest request)
    {
        List<SessionEntity> result = new ArrayList<>();
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        Collection<UnreadEntity> list = unReadMsgService.list(userId);
        for (UnreadEntity unreadEntity : list)
        {
            int peerId = unreadEntity.getPeerId();
            String sessionId = chatService.getSessionName(userId,String.valueOf(peerId),1);
            SessionEntity sessionEntity = buildSessionEntity(sessionId);
            if(sessionEntity!=null)
            {
                sessionEntity.setUnReadCnt(unreadEntity.getUnReadCnt());
                result.add(sessionEntity);
            }
        }
        sortSessions(result,userId);
        return result;
    }

    private SessionEntity buildSessionEntity(String sessionId)
    {
        ChatSessionUtil sutil = new ChatSessionUtil(sessionId);
        User targetUser = userMapper.getById(sutil.getTargetId());
        if(targetUser!=null)
        {
            SessionEntity entity ;
            MessageEntity lastMessage = chatService.getLastMessage(sessionId);
            if(lastMessage!=null)
            {
                entity = EntityChangeEngine.getSessionEntity(lastMessage);
            }else
            {
                entity = new SessionEntity();
                entity.setPeerType(1);
            }
            int iPeer = Integer.valueOf(sutil.getTargetId());
            entity.setPeerId(iPeer);
            entity.buildSessionKey();
            return entity;
        }
        return null;
    }

    /**
     * 对会话列表根据是否置顶重新排序
     * @param result 待排序结果集
     */
    private void sortSessions(List<SessionEntity> result , String userId)
    {
        if(!result.isEmpty())
        {
            int topIndex = 0;
            Map<String, SessionConfigure> configures = sconfigService.getAllConfigures(userId);
            if(!configures.isEmpty())
            {
                for(int i=0;i<result.size();i++)
                {
                    SessionEntity entity = result.get(i);
                    int peerId = entity.getPeerId();
                    String key = "1_"+peerId;
                    SessionConfigure configure = configures.get(key);
                    if(configure!=null && configure.isTop())
                    {
                        result.remove(i);
                        result.add(topIndex,entity);
                        topIndex++;
                    }
                }
            }
        }
    }

    /**
     * 删除聊天会话，不删消息
     * @param request 客户端请求
     * @param peerId  聊天对方ID
     * @return 删除的消息条数，现在固定为0
     */
    @Login
    @RequestMapping(method = RequestMethod.GET, value = "remove")
    public Long remove(HttpServletRequest request, @RequestParam String peerId)
    {
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        chatListservice.removeSesseion(userId,peerId);
        unReadMsgService.clearUnReadMsgCount(Integer.valueOf(userId),Integer.valueOf(peerId));
        logger.error("用户："+userId+" 删除聊天会话"+peerId);
        return 0l;
    }

    /**
     * 清空会话消息，只删除消息，不会将会话从会话列表中清除
     * @param request 客户端请求
     * @return 返回被删除的消息条数
     */
    @Login
    @RequestMapping(method = RequestMethod.POST, value = "clear")
    public long clear(HttpServletRequest request , @RequestBody Map<String,String> params)
    {
        String peerId = params.get("peerId");
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        ChatSessionUtil chatSessionUtil = new ChatSessionUtil(userId,peerId);
        if (!userId.equals(chatSessionUtil.getUserId()))
        {
            logger.error("错误的聊天会话删除请求，chatSessionId " + chatSessionUtil.toString() + " userId " + userId);
        }
        Long count = chatService.clearSession(chatSessionUtil.getSessionId());
        unReadMsgService.clearUnReadMsgCount(Integer.valueOf(userId), Integer.valueOf(peerId));
        logger.error("用户："+userId+" 清除聊天会话"+chatSessionUtil.getSessionId());
        return count;
    }

    @Login
    @RequestMapping(method = RequestMethod.GET, value = "getconfig")
    public SessionConfigure getConfigure(HttpServletRequest request,@RequestParam String peerId)
    {
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        return sconfigService.getSessionConfigure(userId, peerId);
    }

    public List<SessionConfigure> getConfigureList(String userId)
    {
        return sconfigService.list(userId);
    }

    @Login
    @RequestMapping(method = RequestMethod.POST, value = "config")
    public String config(HttpServletRequest request , @RequestBody Map<String,String> params)
    {
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        String peerId = params.get("peerId");
        if (StringUtils.isEmpty(peerId))
        {
            throw new RestException(Result.param_error, "peerId can not be null");
        }
        SessionConfigure configure = sconfigService.getSessionConfigure(userId, peerId);
        if (configure == null)
        {
            configure = new SessionConfigure();
            configure.setPeerId(Integer.valueOf(peerId));
            configure.setPeerType(1);
        }
        String top = params.get("top");
        if (!StringUtils.isEmpty(top))
        {
            configure.setTop(getBooean(top));
        }
        String notice = params.get("notice");
        if (!StringUtils.isEmpty(notice))
        {
            configure.setNotice(getBooean(notice));
        }
        String stranslate = params.get("stranslate");
        if (!StringUtils.isEmpty(stranslate))
        {
            configure.setStranslate(stranslate);
        }
        String rtranslate = params.get("rtranslate");
        if (!StringUtils.isEmpty(rtranslate))
        {
            configure.setRtranslate(rtranslate);
        }
        sconfigService.save(userId,peerId,configure);
        return "success";
    }

    private boolean getBooean(String value)
    {
        return "1".equals(value) || "true".equals(value);
    }

}
