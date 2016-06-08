package com.youngo.msg.model.apns;

import org.springframework.stereotype.Component;

/**
 * Created by 浮沉 on 2016/4/12.
 * APNS配置
 */
public class ApnsConfigure
{
    private String certPath;//证书的存放路径
    private String password;//苹果消息推送的password

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
