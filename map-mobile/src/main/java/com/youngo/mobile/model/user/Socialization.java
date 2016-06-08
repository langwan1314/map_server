package com.youngo.mobile.model.user;

import java.util.Date;

public class Socialization {
    private Integer id;

    private Byte socialType;

    private String userId;

    private String socialValue;

    private Date createtime;

    private Date lastupdatetime;

    public Socialization(Integer id, Byte socialType, String userId, String socialValue, Date createtime, Date lastupdatetime) {
        this.id = id;
        this.socialType = socialType;
        this.userId = userId;
        this.socialValue = socialValue;
        this.createtime = createtime;
        this.lastupdatetime = lastupdatetime;
    }

    public Socialization() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getSocialType() {
        return socialType;
    }

    public void setSocialType(Byte socialType) {
        this.socialType = socialType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSocialValue() {
        return socialValue;
    }

    public void setSocialValue(String socialValue) {
        this.socialValue = socialValue == null ? null : socialValue.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(Date lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }
}