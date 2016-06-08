package com.youngo.msg.controller.event;

import com.youngo.core.model.protobuf.SocketResult;
import org.springframework.context.ApplicationEvent;

/**
 * Created by 浮沉 on 2016/4/15.
 * 语伴申请事件
 */
public class BuddyInviteEvent extends ApplicationEvent
{
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public BuddyInviteEvent(SocketResult source) {
        super(source);
    }

    @Override
    public SocketResult getSource() {
        return (SocketResult)super.getSource();
    }
}
