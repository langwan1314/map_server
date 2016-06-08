package com.youngo.msg.controller.listener;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.youngo.core.mapper.user.UserDeviceMapper;
import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMBuddy;
import com.youngo.core.model.protobuf.IMMessage;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.core.model.user.User;
import com.youngo.core.model.user.UserDevice;
import com.youngo.core.service.msg.ChatService;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.controller.event.BuddyAgreeEvent;
import com.youngo.msg.util.PB2Entity;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 浮沉 on 2016/4/15.
 * 好友申请同意
 */
@Component
public class BuddyAgreeListener implements ApplicationListener<BuddyAgreeEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(BuddyAgreeListener.class);
    @Autowired
    private MessageSendListener messageSendListener;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDeviceMapper deviceMapper;
    @Autowired
    private ChatService chatService;
    @Override
    public void onApplicationEvent(BuddyAgreeEvent event)
    {
        IMBuddy.IMBuddyAgreeNotify notify = pushAgreeNotify(event);
        if (notify != null)
        {
            pushBuddyNotify(notify);
        }
    }

    /**
     * @param event 推送好友申请已同意的通知
     * @return IMBuddy.IMBuddyAgreeNotify
     */
    private IMBuddy.IMBuddyAgreeNotify pushAgreeNotify(BuddyAgreeEvent event)
    {
        SocketResult source = event.getSource();
        byte[] body = source.getBody();
        try
        {
            IMBuddy.IMBuddyAgreeNotify notify = IMBuddy.IMBuddyAgreeNotify.parseFrom(body);
            String invitorId = String.valueOf(notify.getInvitorId());
            Channel context = ChannelCache.getInstance().get(invitorId);
            if (context != null && context.isActive())
            {
                context.writeAndFlush(source);
            }
            return notify;
        } catch (InvalidProtocolBufferException e)
        {
            logger.error("protobuf解析出错 : IMBuddy.IMBuddyAgreeNotify");
        }
        return null;
    }

    /**
     * 好友申请被同意后，给双方给推送一条消息：你和XXX成为语言学习了伙伴，快开始协同学习吧
     */
    private void pushBuddyNotify(IMBuddy.IMBuddyAgreeNotify notify)
    {
        IMBaseDefine.UserInfo invitee = notify.getInvitee();
        int inviteeId = invitee.getUserId();
        int invitorId = notify.getInvitorId();
        try
        {
            String msg = getMessageString(String.valueOf(invitorId), invitee.getNickName());
            int time = (int)(System.currentTimeMillis()/1000);
            IMMessage.IMMsgData msgForInvitor = IMMessage.IMMsgData.newBuilder()
                    .setToUserId(invitorId)
                    .setFromUserId(inviteeId)
                    .setMsgData(ByteString.copyFrom(msg ,"utf-8"))
                    .setMsgType(IMBaseDefine.MsgType.MSG_TYPE_SINGLE_BUDDYAGREE)
                    .setCreateTime(time)
                    .build();
            msgForInvitor = save(msgForInvitor);
            messageSendListener.push(msgForInvitor);

            User invitor = userMapper.getById(String.valueOf(invitorId));
            msg = getMessageString(String.valueOf(inviteeId), invitor.getNickName());
            IMMessage.IMMsgData msgForInvitee = IMMessage.IMMsgData.newBuilder()
                    .setToUserId(inviteeId)
                    .setFromUserId(invitorId)
                    .setMsgData(ByteString.copyFrom(msg ,"utf-8"))
                    .setMsgType(IMBaseDefine.MsgType.MSG_TYPE_SINGLE_BUDDYAGREE)
                    .setCreateTime(time)
                    .build();
            msgForInvitee = save(msgForInvitee);
            messageSendListener.push(msgForInvitee);
        } catch (Exception e)
        {
            logger.error("protobuf解析出错 : IMBuddy.IMBuddyAgreeNotify");
        }
    }

    private IMMessage.IMMsgData save(IMMessage.IMMsgData msgData)
    {
        MessageEntity msg = PB2Entity.getMessageEntity(msgData);//将protobuf转换成java容易处理的javabean
        chatService.saveBuddyAgreeMessage(msg);
        return msgData.toBuilder().setMsgId(msg.getId()).build();
    }

    private String getMessageString(String userId, String parterName)
    {
        String local = getLocal(userId);
        if ("zh-CN".equals(local))
        {
            return "你和" + parterName + "成为了语言学习伙伴，快开始协同学习吧！";
        } else
        {
            return "You and " + parterName + " are language partners now.";
        }
    }

    private String getLocal(String userId)
    {
        String local = null;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("loginStatus", 1);
        UserDevice detailInfo = deviceMapper.getDetailInfo(param);
        if (detailInfo != null)
        {
            local = detailInfo.getLanguage();
        }
        return local;
    }

}
