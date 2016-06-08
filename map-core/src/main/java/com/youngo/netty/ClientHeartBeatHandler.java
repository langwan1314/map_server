package com.youngo.netty;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMOther;
import com.youngo.core.model.protobuf.SocketResult;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by fuchen on 2016/1/26.
 * 客户端心跳
 */
@Service
public class ClientHeartBeatHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = LoggerFactory.getLogger(ClientHeartBeatHandler.class);
    private static volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        SocketResult result = (SocketResult) msg;
        Header header = result.getHeader();
        int serviceId = header.getServiceId();
        short commandId = header.getCommandId();
        if (serviceId == IMBaseDefine.ServiceID.SID_OTHER_VALUE && commandId == IMBaseDefine.OtherCmdID.CID_OTHER_HEARTBEAT_VALUE)
        {
            logger.debug("heart beat success in netty client");
        } else if (serviceId == IMBaseDefine.ServiceID.SID_LOGIN_VALUE
                && commandId == IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE)//服务器刚刚认证成功，启动心跳
        {
            HeartBeatTask task = new HeartBeatTask(ctx);
            heartBeat = ctx.executor().scheduleAtFixedRate(task, 20, 300, TimeUnit.SECONDS);
        } else
        {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        if(heartBeat!=null)
            heartBeat.cancel(true);//通信异常，停止心跳
        super.exceptionCaught(ctx, cause);
    }

    /**
     * 心跳发送器
     */
    public class HeartBeatTask implements Runnable
    {
        private ChannelHandlerContext ctx;

        private HeartBeatTask(ChannelHandlerContext ctx)
        {
            this.ctx = ctx;
        }

        @Override
        public void run()
        {
            Header header = buildHeader();
            IMOther.IMHeartBeat imHeartBeat = IMOther.IMHeartBeat.newBuilder().build();
            SocketResult result = new SocketResult();
            result.setHeader(header);
            result.setBody(imHeartBeat.toByteArray());
            ctx.writeAndFlush(result);
        }
    }

    private Header buildHeader()
    {
        short sid = IMBaseDefine.ServiceID.SID_OTHER_VALUE;
        short cid = IMBaseDefine.OtherCmdID.CID_OTHER_HEARTBEAT_VALUE;
        return new Header(sid, cid);
    }

}
