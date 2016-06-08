package com.youngo.netty;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.SocketResult;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fuchen on 2016/1/26.
 */
@Service
public class SocketResultDecoder extends ByteToMessageDecoder
{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        Header header = new Header();
        header.decode(in);
        SocketResult result = new SocketResult();
        result.setHeader(header);
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        result.setBody(bytes);
        out.add(result);
    }
}
