package com.youngo.mobile.controller.friendship;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.common.Context;
import com.youngo.core.exception.RestException;
import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.Login;
import com.youngo.core.model.Result;
import com.youngo.core.model.msg.ChatConstants;
import com.youngo.core.model.msg.SessionConfigure;
import com.youngo.core.model.user.User;
import com.youngo.core.model.user.UserBrief;
import com.youngo.core.service.msg.ChatListService;
import com.youngo.core.service.msg.SessionConfigureService;
import com.youngo.mobile.controller.chat.ChatSessionUtil;
import com.youngo.mobile.mapper.friendship.FriendShipMapper;
import com.youngo.mobile.model.friendship.FriendInfo;
import com.youngo.mobile.model.friendship.FriendShip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created by fuchen on 2015/11/19.
 * 好友申请，我的好友列表，好友推荐，删除好友
 */
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/friendship")
public class FriendShipController
{
    @Autowired
    private FriendShipMapper friendShipMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ChatListService chatListService;
    @Autowired
    private SessionConfigureService sconfigService;

    private final Logger logger = LoggerFactory.getLogger(FriendShipController.class);

    /**
     * @return 列出用户的所有好友信息
     */
    @Login
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<FriendInfo> list(HttpServletRequest request)
    {
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        List<FriendShip> list = friendShipMapper.list(userId);
        List<FriendInfo> users = list.stream().map(FriendShip::getFriend).collect(toList());
        addTempFriends(users,userId);
        users.sort((u1, u2) -> u1.getNickName().compareTo(u2.getNickName()));
        User me = userMapper.getById(userId);
        User _admin = userMapper.getById(ChatConstants.adminId);
        users.add(0, createFriendInfo(_admin));//添加官方账号用户
        initSessionConfigures(userId,users);
        return users;
    }

    private FriendInfo createFriendInfo(User user)
    {
        FriendInfo friend = new FriendInfo();
        friend.setId(user.getId());
        friend.setIcon(user.getIcon());
        friend.setNickName(user.getNickName());
        friend.setCountry(user.getCountry());
        friend.setEmail(friend.getEmail());
        friend.setMobile(user.getMobile());
        friend.setOnlineStatus(user.getOnlineStatus());
        friend.setLastupdatetime(user.getLastupdatetime());
        friend.setSex(user.getSex());
        return friend;
    }

    /**
     * @param friends 好友列表
     * @param userId 用户ID
     *  把临时好友添加到好友列表中
     */
    private void addTempFriends(List<FriendInfo> friends ,String userId)
    {
        List<String> friendIds = friends.stream().map(FriendInfo::getId).collect(toList());
        List<String> sessionList = chatListService.getSessionList(userId);
        for(String sessionId : sessionList)
        {
            ChatSessionUtil util = new ChatSessionUtil(sessionId);
            String targetId = util.getTargetId();
            if(!"0".equals(targetId) && !friendIds.contains(targetId))
            {
                User target = userMapper.getById(targetId);
                if(target!=null)
                {
                    FriendInfo friendInfo = createFriendInfo(target);
                    friendInfo.setTemp(true);
                    friends.add(friendInfo);
                }
            }
        }
    }

    private void initSessionConfigures(String userId , List<FriendInfo> friendInfos)
    {
        Map<String, SessionConfigure> configures = sconfigService.getAllConfigures(userId);
        if(!configures.isEmpty())
        {
            for(FriendInfo friendInfo : friendInfos)
            {
                String peerId = friendInfo.getId();
                String key = "1_"+peerId;
                SessionConfigure configure = configures.get(key);
                if(configure!=null)
                {
                    friendInfo.setSessionConfig(configure);
                }
            }
        }
    }

    /**
     * 将friendId对应的用户从朋友列表中删除
     * @return 删除成功
     */
    @Login
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(@RequestBody Map<String, String> params)
    {
        String friendId = params.get("friendId");
        if (StringUtils.isEmpty(friendId))
        {
            throw new RestException(Result.empty_param, "friendId不能为空");
        }
        String userId = Context.forceGetUserId();
        Map<String, String> friendShipParam = new HashMap<>(2);
        friendShipParam.put("userId", userId);
        friendShipParam.put("friendId", friendId);
        friendShipMapper.remove(friendShipParam);
        return "success";
    }

}
