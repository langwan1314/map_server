package com.youngo.mobile.smapper;

import com.youngo.ssdb.core.DefaultValueOperations;
import com.youngo.ssdb.core.SsdbConstants;
import com.youngo.ssdb.core.ValueOperations;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by 浮沉 on 2016/4/8.
 */
@Repository
public class FriendInviteSSDBMapper implements SsdbConstants.FriendInvite
{
    private ValueOperations<String, Integer> valueOperations;

    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.valueOperations = new DefaultValueOperations<>(ssdb, String.class, Integer.class);
    }

    public int getUnReadCount(String userId)
    {
        Integer value = valueOperations.get(friendInviteUnread + userId);
        if(value==null)
        {
            value = 0;
        }
        return value;
    }

    /**
     * 增加未读消息数
     * @param userId 用户ID
     * @param increment 增量
     * @return 增加后的未读消息数
     */
    public long increaseUnReadCount(String userId , int increment)
    {
        return valueOperations.increment(friendInviteUnread+userId,increment);
    }

    public void clearUnReadCount(String userId)
    {
        valueOperations.delete(friendInviteUnread + userId);
    }

}
