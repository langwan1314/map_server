package com.youngo.msg.controller.handler;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.controller.Application;
import com.youngo.msg.controller.event.EventPublisher;
import com.youngo.msg.controller.event.MessageSendEvent;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by fuchen on 2015/12/21.
 * 消息推送
 */
public class MessageHandler extends AbstractChannelHandler
{
    private static EventPublisher eventPublisher = Application.getContext().getBean(EventPublisher.class);

    @Override
    public void execute(ChannelHandlerContext ctx, SocketResult input) throws Exception
    {
        Header header = input.getHeader();
        short commandId = header.getCommandId();
        if (IMBaseDefine.MessageCmdID.CID_MSG_DATA_VALUE == commandId)
        {
            MessageSendEvent event = new MessageSendEvent(input);
            eventPublisher.publish(event);
        }
    }

    @Override
    public boolean understand(int serviceId)
    {
        return serviceId == IMBaseDefine.ServiceID.SID_MSG_VALUE;
    }
}
