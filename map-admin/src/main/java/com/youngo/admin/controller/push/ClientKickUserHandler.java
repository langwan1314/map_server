package com.youngo.admin.controller.push;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMLogin;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.netty.NettyContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by 浮沉 on 2016/3/21.
 */
public class ClientKickUserHandler extends ChannelInboundHandlerAdapter
{
    private final Logger logger = LoggerFactory.getLogger(ClientKickUserHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        super.exceptionCaught(ctx, cause);
        logger.error("ClientBuddyHandler : ", cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        super.channelRead(ctx, msg);
    }

    /**
     * 通知用户被禁用
     */
    public static void fireUseerDisable(String userId)
    {
        Channel channel = NettyContext.getChannel();
        if(channel!=null)
        {
            IMLogin.IMKickUser kickUser = IMLogin.IMKickUser.newBuilder().setUserId(Integer.valueOf(userId)).
                    setKickReason(IMBaseDefine.KickReasonType.KICK_REASON_PROSECUTOR).build();
            Header header = new Header((short) IMBaseDefine.ServiceID.SID_LOGIN_VALUE, (short) IMBaseDefine.LoginCmdID.CID_LOGIN_KICK_USER_VALUE);
            SocketResult result = new SocketResult();
            result.setHeader(header);
            result.setBody(kickUser.toByteArray());
            channel.writeAndFlush(result);
        }
    }

}
