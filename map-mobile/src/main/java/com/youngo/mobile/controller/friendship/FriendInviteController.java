package com.youngo.mobile.controller.friendship;

import com.youngo.core.common.Context;
import com.youngo.core.exception.RestException;
import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.Login;
import com.youngo.core.model.Result;
import com.youngo.core.model.user.User;
import com.youngo.core.model.user.UserBrief;
import com.youngo.mobile.controller.push.ClientBuddyHandler;
import com.youngo.core.mapper.friendship.BlackListMapper;
import com.youngo.mobile.mapper.friendship.FriendInviteMapper;
import com.youngo.mobile.mapper.friendship.FriendShipMapper;
import com.youngo.mobile.model.friendship.FriendInvite;
import com.youngo.mobile.model.friendship.FriendInviteStatus;
import com.youngo.mobile.model.friendship.FriendShip;
import com.youngo.mobile.smapper.FriendInviteSSDBMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fuchen on 2015/11/19.
 * 语伴申请
 */
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/friendinvite")
public class FriendInviteController
{
    private final Logger logger = LoggerFactory.getLogger(FriendInviteController.class);
    @Autowired
    private FriendInviteMapper friendInviteMapper;
    @Autowired
    private FriendShipMapper friendShipMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlackListMapper blackListMapper;
    @Autowired
    private FriendInviteSSDBMapper unreadMapper;

    /**
     * 列出用户所有的好友申请
     * @return
     */
    @Login
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<FriendInvite> list()
    {
        String userId = Context.forceGetUserId();
        User user = userMapper.getById(userId);
        Map<String, String> params = new HashMap<>(1);
        params.put("invitee", userId);
        List<FriendInvite> list = friendInviteMapper.list(params);
        unreadMapper.clearUnReadCount(userId);
        return list;
    }

    /**
     * 同意好友申请
     *
     * @return
     */
    @Login
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    public String agree(@RequestBody Map<String, String> params)
    {
        String id = params.get("id");
        if (StringUtils.isEmpty(id))
        {
            throw new RestException(Result.empty_param, "参数ID不能为空");
        }
        FriendInvite friendInvite = friendInviteMapper.getById(id);
        if (friendInvite == null)
        {
            throw new RestException(Result.param_error, Result.param_error_msg);
        }
        String status = friendInvite.getStatus();
        if (FriendInviteStatus.waiting.name().equals(status))
        {
            if (expired(friendInvite))
            {
                throw new RestException(Result.param_error, "好友申请已过期");
            }
            String invitee = friendInvite.getInvitee();//被邀请人
            String userId = Context.forceGetUserId();
            if (!userId.equals(invitee))
            {
                throw new RestException(Result.param_error, "非法的操作");
            }
            Map<String,String> inviteParam = new HashMap<>(1);
            inviteParam.put("id",id);
            friendInviteMapper.agree(inviteParam);
            Map<String, String> friendParam = new HashMap<>(2);
            friendParam.put("userId", friendInvite.getInvitee());
            friendParam.put("friendId", friendInvite.getInvitor().getId());
            FriendShip friendShip1 = friendShipMapper.get(friendParam);
            if (friendShip1 == null)
            {
                friendShipMapper.insert(friendInvite.getInvitee(), friendInvite.getInvitor().getId());
            }
            friendParam.put("userId", friendInvite.getInvitor().getId());
            friendParam.put("friendId", friendInvite.getInvitee());
            FriendShip friendShip2 = friendShipMapper.get(friendParam);
            if (friendShip2 == null)
            {
                friendShipMapper.insert(friendInvite.getInvitor().getId(), friendInvite.getInvitee());
            }
            User user = userMapper.getById(userId);
            ClientBuddyHandler.fireBuddyAgree(friendInvite,user);
        }
        return "success";
    }

