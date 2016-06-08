package com.youngo.msg.controller.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMMessage;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.core.service.msg.ChatService;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.controller.event.MessageSendEvent;
import com.youngo.msg.service.apns.ApnsServiceDelegate;
import com.youngo.msg.util.PB2Entity;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by 浮沉 on 2016/4/7.
 * 消息推送
 */
@Component
public class MessageSendListener implements ApplicationListener<MessageSendEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(MessageSendListener.class);
    @Autowired
    private ChatService chatService;
    @Autowired
    private ApnsServiceDelegate apnsServiceDelegate;
    @Override
    public void onApplicationEvent(MessageSendEvent event)
    {
        SocketResult source = event.getSource();
        byte[] body = source.getBody();
        try {
            IMMessage.IMMsgData request = IMMessage.IMMsgData.parseFrom(body);
            request = save(request);//先保存
            String msgId = String.valueOf(request.getMsgId());
            int fromUserId = request.getFromUserId();
            Header header = source.getHeader();
            reply(header,msgId,String.valueOf(fromUserId));//然后回复消息发送者，告知消息发送成功
            push(request);//再给消息接收方推送消息
        } catch (InvalidProtocolBufferException e)
        {
            logger.error("protobuf解析出错 : IMMessage.IMMsgData");
        }
    }

    private void reply(Header header , String msgId , String userId)
    {
        IMMessage.IMMsgDataRsp act = IMMessage.IMMsgDataRsp.newBuilder().setMsgId(Integer.valueOf(msgId)).
                setSessionId(1).setSessionType(IMBaseDefine.SessionType.SESSION_TYPE_SINGLE).
                setUserId(1).build();
        header.setCommandId((short) IMBaseDefine.MessageCmdID.CID_MSG_DATA_ACK_VALUE);
        SocketResult result = new SocketResult();
        result.setHeader(header);
        result.setBody(act.toByteArray());
        Channel channel = ChannelCache.getInstance().get(String.valueOf(userId));
        if(channel!=null && channel.isActive())
        {
            channel.writeAndFlush(result);
        }
    }

    public IMMessage.IMMsgData save(IMMessage.IMMsgData msgData)
    {
        MessageEntity msg = PB2Entity.getMessageEntity(msgData);//将protobuf转换成java容易处理的javabean
        chatService.save(msg);//保存消息
        //重设ID
        return msgData.toBuilder().setMsgId(msg.getId()).build();
    }

    public void push(IMMessage.IMMsgData chatMessage)
    {
        String toUserId = String.valueOf(chatMessage.getToUserId());
        Channel channel = ChannelCache.getInstance().get(toUserId);
        if(channel!=null)
        {
            sendByNetty(channel,chatMessage);
        }else
        {
            String data = JsonFormat.printToString(chatMessage);
            apnsServiceDelegate.push(IMBaseDefine.ServiceID.SID_MSG_VALUE, IMBaseDefine.MessageCmdID.CID_MSG_DATA_VALUE, data, toUserId);
        }
    }

    private void sendByNetty(Channel channel , IMMessage.IMMsgData msg)
    {
        SocketResult result = new SocketResult();
        Header header = new Header();
        header.setServiceId((short) IMBaseDefine.ServiceID.SID_MSG_VALUE);
        header.setCommandId((short) IMBaseDefine.MessageCmdID.CID_MSG_DATA_VALUE);
        result.setHeader(header);
        result.setBody(msg.toByteArray());
        channel.writeAndFlush(result);
    }
}