package com.youngo.netty;

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
import org.springframework.stereotype.Service;

/**
 * Created by fuchen on 2016/1/26.
 * 握手和安全认证
 */
public class ClientLoginHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = LoggerFactory.getLogger(ClientLoginHandler.class);

    private String passport;
    public ClientLoginHandler(String passport)
    {
        this.passport = passport;
    }

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
                setPassport(passport).build();
        return req.toByteArray();
    }

    /**
     * 构造请求头
     *
     * @return Header
     */
    private Header buildHeader()
    {
        return new Header((short) IMBaseDefine.ServiceID.SID_LOGIN_VALUE,(short)IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE);
    }

    public boolean understand(int serviceId, int commandId)
    {
        return (serviceId == IMBaseDefine.ServiceID.SID_LOGIN_VALUE) && (commandId == IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE);
    }
}
