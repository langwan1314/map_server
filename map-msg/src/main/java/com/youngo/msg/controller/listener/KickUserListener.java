package com.youngo.msg.controller.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMLogin;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.controller.event.KickUserEvent;
import com.youngo.msg.service.apns.ApnsServiceDelegate;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by 浮沉 on 2016/4/16.
 * 踢出用户
 */
@Component
public class KickUserListener implements ApplicationListener<KickUserEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(KickUserListener.class);
    @Override
    public void onApplicationEvent(KickUserEvent event)
    {
        SocketResult input = event.getSource();
        Header header = input.getHeader();
        short commandId = header.getCommandId();
        if (IMBaseDefine.LoginCmdID.CID_LOGIN_KICK_USER_VALUE == commandId)
        {
            byte[] body = input.getBody();
            try {
                IMLogin.IMKickUser imKickUser  = IMLogin.IMKickUser.parseFrom(body);
                int userId = imKickUser.getUserId();
                Channel channel = ChannelCache.getInstance().get(String.valueOf(userId));
                if(channel!=null && channel.isActive())
                {
                    channel.writeAndFlush(input);
                }
            } catch (InvalidProtocolBufferException e)
            {
                logger.error("protobuf解析出错 : IMLogin.IMKickUser");
            }
        }
    }
}
