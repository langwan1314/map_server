package com.youngo.msg.controller.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBuddy;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.controller.event.BuddyInviteEvent;
import com.youngo.msg.service.apns.ApnsServiceDelegate;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by 浮沉 on 2016/4/15.
 * 语伴邀请事件监听
 */
@Component
public class BuddyInviteListener implements ApplicationListener<BuddyInviteEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(BuddyInviteListener.class);
    @Autowired
    private ApnsServiceDelegate apnsServiceDelegate;
    @Override
    public void onApplicationEvent(BuddyInviteEvent event)
    {
        try
        {
            SocketResult result = event.getSource();
            Header header = result.getHeader();
            byte[] body = result.getBody();
            IMBuddy.IMBuddyInviteNotify notify = IMBuddy.IMBuddyInviteNotify.parseFrom(body);
            String inviteeId = String.valueOf(notify.getInviteeId());
            Channel context = ChannelCache.getInstance().get(inviteeId);
            if(context!=null && context.isActive())
            {
                context.writeAndFlush(result);
            }
        } catch (InvalidProtocolBufferException e)
        {
            logger.error("protobuf解析出错 : IMBuddy.IMBuddyInviteNotify");
        }
    }
}
