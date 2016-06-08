package com.youngo.core.model.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.youngo.core.json.JasonDateFieldFormatter;
import com.youngo.core.json.YMDDDateFieldDeserializer;
import com.youngo.core.json.YMDDateFieldFormatter;

import java.util.Date;
import java.util.List;

/**
 * Entity mapped to table UserInfo.
 */
public class UserBrief {

    private String id;

    private String sex;
    /**
     * Not-null value.
     */
    private String nickName;
    /**
     * Not-null value.
     */
    private String userName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = YMDDateFieldFormatter.class)
    @JsonDeserialize(using = YMDDDateFieldDeserializer.class)
    private Date birthday;
    /**
     * Not-null value.
     */
    private String mobile;

    private String country;

    private String icon;
    /**
     * Not-null value.
     */
    private String email;

    private String onlineStatus;

    private String currentCountry;

    private String currentCity;

    @JsonIgnore
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonSerialize(using = JasonDateFieldFormatter.class)
    private Date lastupdatetime;

    //@JsonSerialize(using = JasonDateFieldFormatter.class)
    private Date lastaccesstime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }


    public Date getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(Date lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public Date getLastaccesstime() {
        return lastaccesstime;
    }

    public void setLastaccesstime(Date lastaccesstime) {
        this.lastaccesstime = lastaccesstime;
    }

    @Override
    public String toString() {
        return "UserBrief{" +
                "id='" + id + '\'' +
                ", sex='" + sex + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", birthday=" + birthday +
                ", mobile='" + mobile + '\'' +
                ", country='" + country + '\'' +
                ", icon='" + icon + '\'' +
                ", email='" + email + '\'' +
                ", onlineStatus='" + onlineStatus + '\'' +
                ", currentCountry='" + currentCountry + '\'' +
                ", currentCity='" + currentCity + '\'' +
                ", lastupdatetime=" + lastupdatetime +
                ", lastaccesstime=" + lastaccesstime +
                '}';
    }
}
