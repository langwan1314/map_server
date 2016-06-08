package com.youngo.msg.controller.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.youngo.core.exception.RestException;
import com.youngo.core.model.Result;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMLogin;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.controller.ChannelCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fuchen on 2016/2/15.
 * 安全认证，压力测试的时候用
 * 压力测试时，不对passport做严格检查
 */
public class AuthHandlerForTest extends ChannelInboundHandlerAdapter
{
    private final Logger logger = LoggerFactory.getLogger(AuthHandler.class);
    public static final AttributeKey<String> userIdKey = AttributeKey.valueOf("userIdKey");
    private static AtomicInteger counter = new AtomicInteger();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        SocketResult input = (SocketResult)msg;
        Header header = input.getHeader();
        short serviceId = header.getServiceId();
        if(serviceId == IMBaseDefine.ServiceID.SID_LOGIN_VALUE)
        {
            execute(ctx,(SocketResult)msg);
        }else
        {
            String userId = ctx.channel().attr(AuthHandler.userIdKey).get();
            if(StringUtils.isEmpty(userId))
            {
                ctx.fireExceptionCaught(new RuntimeException("登录认证失败,serviceId:"+serviceId+" commandId:"+header.getCommandId()+" clientIp:"+ctx.channel().remoteAddress()));
            }else
            {
                super.channelRead(ctx, msg);
            }
        }

    }

    public void execute(ChannelHandlerContext ctx, SocketResult input) throws Exception
    {
        Header header = input.getHeader();
        byte[] body = input.getBody();
        short commandId = header.getCommandId();
        if (commandId == IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE)
        {
            IMLogin.IMLoginReq imLoginReq;
            try
            {
                imLoginReq = IMLogin.IMLoginReq.parseFrom(body);
                String passport = counter.addAndGet(1)+"";
                ctx.channel().attr(userIdKey).setIfAbsent(passport);
                ChannelCache.getInstance().put(passport, ctx.channel());
                IMLogin.IMLoginRes response = createResponse(passport);
                body = response.toByteArray();
                SocketResult result = new SocketResult();
                result.setHeader(header);
                result.setBody(body);
                ctx.writeAndFlush(result);
            } catch (InvalidProtocolBufferException e)
            {
                logger.error("protobuf准换出错，无法转换成正确的imloginReq:" + body.toString() , e);
                throw e;
            }
        } else
        {
            throw new RestException(Result.param_error, "无法识别的命令号:" + commandId);
        }
    }

    private IMLogin.IMLoginRes createResponse(String userId)
    {
        IMBaseDefine.UserInfo userInfo = IMBaseDefine.UserInfo.newBuilder().setId(Integer.valueOf(userId)).setNickName(userId).build();

        return IMLogin.IMLoginRes.newBuilder().
                setResultCode(IMBaseDefine.ResultType.REFUSE_REASON_NONE).setServerTime(1).
                build();
    }
}
