package com.youngo.core.mapper.version;


import com.youngo.core.model.version.Version;

import java.util.List;
import java.util.Map;

/**
 * @author fuchen
 */
public interface VersionMapper
{
    List<Version> get(String clientType);

    /**
     * @return the List<Version>
     * @Title: list
     * @Description: TODO
     * @author zhangyu 2015-01-15
     */
    List<Version> list(Map<String, Object> param);


    int insert(Version version);

    /**
     * @param param
     * @return the int
     * @Title: count
     * @Description: TODO
     */
    int count(Map<String, Object> param);
}
