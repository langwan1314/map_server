package com.youngo.msg.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.youngo.core.model.msg.ChatConstants;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMLogin;
import com.youngo.core.model.protobuf.SocketResult;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fuchen on 2016/1/26.
 * 握手和安全认证
 */
public class ClientLoginHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = LoggerFactory.getLogger(ClientLoginHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        Header header = buildHeader();
        byte[] body = buildBody();
        SocketResult result = new SocketResult();
        result.setHeader(header);
        result.setBody(body);
        ctx.writeAndFlush(result);
        ctx.fireChannelActive();//通知下一个处理器
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
            innerRead(ctx, result);
        }else
        {
            ctx.fireChannelRead(msg);
        }
    }

    public void innerRead(ChannelHandlerContext ctx, SocketResult result) throws InvalidProtocolBufferException
    {
        IMLogin.IMLoginRes loginRes = IMLogin.IMLoginRes.parseFrom(result.getBody());
        IMBaseDefine.ResultType code = loginRes.getResultCode();
        if (code == IMBaseDefine.ResultType.REFUSE_REASON_NONE)
        {
            ctx.fireChannelRead(result);//服务器认证成功，启动心跳
        } else
        {
            logger.error("消息服务器认证失败,code : "+code);
            ctx.close();
        }
    }

    /**
     * 构造请求体
     *
     * @return byte[]
     */
    private byte[] buildBody()
    {
        IMLogin.IMLoginReq req = IMLogin.IMLoginReq.newBuilder().
                setClientType(IMBaseDefine.ClientType.CLIENT_TYPE_MAC).
                setPassport(ChatConstants.TEST_CLIENT_NETTY_PASSPORT).build();
        return req.toByteArray();
    }

    /**
     * 构造请求头
     *
     * @return Header
     */
    private Header buildHeader()
    {
        Header header = new Header();
        header.setCommandId((short) IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE);
        header.setSeqnum((short) 1);
        header.setServiceId((short) IMBaseDefine.ServiceID.SID_LOGIN_VALUE);
        return header;
    }

    public boolean understand(int serviceId, int commandId)
    {
        return (serviceId == IMBaseDefine.ServiceID.SID_LOGIN_VALUE) && (commandId == IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE);
    }
}
