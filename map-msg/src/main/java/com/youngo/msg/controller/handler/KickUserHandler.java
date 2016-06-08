package com.youngo.msg.controller.handler;

import com.youngo.core.model.protobuf.*;
import com.youngo.msg.controller.ChannelCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * Created by 浮沉 on 2016/3/21.
 * 踢出用户通知
 */
public class KickUserHandler extends AbstractChannelHandler
{
    @Override
    public boolean understand(int serviceId)
    {
        return serviceId==IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
    }

    @Override
    public void execute(ChannelHandlerContext ctx, SocketResult input) throws Exception
    {
        Header header = input.getHeader();
        short commandId = header.getCommandId();
        if (IMBaseDefine.LoginCmdID.CID_LOGIN_KICK_USER_VALUE == commandId)
        {
            byte[] body = input.getBody();
            IMLogin.IMKickUser imKickUser = IMLogin.IMKickUser.parseFrom(body);
            int userId = imKickUser.getUserId();
            Channel channel = ChannelCache.getInstance().get(String.valueOf(userId));
            if(channel!=null && channel.isActive())
            {
                channel.writeAndFlush(input);
            }
        }
    }
}
