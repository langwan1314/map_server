/**
 * @Title: OrderEnum.java
 * @Package com.yjh.admin.grid.enumeration
 * @Description: 排序枚举
 * @author yiyan
 * @date 2014-11-20 下午2:13:39
 * @version
 */
package com.youngo.admin.grid.enumeration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 类名称：OrderEnum
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-11-20 下午2:13:39
 * 修改人：yiyan
 * 修改时间：2014-11-20 下午2:13:39
 * 修改备注：
 * 
 * @version
 */
public enum OrderEnum
{
    ASC(0, "asc"), DESC(1, "desc");

    private Integer value;
    private String description;

    private static final Map<Integer, OrderEnum> lookupByInteger = new HashMap<Integer, OrderEnum>();
    private static final Map<String, OrderEnum> lookupByString = new HashMap<String, OrderEnum>();

    static
    {
        for (OrderEnum r : EnumSet.allOf(OrderEnum.class))
            lookupByInteger.put(r.getValue(), r);

        for (OrderEnum r : EnumSet.allOf(OrderEnum.class))
            lookupByString.put(r.getDescription(), r);
    }

    /**
     * @Title:
     * @Description: 私有构造方法
     * @param @param value
     * @param @param description
     * @return OrderEnum
     * @throws
     */
    OrderEnum(Integer value, String description)
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
    public static OrderEnum getEnum(int code)
    {
        return lookupByInteger.get(code);
    }

    /**
     * @param code
     * @return
     */
    public static OrderEnum getEnum(String code)
    {
        return getEnum(code, false);
    }

    /**
     * @param code
     * @param caseSensitive
     * @return
     */
    public static OrderEnum getEnum(String code, Boolean caseSensitive)
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
