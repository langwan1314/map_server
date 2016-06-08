package com.youngo.msg.service.analyze;

import com.googlecode.protobuf.format.JsonFormat;
import com.youngo.core.common.DaemonThreadFactory;
import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMBuddy;
import com.youngo.core.model.protobuf.SocketResult;
import com.youngo.msg.controller.ChannelCache;
import com.youngo.msg.mapper.FriendShipMapper;
import com.youngo.msg.model.SnubbedRecommendInfo;
import com.youngo.msg.service.apns.ApnsServiceDelegate;
import com.youngo.msg.smapper.SnubbMapper;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by 浮沉 on 2016/3/23.
 * 通过分析，找出想找人聊天，但没有人回复的用户，给这ta推荐一些同样处境的用户
 */
@Service
public class SnubbedUserAnalyze
{
    private static Map<String,Long> snubbingdMap = new ConcurrentSkipListMap<>(); //待推送的用户集合
    private static final long snubTime = TimeUnit.SECONDS.toMillis(30);
    private static final long snubDelay = TimeUnit.DAYS.toMillis(1);

    private static Set<String> oldUserCache = new ConcurrentSkipListSet<>();//如果一个用户是老用户，记录下来，避免重复查询
    @Autowired
    private FriendShipMapper friendShipMapper;
    @Autowired
    private SnubbMapper snubbMapper;
    @Autowired
    private ApnsServiceDelegate apnsServiceDelegate;
    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());

    @PostConstruct
    public void init()
    {
        executorService.scheduleWithFixedDelay(new SnubAnalyzeTask(),30,30, TimeUnit.SECONDS);
    }

    public void messageReceived(String fromId , String toId)
    {
        if(needPush(fromId))
        {
            snubbingdMap.put(fromId, System.currentTimeMillis());
            snubbingdMap.remove(toId);
        }
    }

    /**
     * 1、好友数目大于5的用户不做冷落推荐
     * 2、任何一个用户，冷落推荐次数不大于3
     * 3、24小时内不重复推荐
     * @param userId 用户ID
     * @return 是否需要冷落推荐
     */
    private boolean needPush(String userId)
    {
        if(oldUserCache.contains(userId))
        {
            return false;
        }
        int friendCount = friendShipMapper.getFriendCount(userId);
        if(friendCount>5)
        {
            oldUserCache.add(userId);
            return false;
        }
        SnubbedRecommendInfo snubbedInfo = snubbMapper.getSnubbedInfo(userId);
        if(snubbedInfo!=null)
        {
            int snubbedTimes = snubbedInfo.getSnubbedTimes();
            if (snubbedTimes>5)
            {
                oldUserCache.add(userId);
                return false;
            }
            long lastSnubbTime = snubbedInfo.getLastSnubbTime();
            if(lastSnubbTime != 0 && snubDelay > (System.currentTimeMillis() - lastSnubbTime))
            {
                return false;
            }
        }
        return true;
    }

    private class SnubAnalyzeTask implements Runnable
    {
        @Override
        public void run()
        {
            Iterator<Map.Entry<String, Long>> iterator = snubbingdMap.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry<String, Long> next = iterator.next();
                long time = next.getValue();
                String userId = next.getKey();
                // Reader is idle - set a new timeout and notify the callback.
                long nextDelay = snubTime - (System.currentTimeMillis() - time);
                if (nextDelay <= 0)//表示snubTime周期内未收到回复
                {
                    iterator.remove();
                    push(userId);
                    snubbMapper.fireRecommend(userId);
                }
            }
        }

        private void push(String userId)
        {
            Channel channel = ChannelCache.getInstance().get(userId);
            if(channel!=null && channel.isActive())
            {
                SocketResult result = new SocketResult();
                Header header = new Header((short)IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE,(short)IMBaseDefine.BuddyListCmdID.CID_BUDDY_SNUBB_RECOMMEND_VALUE);
                IMBuddy.IMBuddySnubbRecommend recommend = IMBuddy.IMBuddySnubbRecommend.getDefaultInstance();
                result.setHeader(header);
                result.setBody(recommend.toByteArray());
                channel.writeAndFlush(result);
            }else
            {
                IMBuddy.IMBuddySnubbRecommend notify = IMBuddy.IMBuddySnubbRecommend.getDefaultInstance();
                String data = JsonFormat.printToString(notify);
                apnsServiceDelegate.push(IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE,IMBaseDefine.BuddyListCmdID.CID_BUDDY_SNUBB_RECOMMEND_VALUE,data,userId);
            }
        }
    }

}
