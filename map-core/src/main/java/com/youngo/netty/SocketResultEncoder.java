package com.youngo.netty;

import com.youngo.core.model.msg.ChatConstants;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.SocketResult;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by fuchen on 2016/1/28.
 */
@Service
public class SocketResultEncoder extends MessageToByteEncoder<SocketResult>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, SocketResult msg, ByteBuf out) throws Exception
    {
        Header header = msg.getHeader();
        byte[] body = msg.getBody();
        int bodySize = (body == null ? 0 : body.length);
        int length = ChatConstants.PROTOCOL_HEADER_LENGTH + bodySize;
        header.setLength(length);
        header.encode(out);
        if (body != null)
            out.writeBytes(body);
        ctx.writeAndFlush(out);
    }
}
