package com.youngo.core.smapper;

import com.youngo.core.model.msg.SessionConfigure;
import com.youngo.ssdb.core.DefaultHashMapOperations;
import com.youngo.ssdb.core.HashMapOperations;
import com.youngo.ssdb.core.SsdbConstants;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by 浮沉 on 2016/4/28.
 */
@Repository
public class SessionConfigureMapper extends ChatBaseMapper
{
    private HashMapOperations<String, String, SessionConfigure> sessionConfigureOp;//聊天会话设置

    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.sessionConfigureOp = new DefaultHashMapOperations<>(ssdb,String.class,String.class,SessionConfigure.class);
    }
    /**
     * @param userId 用户id
     * @param peerId 聊天对方的用户id
     * @return 会话设置信息
     */
    public SessionConfigure getSessionConfigure(String userId , String peerId)
    {
        String name = SsdbConstants.Chat.chatSessionConfigPrefix+userId;
        String sessionName = "1_"+peerId;
        return this.sessionConfigureOp.get(name, sessionName);
    }

    public Map<String, SessionConfigure> getAllConfigures(String userId)
    {
        String name = SsdbConstants.Chat.chatSessionConfigPrefix+userId;
        Map<String, SessionConfigure> all = sessionConfigureOp.getAllByName(name);
        if(all==null)
        {
            all = new HashMap<>();
        }
        return all;
    }

    public List<SessionConfigure> list(String userId)
    {
        String name = SsdbConstants.Chat.chatSessionConfigPrefix+userId;
        Map<String, SessionConfigure> all = sessionConfigureOp.getAllByName(name);
        if(all!=null)
        {
            Collection<SessionConfigure> values = all.values();
            return new ArrayList<>(values);
        }else
        {
            return new ArrayList<>();
        }
    }

    /**
     * 保存聊天会话设置
     * @param userId    用户ID
     * @param peerId    聊天对象的用户ID
     * @param configure 聊天会话设置<br>
     */
    public void save(String userId , String peerId , SessionConfigure configure)
    {
        String name = SsdbConstants.Chat.chatSessionConfigPrefix+userId;
        String sessionName = "1_"+peerId;
        sessionConfigureOp.set(name,sessionName,configure);
    }
}
