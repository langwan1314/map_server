package com.youngo.admin.controller.push;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMLogin;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.core.service.msg.WarningService;
import com.youngo.netty.NettyContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 浮沉 on 2016/3/21.
 */
public class ClientUserWarningHandler extends ChannelInboundHandlerAdapter
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
    public static void fireUserWarning(String userId)
    {
        Channel channel = NettyContext.getChannel();
        if(channel!=null)
        {
            WarningService service = new WarningService();
            SocketResult result = service.buildWarningResult(userId);
            channel.writeAndFlush(result);
        }
    }

}