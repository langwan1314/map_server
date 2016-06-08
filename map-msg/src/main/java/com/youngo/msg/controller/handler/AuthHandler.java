package com.youngo.msg.controller.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.youngo.core.model.msg.ChatConstants;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMLogin;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.controller.Application;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.core.smapper.AuthMapper;
import com.youngo.msg.controller.event.AuthSucessEvent;
import com.youngo.msg.controller.event.EventPublisher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by fuchen on 2015/12/21.<br>
 * 用户登录完毕后携带passport到消息服务器注册<br>
 * 若passport正确，服务器保持和客户端的长连接<br>
 */
public class AuthHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = LoggerFactory.getLogger(AuthHandler.class);
    public static final AttributeKey<String> userIdKey = AttributeKey.valueOf("userIdKey");
    private static AuthMapper authMapper = Application.getContext().getBean(AuthMapper.class);
    private static EventPublisher eventPublisher = Application.getContext().getBean(EventPublisher.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        SocketResult input = (SocketResult) msg;
        Header header = input.getHeader();
        short commandId = header.getCommandId();
        short serviceId = header.getServiceId();
        if (serviceId == IMBaseDefine.ServiceID.SID_LOGIN_VALUE && commandId == IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE)
        {
            doAuth(ctx, (SocketResult) msg);
        } else
        {
            if (!isAuthed(ctx.channel()))//未认证的连接，不允许其他任何请求
            {
                ctx.close();
            } else
            {
                super.channelRead(ctx, msg);
            }
        }
    }

    public void doAuth(ChannelHandlerContext ctx, SocketResult input) throws Exception
    {
        Header header = input.getHeader();
        byte[] body = input.getBody();
        try
        {
            IMLogin.IMLoginReq imLoginReq = IMLogin.IMLoginReq.parseFrom(body);
            String passport = imLoginReq.getPassport();
            String userId;
            if (!isOfficial(passport))
            {
                userId = authMapper.getPassportUserId(passport);
                if (StringUtils.isEmpty(userId))
                {
                    logger.error("can't find userId by passport : " + passport + ",userId:" + userId);
                    fireInvalidPassport(ctx.channel(), header);
                    return;
                }
                checkContext(userId, ctx);
            } else
            {
                userId = "0";
            }
            ctx.channel().attr(userIdKey).set(userId);
            doResponse(ctx, header);
            fireAuthSuccess(userId);
        } catch (InvalidProtocolBufferException e)
        {
            logger.error("protobuf准换出错，无法转换成正确的imloginReq:", e);
            throw e;
        }
    }

    /**
     * 客户端连接是否通过了服务器的认证
     *
     * @param channel 客户端连接
     * @return 是否认证成功
     */
    private boolean isAuthed(Channel channel)
    {
        String userId = channel.attr(AuthHandler.userIdKey).get();
        return !StringUtils.isEmpty(userId);
    }

    private void fireInvalidPassport(Channel channel, Header header)
    {
        IMLogin.IMLoginRes build = IMLogin.IMLoginRes.newBuilder().
                setResultCode(IMBaseDefine.ResultType.REFUSE_REASON_INVALID_PASSPORT).setServerTime(1).
                build();
        SocketResult result = new SocketResult();
        result.setHeader(header);
        result.setBody(build.toByteArray());
        ChannelFuture future = channel.writeAndFlush(result);
        future.addListener(f -> channel.close());
    }

    private boolean isOfficial(String passport)
    {
        return ChatConstants.ADMIN_SERVER_NETTY_PASSPORT.equals(passport) || ChatConstants.MOBILE_SERVER_NETTY_PASSPORT.equals(passport)
                || ChatConstants.TEST_CLIENT_NETTY_PASSPORT.equals(passport);
    }

    private void checkContext(String userId, ChannelHandlerContext ctx)
    {
        ChannelCache cache = ChannelCache.getInstance();
        Channel oldContex = cache.get(userId);
        if (oldContex != null)
        {
            if (oldContex.isActive())
            {
                logger.error("client relogion,release old context ,userId:" + userId);
                closeFuture(userId, oldContex);
            }
            cache.remove(userId, oldContex);
        }
        cache.put(userId, ctx.channel());
    }

    /**
     * @param userId  用户ID
     * @param channel 长连接通道
     */
    private void closeFuture(String userId, Channel channel)
    {
        IMLogin.IMKickUser kickUser = IMLogin.IMKickUser.newBuilder().setUserId(Integer.valueOf(userId)).
                setKickReason(IMBaseDefine.KickReasonType.KICK_REASON_DUPLICATE_USER).build();
        Header header = new Header((short) IMBaseDefine.ServiceID.SID_LOGIN_VALUE, (short) IMBaseDefine.LoginCmdID.CID_LOGIN_KICK_USER_VALUE);
        SocketResult result = new SocketResult();
        result.setHeader(header);
        result.setBody(kickUser.toByteArray());
        ChannelFuture future = channel.writeAndFlush(result);
        future.addListener(f ->channel.close());
    }

    private void doResponse(ChannelHandlerContext ctx, Header header)
    {
        IMLogin.IMLoginRes response = createResponse();
        byte[] body = response.toByteArray();
        SocketResult result = new SocketResult();
        result.setHeader(header);
        result.setBody(body);
        ctx.writeAndFlush(result);
    }

    private IMLogin.IMLoginRes createResponse()
    {
        return IMLogin.IMLoginRes.newBuilder().
                setResultCode(IMBaseDefine.ResultType.REFUSE_REASON_NONE).setServerTime(1).
                build();
    }

    private void fireAuthSuccess(String userId)
    {
        AuthSucessEvent event = new AuthSucessEvent(userId);
        eventPublisher.publish(event);
    }
}
