package com.youngo.msg.controller.listener;

import com.youngo.core.model.msg.MessageEntity;
import com.youngo.msg.service.analyze.SnubbedUserAnalyze;
import com.youngo.msg.controller.event.MessageReceiveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by 浮沉 on 2016/3/23.
 * 处理MessageReceiveEvent
 */
@Component
public class MessageReceiveListener implements ApplicationListener<MessageReceiveEvent>
{
    @Autowired
    private SnubbedUserAnalyze snubbedUserAnalyze;
    @Override
    public void onApplicationEvent(MessageReceiveEvent event)
    {
        MessageEntity msg = event.getSource();
        String fromId = String.valueOf(msg.getFromId());
        String toId = String.valueOf(msg.getToId());
        snubbedUserAnalyze.messageReceived(fromId,toId);
    }
}
