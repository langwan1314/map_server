package com.youngo.core.model.protobuf;


import com.youngo.core.model.msg.ChatConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * Created by fuchen on 2015/12/21.
 * 消息服务器Socket长连接的通信结果封装
 */
public class SocketResult
{
    private Header header;
    private byte[] body;

    public Header getHeader()
    {
        return header;
    }

    public void setHeader(Header header)
    {
        this.header = header;
    }

    public byte[] getBody()
    {
        return body;
    }

    public void setBody(byte[] body)
    {
        this.body = body;
    }

    /**
     * @param allocator allocate buffers
     * @return 将SocketResult的内容写入到一个byteBuf中，并返回
     */
    public ByteBuf encode(ByteBufAllocator allocator)
    {
        int bodySize = (body == null ? 0 : body.length);
        int length = ChatConstants.PROTOCOL_HEADER_LENGTH + bodySize;
        header.setLength(length);
        ByteBuf buffer = allocator.buffer(length);
        header.encode(buffer);
        if (body != null)
            buffer.writeBytes(body);
        return buffer;
    }
}
