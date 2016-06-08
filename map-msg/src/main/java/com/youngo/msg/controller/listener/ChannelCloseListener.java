package com.youngo.msg.controller.listener;

import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.user.User;
import com.youngo.msg.controller.event.ChannelCloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by 浮沉 on 2016/4/27.
 * 连接关闭的时候，把用户的状态设置为下线
 */
@Component
public class ChannelCloseListener implements ApplicationListener<ChannelCloseEvent>
{
    @Autowired
    private UserMapper userMapper;
    @Override
    public void onApplicationEvent(ChannelCloseEvent event)
    {
        String userId = event.getSource();
        User user = new User();
        user.setId(userId);
        user.setOnlineStatus("offline");//下线
        user.setLastaccesstime(new Date());
        userMapper.updateByUserId(user);
    }
}
