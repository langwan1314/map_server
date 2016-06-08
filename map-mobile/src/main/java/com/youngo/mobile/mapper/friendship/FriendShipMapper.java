package com.youngo.mobile.mapper.friendship;

import com.youngo.mobile.model.friendship.FriendShip;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by fuchen on 2015/11/19.
 */
public interface FriendShipMapper
{
    /**
     * 查询好友信息
     * id：直接根据记录的ID查找
     * userId:用户ID
     * friendId:好友ID
     * @param params
     * @return
     */
    FriendShip get(Map<String, String> params);
    /**
     * @param userId 用户ID
     * @return 返回该用户的所有好友信息
     */
    List<FriendShip> list(String userId);

    /**
     * 插入一条好友信息
     * @param userId 用户ID
     * @param friendId 好友的用户ID
     */
    void insert(@Param("userId") String userId, @Param("friendId") String friendId);

    /**
     * 删除好友
     * userId 用户ID,friendId 朋友的用户ID<br>
     * id 数据库记录的唯一ID
     */
    void remove(Map<String, String> params);

}
