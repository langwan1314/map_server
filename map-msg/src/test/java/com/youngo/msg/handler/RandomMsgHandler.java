package com.youngo.msg.handler;

import com.google.protobuf.ByteString;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMMessage;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by fuchen on 2016/2/15.
 * 定时随机给服务器发送消息
 */
public class RandomMsgHandler extends ChannelInboundHandlerAdapter
{
    private Random random = new Random();
    private int userCount = 100000;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        super.channelInactive(ctx);
        ctx.executor().shutdownGracefully();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        SocketResult result = (SocketResult) msg;
        Header header = result.getHeader();
        short serviceId = header.getServiceId();
        short commandId = header.getCommandId();
        if(IMBaseDefine.ServiceID.SID_MSG_VALUE==serviceId&&IMBaseDefine.MessageCmdID.CID_MSG_DATA_ACK_VALUE==commandId)
        {
            long count = NettyClient.receiveMsgCount.incrementAndGet();
        } else if (serviceId == IMBaseDefine.ServiceID.SID_LOGIN_VALUE
                && commandId == IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE)//服务器刚刚认证成功，启动心跳
        {
            ctx.executor().scheduleAtFixedRate(new ScheduleMessageSender(ctx),5,10, TimeUnit.SECONDS);
            ctx.fireChannelRead(msg);
        } else
        {
            ctx.fireChannelRead(msg);
        }
    }

    public class ScheduleMessageSender implements Runnable
    {
        private ChannelHandlerContext ctx;
        ScheduleMessageSender(ChannelHandlerContext ctx)
        {
            this.ctx = ctx;
        }

        @Override
        public void run()
        {
            if(ctx.channel().isActive())
            {
                IMMessage.IMMsgData data = IMMessage.IMMsgData.newBuilder().
                        setMsgType(IMBaseDefine.MsgType.MSG_TYPE_SINGLE_TEXT).
                        setCreateTime((int) (System.currentTimeMillis() / 1000)).
                        setFromUserId(random.nextInt(userCount)).
                        setToUserId(random.nextInt(userCount)).
                        setMsgData(ByteString.copyFromUtf8("哈哈")).build();
                byte[] bytes = data.toByteArray();
                Header header = new Header((short)IMBaseDefine.ServiceID.SID_MSG_VALUE,(short)IMBaseDefine.MessageCmdID.CID_MSG_DATA_VALUE);
                SocketResult result = new SocketResult();
                result.setHeader(header);
                result.setBody(bytes);
                ctx.writeAndFlush(result);
            }
        }
    }

    /**
     * 构造请求头
     *
     * @return Header
     */
    private Header buildHeader()
    {
        Header header = new Header();
        header.setCommandId((short) IMBaseDefine.MessageCmdID.CID_MSG_DATA_VALUE);
        header.setSeqnum((short) 1);
        header.setServiceId((short) IMBaseDefine.ServiceID.SID_MSG_VALUE);
        return header;
    }
}
