package com.youngo.mobile.mapper.friendship;

import java.util.List;
import java.util.Map;

import com.youngo.core.model.user.UserBrief;

/**
 * Created by fuchen on 2015/12/11.
 * 语伴推荐
 */
public interface RecommendMapper
{
    List<UserBrief> list(Map<String, Object> params);

    List<UserBrief> listNearBy(Map<String, Object> params);

    /**
     * 列出感兴趣语言相通的用户
     * @param params
     * @return
     */
    List<UserBrief> listSameInterest(Map<String, Object> params);

    /**
     * 列出异性
     * @param params
     * @return
     */
    List<UserBrief> listOppositeSex(Map<String, Object> params);

    List<UserBrief> listIntroduction(Map<String, Object> params);

    int count(Map<String, Object> params);
}
