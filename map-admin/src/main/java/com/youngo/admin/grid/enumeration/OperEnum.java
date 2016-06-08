/**
 * @Title: SearchOper.java
 * @Package com.yjh.admin.model
 * @Description: 搜索操作类
 * @author yiyan
 * @date 2014-11-19 下午4:32:55
 * @version
 */
package com.youngo.admin.grid.enumeration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 类名称：SearchOper
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-11-19 下午4:32:55
 * 修改人：yiyan
 * 修改时间：2014-11-19 下午4:32:55
 * 修改备注：
 * eq: This means the column is equal to
 * ne: This means the column is not equal to
 * lt: This means the column is less than
 * le: This means the column is less than or equals
 * gt: This means the column is greater than
 * ge: This means the column is greater than or equals
 * bw: This means the column begins with
 * bn: This means the column does not begin with
 * in: This means the column is in an array ofvalues
 * ni: This means the column is not in an array ofvalues
 * ew: This means the column ends with
 * en: This means the column does not end with
 * cn: This means the column contains
 * nc: This means the column does not contain
 * nu: This means the column is not null
 * nn: This means the column is null
 * 
 * @version
 */
public enum OperEnum
{

    EQUAL(0, "eq"), NOT_EQUAL(1, "ne"), LESS_THAN(2, "lt"), GREATER_THAN(3, "gt"), GREATER_EQUAL(4, "ge"), LESSER_EQUAL(
            5, "le"), ENDS_WITH(6, "ew"), BEGINS_WITH(7, "bw"), CONTAINS(8, "cn");

    private Integer value;
    private String description;
    private static final Map<Integer, OperEnum> lookupByInteger = new HashMap<Integer, OperEnum>();
    private static final Map<String, OperEnum> lookupByString = new HashMap<String, OperEnum>();

    static
    {
        for (OperEnum r : EnumSet.allOf(OperEnum.class))
            lookupByInteger.put(r.getValue(), r);

        for (OperEnum r : EnumSet.allOf(OperEnum.class))
            lookupByString.put(r.getDescription(), r);
    }

    /**
     * @Title:
     * @Description: 私有构造方法
     * @param @param value
     * @param @param description
     * @return OperEnum
     * @throws
     */
    OperEnum(Integer value, String description)
    {
        this.value = value;
        this.description = description;
    }

    /**
     * @return
     */
    public Integer getValue()
    {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(Integer value)
    {
        this.value = value;
    }

    /**
     * @return
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @param code
     * @return
     */
    public static OperEnum getEnum(int code)
    {
        return lookupByInteger.get(code);
    }

    /**
     * @param code
     * @return
     */
    public static OperEnum getEnum(String code)
    {
        return getEnum(code, false);
    }

    /**
     * @param code
     * @param caseSensitive
     * @return
     */
    public static OperEnum getEnum(String code, Boolean caseSensitive)
    {
        if (caseSensitive == true)
        {
            return lookupByString.get(code);
        }
        else
        {
            return lookupByString.get(code.toLowerCase());
        }
    }

}
