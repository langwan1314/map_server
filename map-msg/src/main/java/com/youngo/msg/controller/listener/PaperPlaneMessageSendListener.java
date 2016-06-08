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
import com.youngo.msg.controller.event.PaperPlaneMessageSendEvent;
import com.youngo.msg.service.apns.ApnsServiceDelegate;
import com.youngo.msg.util.PB2Entity;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by 浮沉 on 2016/4/25.
 * 纸飞机消息事件处理
 */
@Component
public class PaperPlaneMessageSendListener implements ApplicationListener<PaperPlaneMessageSendEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(MessageSendListener.class);
    @Autowired
    private ChatService chatService;
    @Autowired
    private ApnsServiceDelegate apnsServiceDelegate;
    @Override
    public void onApplicationEvent(PaperPlaneMessageSendEvent event)
    {
        SocketResult source = event.getSource();
        byte[] body = source.getBody();
        try {
            IMMessage.IMMsgData request = IMMessage.IMMsgData.parseFrom(body);
            request = save(request);//先保存
            String msgId = String.valueOf(request.getMsgId());
            int userId = request.getToUserId();
            Header header = source.getHeader();
            reply(header,msgId,String.valueOf(userId));//然后回复消息发送者，告知消息发送成功
            push(request);
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

    private IMMessage.IMMsgData save(IMMessage.IMMsgData msgData)
    {
        MessageEntity msg = PB2Entity.getMessageEntity(msgData);//将protobuf转换成java容易处理的javabean
        chatService.savePaperPlaneMessage(msg);//保存消息
        //重设ID
        return msgData.toBuilder().setMsgId(msg.getId()).build();
    }

    public void push(IMMessage.IMMsgData chatMessage)
    {
        String userId = String.valueOf(chatMessage.getFromUserId());
        Channel channel = ChannelCache.getInstance().get(userId);
        if(channel!=null)
        {
            sendByNetty(channel,chatMessage);
        }
    }

    private void sendByNetty(Channel channel , IMMessage.IMMsgData msg)
    {
        SocketResult result = new SocketResult();
        Header header = new Header();
        header.setServiceId((short) IMBaseDefine.ServiceID.SID_MSG_VALUE);
        header.setCommandId((short) IMBaseDefine.MessageCmdID.CID_PAPERPLANE_DATA_VALUE);
        result.setHeader(header);
        result.setBody(msg.toByteArray());
        channel.writeAndFlush(result);
    }

}
