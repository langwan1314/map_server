package com.youngo.msg.controller.handler;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.controller.Application;
import com.youngo.msg.controller.event.EventPublisher;
import com.youngo.msg.controller.event.PaperPlaneMessageSendEvent;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by 浮沉 on 2016/4/25.<br>
 * 如果是通过纸飞机进入聊天会话的，需要给聊天双方的会话中都插入纸飞机中携带的信息<br>
 */
public class PaperPlaneHandler extends AbstractChannelHandler
{
    private static EventPublisher eventPublisher = Application.getContext().getBean(EventPublisher.class);

    @Override
    public boolean understand(int serviceId)
    {
        return serviceId == IMBaseDefine.ServiceID.SID_MSG_VALUE;
    }

    @Override
    protected void execute(ChannelHandlerContext ctx, SocketResult input) throws Exception
    {
        Header header = input.getHeader();
        short commandId = header.getCommandId();
        if (IMBaseDefine.MessageCmdID.CID_PAPERPLANE_DATA_VALUE == commandId)
        {
            PaperPlaneMessageSendEvent event = new PaperPlaneMessageSendEvent(input);
            eventPublisher.publish(event);
        }
    }
}
