/**
 * @Title: GroupEnum.java
 * @Package com.yjh.admin.grid.enumeration
 * @Description: 组合条件枚举
 * @author yiyan
 * @date 2014-11-20 下午2:20:33
 * @version
 */
package com.youngo.admin.grid.enumeration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 类名称：GroupEnum
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-11-20 下午2:20:33
 * 修改人：yiyan
 * 修改时间：2014-11-20 下午2:20:33
 * 修改备注：
 * 
 * @version
 */
public enum GroupEnum
{
    AND(0, "AND"), OR(1, "OR");

    private Integer value;
    private String description;
    private static final Map<Integer, GroupEnum> lookupByInteger = new HashMap<Integer, GroupEnum>();
    private static final Map<String, GroupEnum> lookupByString = new HashMap<String, GroupEnum>();

    static
    {
        for (GroupEnum r : EnumSet.allOf(GroupEnum.class))
            lookupByInteger.put(r.getValue(), r);

        for (GroupEnum r : EnumSet.allOf(GroupEnum.class))
            lookupByString.put(r.getDescription(), r);
    }

    /**
     * @Title:
     * @Description: 私有构造方法
     * @param @param value
     * @param @param description
     * @return GroupEnum
     * @throws
     */
    GroupEnum(Integer value, String description)
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
    public static GroupEnum getEnum(int code)
    {
        return lookupByInteger.get(code);
    }

    /**
     * @param code
     * @return
     */
    public static GroupEnum getEnum(String code)
    {
        return getEnum(code, false);
    }

    /**
     * @param code
     * @param caseSensitive
     * @return
     */
    public static GroupEnum getEnum(String code, Boolean caseSensitive)
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
