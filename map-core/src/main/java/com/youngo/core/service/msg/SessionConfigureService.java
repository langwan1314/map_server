package com.youngo.core.service.msg;

import com.youngo.core.model.msg.SessionConfigure;
import com.youngo.core.smapper.SessionConfigureMapper;
import com.youngo.ssdb.core.SsdbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by 浮沉 on 2016/4/28.
 */
@Service
public class SessionConfigureService
{
    @Autowired
    private SessionConfigureMapper sessionConfigureMapper;
    /**
     * @param userId 用户id
     * @param peerId 聊天对方的用户id
     * @return 会话设置信息
     */
    public SessionConfigure getSessionConfigure(String userId , String peerId)
    {
        return sessionConfigureMapper.getSessionConfigure(userId,peerId);
    }

    public Map<String, SessionConfigure> getAllConfigures(String userId)
    {
        return sessionConfigureMapper.getAllConfigures(userId);
    }

    public List<SessionConfigure> list(String userId)
    {
       return sessionConfigureMapper.list(userId);
    }

    public void save(String userId , String peerId ,SessionConfigure configure)
    {
        sessionConfigureMapper.save(userId,peerId,configure);
    }
}
