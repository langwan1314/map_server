package com.youngo.mobile.model.jpush;

/**
 * 极光推送配置信息
 */
public class JPushProperty
{
    private String androidAppKey;
    private String androidMasterSecret;
    private String iosAppKey;
    private String iosMasterSecret;
    private String apns;

    public String getAndroidAppKey()
    {
        return androidAppKey;
    }

    public void setAndroidAppKey(String androidAppKey)
    {
        this.androidAppKey = androidAppKey;
    }

    public String getAndroidMasterSecret()
    {
        return androidMasterSecret;
    }

    public void setAndroidMasterSecret(String androidMasterSecret)
    {
        this.androidMasterSecret = androidMasterSecret;
    }

    public String getIosAppKey()
    {
        return iosAppKey;
    }

    public void setIosAppKey(String iosAppKey)
    {
        this.iosAppKey = iosAppKey;
    }

    public String getIosMasterSecret()
    {
        return iosMasterSecret;
    }

    public void setIosMasterSecret(String iosMasterSecret)
    {
        this.iosMasterSecret = iosMasterSecret;
    }

    public String getApns()
    {
        return apns;
    }

    public void setApns(String apns)
    {
        this.apns = apns;
    }
}
