package com.youngo.msg.controller.listener;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMLogin;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.core.model.push.PushBriefInfo;
import com.youngo.core.smapper.PushQueueMapper;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.controller.event.WarningEvent;
import com.youngo.msg.service.apns.ApnsServiceDelegate;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by 浮沉 on 2016/4/16.
 * 警告用户（不当言论，滥发广告信息等）
 */
@Component
public class WarningListener implements ApplicationListener<WarningEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(WarningListener.class);
    @Autowired
    private PushQueueMapper pushQueueMapper;
    @Autowired
    private ApnsServiceDelegate apnsServiceDelegate;
    @Override
    public void onApplicationEvent(WarningEvent event)
    {
        SocketResult input = event.getSource();
        Header header = input.getHeader();
        byte[] body = input.getBody();
        try {
            IMLogin.IMWarningUser warningUser = IMLogin.IMWarningUser.parseFrom(body);
            int userId = warningUser.getUserId();
            Channel channel = ChannelCache.getInstance().get(String.valueOf(userId));
            if (channel != null && channel.isActive())
            {
                channel.writeAndFlush(input);
            }else
            {
                save(String.valueOf(userId) , header);
            }
        } catch (InvalidProtocolBufferException e)
        {
            logger.error("protobuf解析出错 : IMLogin.IMWarningUser");
        }
    }

    private void save(String userId , Header header)
    {
        PushBriefInfo briefInfo = new PushBriefInfo();
        briefInfo.setServiceId(header.getServiceId());
        briefInfo.setCommandId(header.getCommandId());
        pushQueueMapper.add(userId ,briefInfo);
    }
}
