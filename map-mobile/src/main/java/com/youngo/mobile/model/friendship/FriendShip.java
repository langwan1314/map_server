package com.youngo.mobile.model.friendship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.youngo.core.json.JasonDateFieldFormatter;

import java.util.Date;

/**
 * Created by fuchen on 2015/11/19.
 */
public class FriendShip
{
    @JsonIgnore
    private String id;
    @JsonIgnore
    private String userId;
    private FriendInfo friend;
    @JsonSerialize(using = JasonDateFieldFormatter.class)
    private Date createtime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public FriendInfo getFriend()
    {
        return friend;
    }

    public void setFriend(FriendInfo friend)
    {
        this.friend = friend;
    }

    public Date getCreatetime()
    {
        return createtime;
    }

    public void setCreatetime(Date createtime)
    {
        this.createtime = createtime;
    }
}
