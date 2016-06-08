package com.youngo.core.model.version;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.youngo.core.json.JasonDateFieldFormatter;

import java.util.Date;

/**
 * @author fuchen 版本信息，服务端记录一个版本号，客户端记录一个版本号<br>
 *         根据两个版本号是否一致来确定客户端是否需要升级<br>
 */
public class Version
{
    private String id;// 主键，递增
    private String versionNumber;// 版本号
    private boolean forcibly;// 是否强制升级
    private String description;// 该版本的改动
    private String url;
    private String versionName;// 显示给用户看的版本名称
    private String clientType;//客户端类型
    private Date createtime;//更新的时间

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getVersionNumber()
    {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber)
    {
        this.versionNumber = versionNumber;
    }

    public boolean isForcibly()
    {
        return forcibly;
    }

    public void setForcibly(boolean forcibly)
    {
        this.forcibly = forcibly;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getVersionName()
    {
        return versionName;
    }

    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }

    public String getClientType()
    {
        return clientType;
    }

    public void setClientType(String clientType)
    {
        this.clientType = clientType;
    }

    @JsonInclude(Include.NON_EMPTY)
    @JsonSerialize(using = JasonDateFieldFormatter.class)
    public Date getCreatetime()
    {
        return createtime;
    }

    public void setCreatetime(Date createtime)
    {
        this.createtime = createtime;
    }

}
