package com.youngo.msg.controller.handler;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.SocketResult;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * Created by fuchen on 2016/1/19.
 * 心跳处理
 */
public class HeartBeatHandler extends AbstractChannelHandler
{
    @Override
    public boolean understand(int serviceId)
    {
        return serviceId == IMBaseDefine.ServiceID.SID_OTHER_VALUE;
    }

    /**
     * 心跳处理，原封不动的把客户端请求发来的数据返回即可
     */
    @Override
    public void execute(ChannelHandlerContext ctx, SocketResult input) throws Exception
    {
        Header header = input.getHeader();
        short commandId = header.getCommandId();
        if (IMBaseDefine.OtherCmdID.CID_OTHER_HEARTBEAT_VALUE == commandId)
        {
            ctx.writeAndFlush(input);
        }
    }
}
