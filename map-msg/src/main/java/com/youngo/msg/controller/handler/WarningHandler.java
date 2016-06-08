package com.youngo.msg.controller.handler;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMLogin;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.controller.ChannelCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by 浮沉 on 2016/3/21.
 * 警告用户
 */
public class WarningHandler extends AbstractChannelHandler
{
    @Override
    public boolean understand(int serviceId)
    {
        return serviceId== IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
    }

    @Override
    public void execute(ChannelHandlerContext ctx, SocketResult input) throws Exception
    {
        Header header = input.getHeader();
        short commandId = header.getCommandId();
        if (IMBaseDefine.LoginCmdID.CID_LOGIN_WARNING_USER_VALUE == commandId)
        {
            byte[] body = input.getBody();
            IMLogin.IMWarningUser warningUser = IMLogin.IMWarningUser.parseFrom(body);
            int userId = warningUser.getUserId();
            Channel channel = ChannelCache.getInstance().get(String.valueOf(userId));
            if(channel!=null && channel.isActive())
            {
                channel.writeAndFlush(input);
            }
        }
    }
}