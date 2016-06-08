package com.youngo.msg.controller.handler;

import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMMessage;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.model.MsgServerInfo;
import com.youngo.msg.util.PB2Entity;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by FC on 2016/2/18.
 */
public class MessageHandlerForTest  extends AbstractChannelHandler
{
    private static ExecutorService executorService;

    public MessageHandlerForTest()
    {
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(ChannelHandlerContext ctx, SocketResult input) throws Exception
    {
        Header header = input.getHeader();
        byte[] body = input.getBody();
        short commandId = header.getCommandId();
        if (IMBaseDefine.MessageCmdID.CID_MSG_DATA_VALUE == commandId)
        {
            IMMessage.IMMsgData request = IMMessage.IMMsgData.parseFrom(body);
            body = sendMessage(request);
            input.setBody(body);
            header.setCommandId((short) IMBaseDefine.MessageCmdID.CID_MSG_DATA_ACK_VALUE);
            ctx.writeAndFlush(input);//告诉客户端消息发送成功
            MsgServerInfo.getInstance().addMsgCount();
        }
    }

    public byte[] sendMessage(IMMessage.IMMsgData request)
    {
        IMMessage.IMMsgData chatMessage = save(request);//先存储到服务器
        pushMsgAnsc(chatMessage);//然后再异步发送
        String id = String.valueOf(chatMessage.getMsgId());
        IMMessage.IMMsgDataRsp act = IMMessage.IMMsgDataRsp.newBuilder().setMsgId(Integer.valueOf(id)).
                setSessionId(1).setSessionType(IMBaseDefine.SessionType.SESSION_TYPE_SINGLE).
                setUserId(1).build();
        return act.toByteArray();
    }

    private IMMessage.IMMsgData save(IMMessage.IMMsgData msgData)
    {
        MessageEntity msg = PB2Entity.getMessageEntity(msgData);//将protobuf转换成java容易处理的javabean
//        ChatSSDBService.getInstance().save(msg);//保存消息
        //重设ID
        return msgData.toBuilder().setMsgId(msg.getId()).build();
    }

    /**
     * 异步发送消息
     * @param msg 聊天消息
     */
    private void pushMsgAnsc(IMMessage.IMMsgData msg)
    {
        executorService.execute(new Runnable()
        {
            @Override
            public void run()
            {
                SocketResult result = new SocketResult();
                Header header = new Header();
                header.setServiceId((short)3);
                header.setCommandId((short) IMBaseDefine.MessageCmdID.CID_MSG_DATA_VALUE);
                result.setHeader(header);
                result.setBody(msg.toByteArray());
                ChannelCache instance = ChannelCache.getInstance();
                Channel context = instance.get(String.valueOf(msg.getToUserId()));
                if (context != null)
                {
                    context.writeAndFlush(result);
                }
            }
        });
    }

    @Override
    public boolean understand(int serviceId)
    {
        return serviceId == IMBaseDefine.ServiceID.SID_MSG_VALUE;
    }
}
