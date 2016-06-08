package com.youngo.core.model.friendship;

import java.util.Date;

/**
 * Created by 浮沉 on 2016/3/14.
 */
public class ProseCute
{
    private String id;
    private String prosecutorId; //举报人
    private String accusedId; //被举报人
    private Date createtime;
    private boolean success;//是否举报成功

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProsecutorId() {
        return prosecutorId;
    }

    public void setProsecutorId(String prosecutorId) {
        this.prosecutorId = prosecutorId;
    }

    public String getAccusedId() {
        return accusedId;
    }

    public void setAccusedId(String accusedId) {
        this.accusedId = accusedId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
