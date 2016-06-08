package com.youngo.msg.controller.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.googlecode.protobuf.format.JsonFormat;
import com.youngo.core.model.protobuf.*;
import com.youngo.msg.controller.Application;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.model.MsgServerInfo;
import com.youngo.msg.service.apns.ApnsServiceDelegate;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by fuchen on 2016/2/15.
 * 查看服务器信息,如总连接数，运行期间收发消息总数等等
 */
public class ServerInfoHandler extends AbstractChannelHandler
{
    @Override
    public boolean understand(int serviceId)
    {
        return IMBaseDefine.ServiceID.SID_SERVERINFO_VALUE==serviceId;
    }

    @Override
    protected void execute(ChannelHandlerContext ctx, SocketResult input) throws Exception
    {
        SocketResult result = new SocketResult();
        Header header = new Header((short)IMBaseDefine.ServiceID.SID_SERVERINFO_VALUE,(short)IMBaseDefine.ServiceID.SID_SERVERINFO_VALUE);
        IMServerInfo.IMInfo build = IMServerInfo.IMInfo.newBuilder().
                setClientCount(ChannelCache.getInstance().size()).
                setMsgCount(MsgServerInfo.getInstance().getMsgCount()).build();
        result.setBody(build.toByteArray());
        result.setHeader(header);
        ctx.writeAndFlush(result);
        MsgServerInfo.getInstance().clearMsgCount();
//        test();
    }

    private void test() throws IOException {
        ApnsServiceDelegate serviceDelegate = Application.getContext().getBean(ApnsServiceDelegate.class);
        IMMessage.IMMsgData data = IMMessage.IMMsgData.newBuilder().setMsgId(1)
                .setCreateTime(1)
                .setFromUserId(2)
                .setMsgType(IMBaseDefine.MsgType.MSG_TYPE_GROUP_AUDIO)
                .setMsgData(ByteString.copyFrom("哈哈", "utf-8"))
                .build();
        String string = JsonFormat.printToString(data);
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(string, Map.class);
        serviceDelegate.push(IMBaseDefine.ServiceID.SID_MSG_VALUE,IMBaseDefine.MessageCmdID.CID_MSG_DATA_ACK_VALUE,map,"175");
        System.out.println("push over");
    }

}
