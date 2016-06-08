/**
 * @Title: SearchRule.java
 * @Package com.yjh.admin.model
 * @Description: 搜索规则模型
 * @author yiyan
 * @date 2014-11-19 下午3:47:54
 * @version
 */
package com.youngo.admin.grid;

/**
 * 类名称：SearchRule
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-11-19 下午3:47:54
 * 修改人：yiyan
 * 修改时间：2014-11-19 下午3:47:54
 * 修改备注：
 * 
 * @version
 */
public class SearchRule
{
    private String field;
    private String op;
    private String data;

    /**
     * @return the field
     */
    public String getField()
    {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field)
    {
        this.field = field;
    }

    /**
     * @return the op
     */
    public String getOp()
    {
        return op;
    }

    /**
     * @param op the op to set
     */
    public void setOp(String op)
    {
        this.op = op;
    }

    /**
     * @return the data
     */
    public String getData()
    {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data)
    {
        this.data = data;
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
        return "SearchRule [field=" + field + ", op=" + op + ", data=" + data + "]";
    }
}
