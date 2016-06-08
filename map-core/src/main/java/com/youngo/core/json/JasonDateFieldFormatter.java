/**
 * @Title: JasonDateFieldFormatter.java
 * @Package com.yjh.core.common.json
 * @Description:
 * @author fuchen
 * @date 2014-11-5 上午10:58:31
 * @version
 */

package com.youngo.core.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 类名称：JasonDateFieldFormatter
 * 类描述：定义将日期字段转换成jason时的转换格式<br>
 * 创建人：浮沉
 * 创建时间：2014-11-5 上午10:58:31
 * 使用方式：@JsonSerialize(using = JasonDateFieldFormatter.class)
 */
public class JasonDateFieldFormatter extends JsonSerializer<Date>
{

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(value);
        jgen.writeString(formattedDate);
    }
}
