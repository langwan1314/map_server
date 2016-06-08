package com.youngo.mobile.controller.push;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMBuddy;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.core.model.user.User;
import com.youngo.core.model.user.UserBrief;
import com.youngo.mobile.model.friendship.FriendInvite;
import com.youngo.netty.NettyContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by fuchen on 2016/1/27.
 * 处理添加好友申请、好友申请通过的小红点消息推送请求
 */
public class ClientBuddyHandler extends ChannelInboundHandlerAdapter
{
    private final Logger logger = LoggerFactory.getLogger(ClientBuddyHandler.class);

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
     * 添加好友，消息推送
     */
    public static void fireBuddyInvite(User invitor, String invitee, String id)
    {
        Channel channel = NettyContext.getChannel();
        if(channel!=null)
        {
            IMBuddy.IMBuddyInviteNotify notify = IMBuddy.IMBuddyInviteNotify.newBuilder().
                    setId(Integer.valueOf(id)).
                    setInviteeId(Integer.valueOf(invitee)).
                    setInvitor(createPBUser(invitor)).build();
            SocketResult result = new SocketResult();
            Header header = new Header((short) IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE,
                    (short) IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_INVITE_NOTIFY_VALUE);
            result.setHeader(header);
            result.setBody(notify.toByteArray());
            NettyContext.getChannel().writeAndFlush(result);
        }
    }

    /**
     * 好友申请被接受,推送给邀请人
     */
    public static void fireBuddyAgree(FriendInvite invite, User invitee)
    {
        Channel channel = NettyContext.getChannel();
        if (channel != null)
        {
            UserBrief invitor = invite.getInvitor();
            String invitorId = invitor.getId();
            IMBuddy.IMBuddyAgreeNotify notify = IMBuddy.IMBuddyAgreeNotify.newBuilder().
                    setId(Integer.valueOf(invite.getId()))
                    .setInvitorId(Integer.valueOf(invitorId)).setInvitee(createPBUser(invitee)).build();
            SocketResult result = new SocketResult();
            Header header = new Header((short) IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE,
                    (short) IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_INVITE_AGREE_VALUE);
            result.setHeader(header);
            result.setBody(notify.toByteArray());
            ByteBuf encode = result.encode(channel.alloc());
            channel.writeAndFlush(encode);
        }
    }

    private static IMBaseDefine.UserInfo createPBUser(User user)
    {
        Integer userId = Integer.valueOf(user.getId());
        IMBaseDefine.UserInfo.Builder builder = IMBaseDefine.UserInfo.newBuilder();
        builder.setId(userId).setCountry(user.getCountry())
                .setIcon(user.getIcon()).setSex(user.getSex()).setNickName(user.getNickName())
                .setOnlineStatus(user.getOnlineStatus()).setUserId(userId);
        return builder.build();
    }

}
