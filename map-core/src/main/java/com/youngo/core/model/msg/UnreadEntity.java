package com.youngo.core.model.msg;

/**
 * @author : yingmu on 15-1-6.
 * @email : yingmu@mogujie.com.
 * <p>
 * 未读session实体，并未保存在DB中
 */
public class UnreadEntity
{
    private int peerId;
    private int sessionType;
    private int unReadCnt;

    public int getPeerId()
    {
        return peerId;
    }

    public void setPeerId(int peerId)
    {
        this.peerId = peerId;
    }

    public int getSessionType()
    {
        return sessionType;
    }

    public void setSessionType(int sessionType)
    {
        this.sessionType = sessionType;
    }

    public int getUnReadCnt()
    {
        return unReadCnt;
    }

    public void setUnReadCnt(int unReadCnt)
    {
        this.unReadCnt = unReadCnt;
    }
}
