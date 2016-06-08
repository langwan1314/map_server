package com.youngo.mobile.controller.chat;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.exception.RestException;
import com.youngo.core.model.Login;
import com.youngo.core.model.Result;
import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.msg.MsgServerAddress;
import com.youngo.core.service.msg.ChatService;
import com.youngo.core.service.msg.UnReadMsgService;
import com.youngo.core.upload.ImageUploadService;
import com.youngo.ssdb.core.*;
import com.youngo.ssdb.core.entity.Tuple;
import org.nutz.ssdb4j.spi.SSDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by fuchen on 2015/11/18.
 * 聊天，消息发送，接收接口
 */
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/chat")
public class ChatController
{
    private SortedSetOperations<String, String> setOperations;
    private ValueOperations<String, MessageEntity> valueOperations;
    @Autowired
    private ImageUploadService imageService;
    @Autowired
    private MsgServerAddress msgServerAddress;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UnReadMsgService unReadMsgService;
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.setOperations = new DefaultSortedSetOperations<>(ssdb, String.class, String.class);
        this.valueOperations = new DefaultValueOperations<>(ssdb, String.class, MessageEntity.class);
    }

    /**
     * 获取消息服务器地址<br>
     * 消息服务器会有多个，根据各消息服务器的负载情况，返回一个负载最小的给客户端
     *
     * @return 消息服务器地址
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "msgAddress")
    public MsgServerAddress getMsgServerAddrs()
    {
        return msgServerAddress;
    }

    /**
     * 查看历史消息
     *
     * @param request   客户端请求
     * @param peerId    聊天对象ID
     * @param lastMsgId 客户端缓存的最大的ID
     * @return SingleChatResult
     */
    @Login
    @RequestMapping(method = RequestMethod.GET, value = "history")
    public List<MessageEntity> history(HttpServletRequest request, @RequestParam String peerId, @RequestParam Long lastMsgId, @RequestParam(defaultValue = "18") int count)
    {
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        ChatSessionUtil chatSessionUtil = new ChatSessionUtil(userId, peerId);
        String chatSessionId = chatSessionUtil.getSessionId();
        validateUser(request, chatSessionUtil);
        List<MessageEntity> messageEntitys = null;
        List<Tuple<String, Long>> tuples = setOperations.scanScoreReverse(chatSessionId, null, lastMsgId, null, count);
        if (tuples != null && !tuples.isEmpty())
        {
            List<String> keys = new LinkedList<>();
            for (int i = tuples.size() - 1; i >= 0; i--)//scanScoreReverse取出来的数据是反向排序的，所以要反向遍历集合来组装keys
            {
                keys.add(tuples.get(i).getKey());
            }
            messageEntitys = chatService.getMessageEntitys(keys);
            if (messageEntitys != null)
            {
                for (MessageEntity entity : messageEntitys)
                {
                    int fromId = entity.getFromId();
                    boolean isSend = userId.equals(fromId+"");
                    entity.buildSessionKey(isSend);
                }
            }
        }
        unReadMsgService.clearUnReadMsgCount(Integer.valueOf(userId), Integer.valueOf(peerId));//清除未读消息
        return messageEntitys;
    }

    @Login
    @RequestMapping(method = RequestMethod.GET, value = "mget")
    public List<MessageEntity> mutiGetMessageById(HttpServletRequest request, @RequestParam int peerId, @RequestParam String ids)
    {
        String[] split = ids.split(",");
        List<MessageEntity> messages = new ArrayList<>();
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        for (String index : split)
        {
            MessageEntity message = getMessage(Integer.valueOf(userId), peerId, Integer.valueOf(index));
            if(message!=null)
            {
                messages.add(message);
            }
        }
        unReadMsgService.clearUnReadMsgCount(Integer.valueOf(userId), peerId);//清除未读消息
        return messages;
    }

    public MessageEntity getMessage(int userId, int peerId, int index)
    {
        String key1 = userId + ":" + peerId;
        String key2 = peerId + ":" + userId;
        String sessionKey = SsdbConstants.Chat.chatSessionPrefix + key1;
        String detail1 = SsdbConstants.Chat.chatDetailPrefix + key1 + ":" + index;
        String detail2 = SsdbConstants.Chat.chatDetailPrefix + key2 + ":" + index;

        MessageEntity message = null;
        if (setOperations.exists(sessionKey, detail1))
        {
            message = valueOperations.get(detail1);
        } else if (setOperations.exists(sessionKey, detail2))
        {
            message = valueOperations.get(detail2);
        }
        if (message != null)
        {
            int fromId = message.getFromId();
            message.buildSessionKey(userId==fromId);
        }
        return message;
    }

    /**
     * 检查发起请求的用户和聊天session的用户是否一致（不允许查看不是自己的聊天信息）
     */
    private void validateUser(HttpServletRequest request, ChatSessionUtil chatSessionUtil)
    {
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        if (!userId.equals(chatSessionUtil.getUserId()))
        {
            logger.error("错误的聊天消息pull请求，chatSessionId " + chatSessionUtil.toString() + " userId " + userId);
            throw new RestException(Result.param_error, "sessionId格式错误");
        }
    }

    @Login
    @RequestMapping(method = RequestMethod.POST, value = "/image/fsend")
    public String sendImage(MultipartHttpServletRequest request) throws IOException
    {
        String senderId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        String[] imageurl = imageService.sendToServer(request, "chat/" + senderId);
        if (imageurl != null && imageurl.length > 0)
        {
            return imageurl[0];
        }
        return null;
    }

    /**
     * 删除消息
     *
     * @param params 客户端参数
     */
    @Login
    @RequestMapping(method = RequestMethod.POST, value = "remove")
    public Long remove(HttpServletRequest request, @RequestBody Map<String, Object> params)
    {
        Long removeCount = 0l;
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        String peerId = (String) params.get("peerId");
        ChatSessionUtil session = new ChatSessionUtil(userId, peerId);
        validateUser(request, session);
        List ids = (List) params.get("ids");
        List<Long> longIds = new ArrayList<>();
        if (ids != null && !ids.isEmpty())
        {
            for (Object id : ids)
            {
                longIds.add(Long.valueOf(id.toString()));//统一转换为长整形
            }
            if (isSeries(longIds))//如果id列表连续的话，调用ssdb的范围删除方法
            {
                removeCount = setOperations.deleteByScore(session.getSessionId(), longIds.get(0), longIds.get(longIds.size() - 1));
            } else//逐个删除
            {
                for (Long id : longIds)
                {
                    setOperations.deleteByScore(session.getSessionId(), id, id);
                }
                removeCount = (long) longIds.size();
            }
        }
        return removeCount;
    }

    private boolean isSeries(List<Long> ids)
    {
        ids.sort((Long o1, Long o2) -> (o1.compareTo(o2)));
        for (int i = 1; i < ids.size(); i++)
        {
            if (ids.get(i) != ids.get(i - 1) + 1)
            {
                return false;
            }
        }
        return true;
    }

}
