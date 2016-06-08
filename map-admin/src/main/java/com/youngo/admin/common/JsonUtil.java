/**
 * @Title: JsonUtil.java
 * @Package com.yjh.admin.common
 * @Description: Json 帮助类
 * @author yiyan
 * @date 2015-2-10 下午1:42:45
 * @version
 */
package com.youngo.admin.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 类名称：JsonUtil
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2015-2-10 下午1:42:45
 * 修改人：yiyan
 * 修改时间：2015-2-10 下午1:42:45
 * 修改备注：
 * 
 * @version
 */
public class JsonUtil
{
    static ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static String toJson(Object object) throws JsonProcessingException
    {
        return objectMapper.writeValueAsString(object);
    }

}
