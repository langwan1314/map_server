package com.youngo.mobile.controller.friendship;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.model.Login;
import com.youngo.core.mapper.friendship.BlackListMapper;
import com.youngo.mobile.mapper.friendship.FriendShipMapper;
import com.youngo.mobile.mapper.friendship.ProsecuteMapper;
import com.youngo.core.model.friendship.BlackListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 浮沉 on 2016/3/4.
 * 举报
 */
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/prosecute")
public class ProsecuteController
{
    @Autowired
    private ProsecuteMapper prosecuteMapper;
    @Autowired
    private BlackListMapper blackListMapper;
    @Autowired
    private FriendShipMapper friendShipMapper;
    /**
     * accusedId : 被举报人用户ID
     * reason ： 举报的理由或原因
     * 新增举报信箱
     * @return
     */
    @Login
    @RequestMapping(value = "/add", method = RequestMethod.POST)
     public String prosecute(@RequestBody Map<String,String> params , HttpServletRequest request)
     {
         String accusedId = params.get("accusedId");
         if(StringUtils.isEmpty(accusedId))
         {
             return "success";
         }
         String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
         params.put("prosecutorId",userId);//举报人为当前用户
         prosecuteMapper.insert(params);//插入举报信息

         BlackListItem item = new BlackListItem();
         item.setUserId(userId);
         item.setBlackId(accusedId);
         blackListMapper.insert(item);//举报的同时把被举报的用户加入到黑名单中

         Map<String,String> friendShipParams = new HashMap<>(2);
         friendShipParams.put("userId",userId);
         friendShipParams.put("friendId",accusedId);
         friendShipMapper.remove(friendShipParams);//举报的时候需要同步删除好友关系
         return "success";
     }
}
