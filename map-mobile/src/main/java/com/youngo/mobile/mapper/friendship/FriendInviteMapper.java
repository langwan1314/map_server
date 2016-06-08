package com.youngo.mobile.mapper.friendship;

import com.youngo.mobile.model.friendship.FriendInvite;

import java.util.List;
import java.util.Map;

/**
 * Created by fuchen on 2015/11/19.
 */
public interface FriendInviteMapper
{
    /**
     * 根据ID获取添加好友的申请记录
     * @param id
     * @return
     */
    FriendInvite getById(String id);

    /**
     * 条件查询
     * @param params
     * invitee：被邀请人
     * invitor：邀请人
     * status ：好友申请的状态（waiting:等待处理,agree:已同意,refused:已拒绝)
     * @return
     */
    FriendInvite get(Map<String, String> params);

    /**
     * 查询好友申请记录
     * @param params
     * @return
     */
    List<FriendInvite> list(Map<String, String> params);

    /**
     * 插入一条好友申请记录
     * @param friendInvite
     */
    void insert(Map<String, String> friendInvite);

    /**
     * 同意加好友的申请
     * @param params
     * id,
     * invitor,
     * invitee
     */
    void agree(Map<String,String> params);

    /**
     * 拒绝好友申请
     * @param id
     */
    void refuse(String id);

    /**
     * 删除好友申请记录
     * @param id
     */
    void remove(String id);

    /**
     * 重设记录的createtime和expiretime
     * @param id
     */
    void updateTime(String id);

}
