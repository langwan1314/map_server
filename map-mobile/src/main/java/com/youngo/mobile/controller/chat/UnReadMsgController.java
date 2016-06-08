package com.youngo.mobile.controller.chat;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.model.Login;
import com.youngo.core.model.Result;
import com.youngo.core.model.msg.UnreadEntity;
import com.youngo.core.service.msg.UnReadMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fuchen on 2016/1/13.
 * 未读消息处理
 */
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/unread")
public class UnReadMsgController
{
    @Autowired
    private UnReadMsgService unReadMsgService;

    /**
     * 获取未读消息列表
     *
     * @param request
     * @return
     */
    @Login
    @RequestMapping(method = RequestMethod.GET, value = "list")
    public Collection<UnreadEntity> list(HttpServletRequest request)
    {
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        return unReadMsgService.list(userId);
    }

    /**
     * 读取未读消息
     *
     * @param request 客户端请求
     * @param peerId  聊天伙伴的ID
     * @return 伙伴ID，会话中最后一条消息的ID，会话类型
     */
    @Login
    @RequestMapping(method = RequestMethod.GET, value = "read")
    public Result remove(HttpServletRequest request, @RequestParam int peerId, @RequestParam(required = false) Integer msgId)
    {
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        UnreadEntity unreadEntity = unReadMsgService.clearUnReadMsgCount(Integer.valueOf(userId), peerId);
        Map<String, Integer> data = new HashMap<>();
        if (unreadEntity != null)
        {
            data.put("msgId", msgId);
            data.put("peerId", peerId);
            data.put("sessionType", unreadEntity.getSessionType());
        }
        Result result = new Result();
        result.setData(data);
        return result;
    }

}
