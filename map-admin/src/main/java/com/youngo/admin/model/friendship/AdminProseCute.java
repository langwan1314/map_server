package com.youngo.admin.model.friendship;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.youngo.core.json.JasonDateFieldFormatter;

import java.util.Date;

/**
 * Created by 浮沉 on 2016/3/15.
 */
public class AdminProseCute
{
    private String id;
    private String prosecutorId;//举报人的用户ID
    private String prosecutorName; //举报人用户昵称
    private String accusedId;//被举报人的用户ID
    private String accusedName; //被举报人用户昵称
    private Date createtime;//创建时间
    private String status;//0 未处理，1 不做处理 ，2 已警告，3已封禁
    private int accusedTimes;//被举报的次数

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

    public String getProsecutorName() {
        return prosecutorName;
    }

    public void setProsecutorName(String prosecutorName) {
        this.prosecutorName = prosecutorName;
    }

    public String getAccusedId() {
        return accusedId;
    }

    public void setAccusedId(String accusedId) {
        this.accusedId = accusedId;
    }

    public String getAccusedName() {
        return accusedName;
    }

    public void setAccusedName(String accusedName) {
        this.accusedName = accusedName;
    }

    @JsonSerialize(using = JasonDateFieldFormatter.class)
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAccusedTimes() {
        return accusedTimes;
    }

    public void setAccusedTimes(int accusedTimes) {
        this.accusedTimes = accusedTimes;
    }
}
