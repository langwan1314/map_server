package com.youngo.core.mapper.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.youngo.core.model.user.User;

/**
 * Created by fuchen on 2015/11/20.
 * 用户,增删改查
 */
public interface UserMapper
{
	Integer insert(User user);
	
	//public Integer updateByUserName(User user);//TODO
	
	Integer updateByUserId(User user);
	
	Integer updateUserIntroduce(User user);
	
	Integer checkNickNameExsits(@Param("nickName") String nickName);
	
    List<User> list(Map<String, Object> params);

    int count(Map<String, Object> params);
    
    User getUserByUserName(String userName);

    User getById(String id);
    
    List<User> getByIds(@Param("ids")List<String> ids);
    
    Integer register(User user);
}
