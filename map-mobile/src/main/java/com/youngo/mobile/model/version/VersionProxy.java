package com.youngo.mobile.model.version;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author fuchen
 */
public class VersionProxy
{
    private boolean needUpdate;// 是否需要升级
    private Version target;// 真实的版本数据

    public boolean isNeedUpdate()
    {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate)
    {
        this.needUpdate = needUpdate;
    }

    public String getLastVersion()
    {
        if (target == null)
        {
            return null;
        }
        return target.getVersionNumber();
    }

    public String getVersionName()
    {
        if (target == null)
        {
            return null;
        }
        return target.getVersionName();
    }

    public Boolean isForcibly()
    {
        if (target == null)
        {
            return null;
        }
        return target.isForcibly();
    }

    public String getDescription()
    {
        if (target == null)
        {
            return null;
        }
        return target.getDescription();
    }

    public String getUrl()
    {
        if (target == null)
        {
            return null;
        }
        return target.getUrl();
    }

    @JsonIgnore
    public void setTarget(Version target)
    {
        this.target = target;
    }
}