    /**
     * 发起好友申请
     * @return 是否申请成功
     */
    @Login
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public synchronized String add(@RequestBody Map<String, String> params)
    {
        //参数校验
        String friendId = params.get("friendId");
        if (StringUtils.isEmpty(friendId))
        {
            throw new RestException(Result.empty_param, "错误的请求，friendId不能为空");
        }
        //检查friendId对应的用户是否存在
        User friend = userMapper.getById(friendId);
        if (friend == null)
        {
            throw new RestException(Result.param_error, "添加好友失败，用户不存在");
        }

        String invitorId = Context.forceGetUserId();
        Map<String,String> blackParams = new HashMap<>();
        blackParams.put("userId",invitorId);
        blackParams.put("blackId",friendId);
        blackListMapper.remove(blackParams);//如果以前把用户加了黑名单，那么申请加好友的时候需要把对方从黑名单中移除

        //如果双方已经互为好友关系，重复发起好友申请无效
        Map<String, String> friendParam = new HashMap<>(2);
        friendParam.put("userId", invitorId);
        friendParam.put("friendId", friendId);
        FriendShip friendShip1 = friendShipMapper.get(friendParam);
        if (friendShip1 != null)
        {
            logger.info("重复的好友申请,申请人：" + invitorId + "添加对象：" + friendId);
            return "success";
        }

        Map<String, String> inviteParam = new HashMap<>();
        inviteParam.put("invitor",invitorId);
        inviteParam.put("invitee",  friendId);
        FriendInvite invite1 = friendInviteMapper.get(inviteParam);
        if(invite1!=null && FriendInviteStatus.waiting.name().equals(invite1.getStatus()))//如果是重复申请，更新申请时间
        {
            friendInviteMapper.updateTime(invite1.getId());
        }else
        {
            //插入申请信息
            friendInviteMapper.insert(inviteParam);
            User invitor = userMapper.getById(invitorId);
            ClientBuddyHandler.fireBuddyInvite(invitor,friendId,inviteParam.get("id"));
            unreadMapper.increaseUnReadCount(friendId,1);
        }
        inviteParam = new HashMap<>();
        inviteParam.put("invitor",friendId);
        inviteParam.put("invitee",  invitorId);
        FriendInvite invite2 = friendInviteMapper.get(inviteParam);
        //如果对方也申请过加我为好友，那么直接把这两个人加为好友
        if(invite2!=null && FriendInviteStatus.waiting.name().equals(invite2.getStatus()))
        {
            setFriend(invitorId,friendId);
            friendInviteMapper.agree(inviteParam);
            User user = userMapper.getById(invitorId);
            ClientBuddyHandler.fireBuddyAgree(invite2,user);
            inviteParam = new HashMap<>();
            inviteParam.put("invitor",invitorId);
            inviteParam.put("invitee",  friendId);
            friendInviteMapper.agree(inviteParam);
            invite1 = friendInviteMapper.get(inviteParam);
            ClientBuddyHandler.fireBuddyAgree(invite1,friend);
        }
        return "success";
    }

    private void setFriend(String invitor,String invitee)
    {
        Map<String, String> friendParam = new HashMap<>(2);
        friendParam.put("userId", invitor);
        friendParam.put("friendId", invitee);
        FriendShip friendShip1 = friendShipMapper.get(friendParam);
        if (friendShip1 == null)
        {
            friendShipMapper.insert(invitor, invitee);
        }
        friendParam.put("userId", invitee);
        friendParam.put("friendId",invitor);
        FriendShip friendShip2 = friendShipMapper.get(friendParam);
        if (friendShip2 == null)
        {
            friendShipMapper.insert(invitee,invitor);
        }
    }

    @Login
    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    public String refuse(@RequestParam String id)
    {
        String userId = Context.forceGetUserId();
        FriendInvite invite = friendInviteMapper.getById(id);
        if (invite != null)
        {
            String invitee = invite.getInvitee();
            String status = invite.getStatus();
            if (userId.equals(invitee) && FriendInviteStatus.waiting.name().equals(status))
            {
                friendInviteMapper.refuse(id);
            } else
            {
                logger.info("错误的refuse操作，用户：" + userId + " id：" + id + " status：" + status);
            }
        }
        return "success";
    }

    @Login
    @RequestMapping(value = "/unreadcount", method = RequestMethod.GET)
    public int unReadCount()
    {
        String userId = Context.forceGetUserId();
        return unreadMapper.getUnReadCount(userId);
    }

    @Login
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public String read()
    {
        String userId = Context.forceGetUserId();
        unreadMapper.clearUnReadCount(userId);
        return "success";
    }

    /**
     * 判断邀请是否过期
     *
     * @param friendInvite 朋友邀请
     * @return
     */
    private boolean expired(FriendInvite friendInvite)
    {
        Date expiretime = friendInvite.getExpiretime();
        Date now = new Date();
        return now.after(expiretime);
    }

}
