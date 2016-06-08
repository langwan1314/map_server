/**
 * @Title: SearchFilter.java
 * @Package com.yjh.admin.model
 * @Description: 搜索过滤模型
 * @author yiyan
 * @date 2014-11-19 下午3:46:27
 * @version
 */
package com.youngo.admin.grid;

import java.util.List;

/**
 * 类名称：SearchFilter
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-11-19 下午3:46:27
 * 修改人：yiyan
 * 修改时间：2014-11-19 下午3:46:27
 * 修改备注：
 * 
 * @version
 */
public class SearchFilter
{
    private String groupOp;
    private List<SearchRule> rules;

    /**
     * @return the groupOp
     */
    public String getGroupOp()
    {
        return groupOp;
    }

    /**
     * @param groupOp the groupOp to set
     */
    public void setGroupOp(String groupOp)
    {
        this.groupOp = groupOp;
    }

    /**
     * @return the rules
     */
    public List<SearchRule> getRules()
    {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(List<SearchRule> rules)
    {
        this.rules = rules;
    }

    /*
     * (non-Javadoc)
     * <p>Title: toString</p>
     * <p>Description: </p>
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "SearchFilter [groupOp=" + groupOp + ", rules=" + rules + "]";
    }

}
