package com.youngo.msg.controller.handler;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.SocketResult;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by fuchen on 2016/1/28.
 * 抽象类，如果ChannelHandlerContext未认证，断开连接
 */
public abstract class AbstractChannelHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        SocketResult input = (SocketResult)msg;
        Header header = input.getHeader();
        short serviceId = header.getServiceId();
        if(understand(serviceId))
        {
            execute(ctx,(SocketResult)msg);
        }
        super.channelRead(ctx, msg);
    }

    public abstract boolean understand(int serviceId);
    protected abstract void execute(ChannelHandlerContext ctx,SocketResult input) throws Exception;

}
