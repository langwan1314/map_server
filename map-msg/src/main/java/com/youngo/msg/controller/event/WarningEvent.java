package com.youngo.msg.controller.event;

import com.youngo.core.model.protobuf.SocketResult;
import org.springframework.context.ApplicationEvent;

/**
 * Created by 浮沉 on 2016/4/16.
 * 警告用户（不当言论，滥发广告信息等）
 */
public class WarningEvent extends ApplicationEvent
{
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public WarningEvent(SocketResult source)
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
