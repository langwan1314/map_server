package com.youngo.msg.controller.event;

import com.youngo.core.model.protobuf.SocketResult;
import org.springframework.context.ApplicationEvent;

/**
 * Created by 浮沉 on 2016/4/25.
 * 纸飞机消息
 */
public class PaperPlaneMessageSendEvent extends ApplicationEvent
{
    /**
     * Create a new ApplicationEvent.
     * @param source the component that published the event (never {@code null})
     */
    public PaperPlaneMessageSendEvent(SocketResult source)
    {
        super(source);
    }

    /**
     * The object on which the Event initially occurred.
     *
     * @return   The object on which the Event initially occurred.
     */
    public SocketResult getSource() {
        return (SocketResult)source;
    }
}
