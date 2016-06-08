package com.youngo.admin.mapper.friendship;

import com.youngo.admin.model.friendship.AdminProseCute;

import java.util.List;
import java.util.Map;

/**
 * Created by 浮沉 on 2016/3/15.
 */
public interface AdminProsecuteMapper
{
    /**
     * @param id 举报记录的id（唯一标识）
     * @return 举报记录
     */
    AdminProseCute get(String id);

    List<AdminProseCute> list(Map<String, Object> param);

    int count(Map<String,Object> params);

    void update(Map<String,String> params);
}
