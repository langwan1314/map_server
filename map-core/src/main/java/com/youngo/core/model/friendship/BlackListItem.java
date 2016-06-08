package com.youngo.core.model.friendship;

import java.util.Date;

/**
 * Created by 浮沉 on 2016/3/14.
 * 黑名单信息
 */
public class BlackListItem
{
    private String id;
    private String userId;
    private String blackId;
    private Date createtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBlackId() {
        return blackId;
    }

    public void setBlackId(String blackId) {
        this.blackId = blackId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
