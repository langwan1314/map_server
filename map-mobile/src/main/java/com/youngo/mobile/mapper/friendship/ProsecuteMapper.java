package com.youngo.mobile.mapper.friendship;

import com.youngo.core.model.friendship.ProseCute;

import java.util.Map;

/**
 * Created by 浮沉 on 2016/3/14.
 * 举报，ORmapping类
 */
public interface ProsecuteMapper
{
    /**
     * 根据ID查询举报记录
     * @param id
     * @return
     */
    public ProseCute get(String id);

    public int insert(Map<String,String> proseCute);
}
