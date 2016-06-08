package com.youngo.mobile.controller.version;

import com.youngo.core.mapper.version.VersionMapper;
import com.youngo.core.model.version.Version;
import com.youngo.core.model.version.VersionProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author fuchen
 */
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/version")
public class VersionController
{
    @Autowired
    private VersionMapper mapper;

    /**
     * @param clientVersion 客户端版本号
     * @param clientType    客户端类型（android，ios）
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public VersionProxy getLastVersion(@RequestParam String clientVersion, @RequestParam String clientType)
    {
        VersionProxy proxy = new VersionProxy();
        List<Version> versions = mapper.get(clientType);//获取所有版本号
        if(versions==null || versions.isEmpty())
        {
            return proxy;
        }
        int index = -1;
        //先定位到当前版本
        for (int i=0; i < versions.size(); i++)
        {
            if (versions.get(i).getVersionNumber().equals(clientVersion))
            {
                index = i;
                break;
            }
        }
        if(index==-1)//未知版本
        {
            Version lastVersion = versions.get(versions.size() - 1);
            if(lastVersion.getVersionNumber().compareTo(clientVersion)>0)
            {
                proxy.setNeedUpdate(true);
                lastVersion.setForcibly(true);
                proxy.setTarget(lastVersion);
            }
        }else if (index < versions.size() - 1)
        {
            proxy.setNeedUpdate(true);
            Version lastVersion = versions.get(versions.size() - 1);
            //根据比当前版本新的版本的forcibly信息判断是否强制升级
            for (int i = index + 1; i < versions.size(); i++)
            {
                if (versions.get(i).isForcibly())
                {
                    lastVersion.setForcibly(true);
                    break;
                }
            }
            proxy.setTarget(lastVersion);
        }
        return proxy;
    }
}
