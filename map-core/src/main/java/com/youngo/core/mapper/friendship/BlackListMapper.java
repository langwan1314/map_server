package com.youngo.core.mapper.friendship;

import com.youngo.core.model.friendship.BlackListItem;

import java.util.List;
import java.util.Map;

/**
 * Created by 浮沉 on 2016/3/14.
 */
public interface BlackListMapper
{
    BlackListItem get(String id);

    List<BlackListItem> list(Map<String,String> params);

    int insert(BlackListItem blackListItem);

    void remove(Map<String,String> params);
}
