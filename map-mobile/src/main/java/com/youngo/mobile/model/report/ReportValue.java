package com.youngo.mobile.model.report;


import com.youngo.core.model.msg.SessionEntity;
import com.youngo.core.model.msg.UnreadEntity;
import com.youngo.core.model.version.VersionProxy;
import com.youngo.mobile.model.friendship.FriendInfo;

import java.util.Collection;
import java.util.List;

/**
 * @author fuchen <br>
 *         客户端信息上报的返回结果
 **/
public class ReportValue
{
    private VersionProxy version;
    private List<FriendInfo> friends;//联系人列表
    private List<SessionEntity> sessions;//会话列表
    private Collection<UnreadEntity> unreads;//未读消息列表

    public VersionProxy getVersion()
    {
        return version;
    }

    public void setVersion(VersionProxy version)
    {
        this.version = version;
    }

    public List<FriendInfo> getFriends()
    {
        return friends;
    }

    public void setFriends(List<FriendInfo> friends)
    {
        this.friends = friends;
    }

    public List<SessionEntity> getSessions()
    {
        return sessions;
    }

    public void setSessions(List<SessionEntity> sessions)
    {
        this.sessions = sessions;
    }

    public Collection<UnreadEntity> getUnreads() {
        return unreads;
    }

    public void setUnreads(Collection<UnreadEntity> unreads) {
        this.unreads = unreads;
    }
}
