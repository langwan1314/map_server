package com.youngo.mobile.model.friendship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.youngo.core.json.JasonDateFieldFormatter;
import com.youngo.core.model.user.UserBrief;

import java.util.Date;

/**
 * Created by fuchen on 2015/11/19.
 */
public class FriendInvite
{
    private String id;
    private UserBrief invitor;
    @JsonIgnore
    private String invitee;
    @JsonIgnore
    private Date createtime;
    @JsonSerialize(using = JasonDateFieldFormatter.class)
    private Date expiretime;
    private String status;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public UserBrief getInvitor()
    {
        return invitor;
    }

    public void setInvitor(UserBrief invitor)
    {
        this.invitor = invitor;
    }

    public String getInvitee()
    {
        return invitee;
    }

    public void setInvitee(String invitee)
    {
        this.invitee = invitee;
    }

    public Date getCreatetime()
    {
        return createtime;
    }

    public void setCreatetime(Date createtime)
    {
        this.createtime = createtime;
    }

    public Date getExpiretime()
    {
        return expiretime;
    }

    public void setExpiretime(Date expiretime)
    {
        this.expiretime = expiretime;
    }

    /**
     * @return 状态，waiting:等待处理,agree:已同意,refused:已拒绝
     */
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
