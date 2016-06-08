package com.youngo.mobile.controller;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.model.ForceLogin;
import com.youngo.core.model.Login;
import com.youngo.core.model.Result;
import com.youngo.core.model.msg.SessionEntity;
import com.youngo.core.model.msg.UnreadEntity;
import com.youngo.core.model.version.VersionProxy;
import com.youngo.mobile.controller.chat.ChatListController;
import com.youngo.mobile.controller.chat.UnReadMsgController;
import com.youngo.mobile.controller.friendship.FriendShipController;
import com.youngo.mobile.controller.version.VersionController;
import com.youngo.mobile.model.friendship.FriendInfo;
import com.youngo.ssdb.core.DefaultHashMapOperations;
import com.youngo.ssdb.core.HashMapOperations;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 浮沉 on 2016/3/23.
 */
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/home")
public class HomeController
{
    @Autowired
    private VersionController versionController;
    @Autowired
    private FriendShipController friendShipController;
    @Autowired
    private ChatListController chatListController;
    @Autowired
    private UnReadMsgController unReadMsgController;
    /**
     * @return 客户端启动时，需要加载好友信息，聊天会话列表，未读消息列表等数据，将这些接口进行整合，一次性返回，减少请求次数
     */
    @Login(forceLogin = ForceLogin.no)
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "datas")
    public Result getDatas(HttpServletRequest request)
    {
        Map<String,Object> data = new HashMap<>();
        Result result = new Result();
        result.setData(data);
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        if(!StringUtils.isEmpty(userId))
        {
            List<FriendInfo> friends = friendShipController.list(request);//联系人列表
            List<SessionEntity> sessionEntityList = chatListController.list(request);//会话列表
            Collection<UnreadEntity> unreadEntities = unReadMsgController.list(request);
            data.put("friends",friends);
            data.put("sessions",sessionEntityList);
//            data.put("sessionConfigs",configureList);
//            data.put("unreads",unreadEntities);
        }
        return result;
    }

    private VersionProxy getVersion(Map<String, String> params)
    {
        String version = params.get("clientVersion");
        String type = params.get("clientType");
        return versionController.getLastVersion(version, type);
    }

    private HashMapOperations<String, String, UnreadEntity> unReadOperations;//未读消息

    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.unReadOperations = new DefaultHashMapOperations<>(ssdb, String.class, String.class, UnreadEntity.class);
    }
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.GET, value = "test")
//    public String test()
//    {
//        Set<String> names = unReadOperations.getNames("chat:unrea", "chat:unreae", 1000);
//        for(String name : names)
//        {
//            unReadOperations.clear(name);
//        }
//        return names.size()+"";
//    }
}
