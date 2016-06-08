package com.youngo.msg.smapper;

import com.youngo.msg.model.SnubbedRecommendInfo;
import com.youngo.ssdb.core.DefaultHashMapOperations;
import com.youngo.ssdb.core.HashMapOperations;
import com.youngo.ssdb.core.SsdbConstants;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by 浮沉 on 2016/3/24.
 * 尝试找人聊天但是没有被搭理的人，服务器会尝试主动给他推荐一些语伴<br>
 *     SnubbSSDBService负责记录这些推荐信息<br>
 */
@Repository
public class SnubbMapper
{
    private HashMapOperations<String, String, SnubbedRecommendInfo> recommendOperations;
    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.recommendOperations = new DefaultHashMapOperations<>(ssdb,String.class,String.class,SnubbedRecommendInfo.class);
    }

    public void fireRecommend(String userId)
    {
        SnubbedRecommendInfo info = recommendOperations.get(SsdbConstants.Snubb.buddySnubbRecommend, userId);
        if(info==null)
        {
            info = new SnubbedRecommendInfo();
        }
        info.setSnubbedTimes(info.getSnubbedTimes()+1);
        info.setLastSnubbTime(System.currentTimeMillis());
        recommendOperations.set(SsdbConstants.Snubb.buddySnubbRecommend, userId,info);
    }

    public SnubbedRecommendInfo getSnubbedInfo(String userId)
    {
        return recommendOperations.get(SsdbConstants.Snubb.buddySnubbRecommend, userId);
    }
}
