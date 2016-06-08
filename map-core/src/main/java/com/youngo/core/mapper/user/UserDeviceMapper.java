package com.youngo.core.mapper.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.youngo.core.model.user.UserDevice;


/**
 * 
 * @Title: UserDeviceMapper.java 
 * @Package com.yjh.core.mapper.user 
 * @Description: 用户设备 
 * @author atao 
 * @date 2015-1-5 下午2:13:28 
 * @version V1.0
 */
public interface UserDeviceMapper
{
    
     /**
      * 添加
      * @param userDevice
      * @return
      */
     int insert(UserDevice userDevice);
    
    int update(UserDevice userDevice);
    
    int updateById(UserDevice userDevice);
    
    UserDevice getDetailInfo(Map<String, Object> param);
    
    List<UserDevice> getDetailList(Map<String, Object> param);



    /**
     * 通过条件查询到对应所有RegistrationId 以，分隔
     * @return
     */
    List<String> getRegistrationIdsBycondition(Map<String, Object> param);

    /**
     * 通过条件查询到对应所有RegistrationId 以，分隔
     * @return
     */
    int getRegistrationIdsByconditionCount(Map<String, Object> param);

    /**
     * 将设备对应所有记录都设置成未登录状态
     * @Title: disableUserLoginByRegistrationId
     * @author atao
     * @date 2015-1-21 上午9:45:57
     * @return int
     */
    int disableLoginByUser(@Param("userId") String userId, @Param("registrationId") String registration_id);




}
