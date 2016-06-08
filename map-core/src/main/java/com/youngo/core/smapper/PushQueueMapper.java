package com.youngo.core.smapper;

import com.youngo.core.model.push.PushBriefInfo;
import com.youngo.ssdb.core.DefaultListOperations;
import com.youngo.ssdb.core.DefaultSortedSetOperations;
import com.youngo.ssdb.core.SortedSetOperations;
import com.youngo.ssdb.core.SsdbConstants;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 浮沉 on 2016/5/11.
 * 待推送消息队列
 */
@Repository
public class PushQueueMapper
{
    private DefaultListOperations<String, PushBriefInfo> operations;//会话列表，会话

    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.operations = new DefaultListOperations<>(ssdb, String.class, PushBriefInfo.class);
    }

    public List<PushBriefInfo> list(String userId)
    {
        return operations.range(getListName(userId), 0, 3);
    }

    public void add(String userId , PushBriefInfo briefInfo)
    {
        String name = getListName(userId);
        Integer size = operations.push(name, briefInfo);
        if(size>3)
        {
            operations.deleteFront(name , size-3);
        }
    }

    public void deleteFront(String userId)
    {
        operations.deleteFront(getListName(userId),1);
    }

    private String getListName(String userId)
    {
        return SsdbConstants.pushQueuePrefix + userId;
    }
}
