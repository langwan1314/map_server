package com.youngo.mobile.mapper.user;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.youngo.mobile.model.user.Gallery;

public interface GalleryMapper {
    int insert(Gallery record);
    
    int insertList(@Param("records")List<Gallery> records);
    
    Gallery selectById(@Param("id")Integer id);
    
    List<Gallery> selectByUserId(@Param("userId")Integer userId);
    
    List<String> selectUrlByUserId(@Param("userId")Integer userId);
    
    int deleteByUserId(@Param("userId")Integer userId);
    
    int deleteById(@Param("id")Integer id);
}