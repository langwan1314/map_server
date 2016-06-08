package com.youngo.mobile.mapper.user;

import java.util.List;
import java.util.Map;

import com.youngo.mobile.model.user.Socialization;

public interface SocializationMapper {
	
	int insert(Socialization record);
	
	int updateByPrimaryKeySelective(Socialization record);
	
	int updateByPrimaryKey(Socialization record);

    int deleteByPrimaryKey(Integer id);

    List<Socialization> select(Map<String, Object> param);
    
    Socialization selectBySocialAndType(Map<String, Object> param);

    Socialization selectByPrimaryKey(Integer id);
}