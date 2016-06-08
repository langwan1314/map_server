package com.youngo.msg.controller.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.youngo.core.model.protobuf.*;
import com.youngo.msg.controller.Application;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.controller.event.BuddyAgreeEvent;
import com.youngo.msg.controller.event.BuddyInviteEvent;
import com.youngo.msg.controller.event.EventPublisher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by fuchen on 2016/1/27.
 * 好友申请，添加好友同意的消息通知
 */
public class BuddyHandler extends AbstractChannelHandler
{
    private static final Logger logger = LoggerFactory.getLogger(BuddyHandler.class);
    private static EventPublisher eventPublisher = Application.getContext().getBean(EventPublisher.class);
    @Override
    public boolean understand(int serviceId)
    {
        return IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE==serviceId;
    }

    @Override
    public void execute(ChannelHandlerContext ctx,SocketResult input) throws Exception
    {
        Header header = input.getHeader();
        short commandId = header.getCommandId();
        if(IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_INVITE_NOTIFY_VALUE==commandId)
        {
            BuddyInviteEvent event = new BuddyInviteEvent(input);
            eventPublisher.publish(event);
        }else if(IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_INVITE_AGREE_VALUE==commandId)
        {
            BuddyAgreeEvent event = new BuddyAgreeEvent(input);
            eventPublisher.publish(event);
        }
    }

    private void createBuddyAgreeMsg(SocketResult source)
    {
        byte[] body = source.getBody();
        try {
            IMBuddy.IMBuddyAgreeNotify notify =  IMBuddy.IMBuddyAgreeNotify.parseFrom(body);
            String invitorId = String.valueOf(notify.getInvitorId());
            int inviteeId = notify.getInvitee().getUserId();
            IMMessage.IMMsgData msg = IMMessage.IMMsgData.newBuilder().setToUserId(Integer.valueOf(invitorId))
                    .setFromUserId(inviteeId)
                    .setMsgData(ByteString.copyFrom("你和XXX成为了语言学习伙伴，快开始协同学习吧", "utf-8"))
                    .setMsgType(IMBaseDefine.MsgType.MSG_TYPE_SINGLE_BUDDYAGREE)
                    .build();

        } catch (Exception e)
        {
            logger.error("protobuf解析出错 : IMBuddy.IMBuddyAgreeNotify");
        }
    }
}