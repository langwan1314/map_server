package com.youngo.core.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.youngo.core.json.YMDDDateFieldDeserializer;
import com.youngo.core.json.YMDDateFieldFormatter;

import java.util.Date;
import java.util.List;

/**
 * Created by fuchen on 2015/11/17.
 * 用户信息，字段待补充，因为其他地方要用到，先占个位
 */
public class User {
    private String id;//用户id
    private String nickName;//用户昵称
    private String userName;//用户名

    @JsonIgnore
    private String password;//用户密码
    private String icon;//用户头像
    private String sex;//性别

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = YMDDateFieldFormatter.class)
    @JsonDeserialize(using = YMDDDateFieldDeserializer.class)
    private Date birthday;//生日
    private String mobile;//移动电话
    private String email;//手机
    private String country;//国籍/地区
    private String onlineStatus;//在线状态,online:在线,offline:不在线
    @JsonIgnore
    private Date createtime;//记录的创建时间
    @JsonIgnore
    private Date lastupdatetime;//最后更新时间
    private Date lastaccesstime;//最后一次使用APP的时间
    @JsonIgnore
    private Date lastchattime;//最后一次使用聊天功能的时间
    @JsonIgnore
    private String salt;  //加密的盐值
    private Position position;
    private Double latitude;
    private Double longitude;
    private UserDevice userDevice;//用户的移动设备
    private String currentCountry;
    private String currentCity;
    private String geohash;
    @JsonIgnore
    private Integer enable;// 1表示账号可用，0表示账号被禁用
    @JsonIgnore
    private String status;// 1表示账号可用，0表示账号被禁用
    @JsonIgnore
    private String local;//用户的本地语言。

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
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

    public Date getLastaccesstime() {
        return lastaccesstime;
    }

    public void setLastaccesstime(Date lastaccesstime) {
        this.lastaccesstime = lastaccesstime;
    }

    public Date getLastchattime() {
        return lastchattime;
    }

    public void setLastchattime(Date lastchattime) {
        this.lastchattime = lastchattime;
    }

    @JsonIgnore
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public UserDevice getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(UserDevice userDevice) {
        this.userDevice = userDevice;
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

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }


    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public UserBrief changeToBrief() {
        UserBrief ub = new UserBrief();
        ub.setId(this.getId());
        ub.setSex(this.getSex());
        ub.setNickName(this.getNickName());
        ub.setUserName(this.getUserName());
        ub.setBirthday(this.getBirthday());
        ub.setMobile(this.getMobile());
        ub.setCountry(this.getCountry());
        ub.setIcon(this.getIcon());
        ub.setEmail(this.getEmail());
        ub.setOnlineStatus(this.getOnlineStatus());
        ub.setCurrentCountry(this.getCurrentCountry());
        ub.setCurrentCity(this.getCurrentCity());
        ub.setLastupdatetime(this.getLastupdatetime());
        ub.setLastaccesstime(this.getLastaccesstime());
        return ub;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", icon='" + icon + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", onlineStatus='" + onlineStatus + '\'' +
                ", createtime=" + createtime +
                ", lastupdatetime=" + lastupdatetime +
                ", lastaccesstime=" + lastaccesstime +
                ", lastchattime=" + lastchattime +
                ", salt='" + salt + '\'' +
                ", position=" + position +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", userDevice=" + userDevice +
                ", currentCountry='" + currentCountry + '\'' +
                ", currentCity='" + currentCity + '\'' +
                ", geohash='" + geohash + '\'' +
                ", enable=" + enable +
                ", status='" + status + '\'' +
                ", local='" + local + '\'' +
                '}';
    }

    public enum UserStatus {
        enable, disable
    }
}
