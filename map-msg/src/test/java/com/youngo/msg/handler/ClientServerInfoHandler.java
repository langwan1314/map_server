package com.youngo.msg.handler;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMServerInfo;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.controller.ChannelCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by fuchen on 2016/2/15.
 * 服务器信息统计请求处理
 */
public class ClientServerInfoHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = LoggerFactory.getLogger(ClientServerInfoHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);
        ctx.executor().scheduleAtFixedRate(new ScheduleGetter(ctx),3,10, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        SocketResult result = (SocketResult) msg;
        Header header = result.getHeader();
        short serviceId = header.getServiceId();
        short commandId = header.getCommandId();
        if (understand(serviceId, commandId))
        {
            byte[] body = result.getBody();
            IMServerInfo.IMInfo imInfo = IMServerInfo.IMInfo.parseFrom(body);
            logger.error("serverInfo: clientCount is "+imInfo.getClientCount());
            logger.error("serverInfo: msgCount is "+imInfo.getMsgCount());
        }
    }

    public boolean understand(int serviceId, int commandId)
    {
        return (serviceId == IMBaseDefine.ServiceID.SID_SERVERINFO_VALUE) && (commandId == IMBaseDefine.ServiceID.SID_SERVERINFO_VALUE);
    }

    private class ScheduleGetter implements Runnable
    {
        private ChannelHandlerContext ctx;

        ScheduleGetter(ChannelHandlerContext ctx)
        {
            this.ctx = ctx;
        }
        @Override
        public void run()
        {
            SocketResult result = new SocketResult();
            Header header = new Header((short) IMBaseDefine.ServiceID.SID_SERVERINFO_VALUE,(short)IMBaseDefine.ServiceID.SID_SERVERINFO_VALUE);
            IMServerInfo.IMInfo build = IMServerInfo.IMInfo.newBuilder().setClientCount(ChannelCache.getInstance().size()).build();
            result.setBody(build.toByteArray());
            result.setHeader(header);
            ctx.writeAndFlush(result);
        }
    }
}
