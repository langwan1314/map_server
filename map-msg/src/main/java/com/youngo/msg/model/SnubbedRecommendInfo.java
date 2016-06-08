package com.youngo.msg.model;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 浮沉 on 2016/3/30.
 * 冷落推荐信息<br>
 *     记录冷落推荐的次数，时间
 */
public class SnubbedRecommendInfo
{
    private int snubbedTimes;//已推荐次数
    private long lastSnubbTime;//最后一次推荐的时间

    public int getSnubbedTimes() {
        return snubbedTimes;
    }

    public void setSnubbedTimes(int snubbedTimes) {
        this.snubbedTimes = snubbedTimes;
    }

    public long getLastSnubbTime() {
        return lastSnubbTime;
    }

    public void setLastSnubbTime(long lastSnubbTime) {
        this.lastSnubbTime = lastSnubbTime;
    }
}
