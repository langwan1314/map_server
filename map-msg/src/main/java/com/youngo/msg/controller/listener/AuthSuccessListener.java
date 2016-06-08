package com.youngo.msg.controller.listener;

import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.core.model.push.PushBriefInfo;
import com.youngo.core.service.msg.WarningService;
import com.youngo.core.smapper.PushQueueMapper;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.controller.event.AuthSucessEvent;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 浮沉 on 2016/5/11.<br>
 * 认证成功，表示长连接正式建立，将之前因为长连接不通，未成功推送给客户的消息推过去
 */
@Component
public class AuthSuccessListener implements ApplicationListener<AuthSucessEvent>
{
    @Autowired
    private PushQueueMapper mapper;

    @Override
    public void onApplicationEvent(AuthSucessEvent event)
    {
        String userId = event.getSource();
        List<PushBriefInfo> list = mapper.list(userId);
        if (list != null && !list.isEmpty())
        {
            Channel channel = ChannelCache.getInstance().get(userId);
            if (channel != null && channel.isActive())
            {
                for (PushBriefInfo briefInfo : list)//目前来说，只有用户警告需要做推送确认抵达
                {
                    int serviceId = briefInfo.getServiceId();
                    int commandId = briefInfo.getCommandId();
                    if (serviceId == IMBaseDefine.ServiceID.SID_LOGIN_VALUE && commandId == IMBaseDefine.LoginCmdID.CID_LOGIN_WARNING_USER_VALUE)
                    {
                        fireUserWarning(channel,userId);
                        mapper.deleteFront(userId);
                    }
                }
            }
        }
    }

    /**
     * 通知用户被禁用
     */
    public static void fireUserWarning(Channel channel, String userId)
    {
        WarningService service = new WarningService();
        SocketResult result = service.buildWarningResult(userId);
        channel.writeAndFlush(result);
    }
}
