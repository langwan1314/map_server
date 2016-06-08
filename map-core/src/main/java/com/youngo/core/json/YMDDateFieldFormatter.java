package com.youngo.core.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author 浮沉
 * 只显示年月日
 */
public class YMDDateFieldFormatter extends JsonSerializer<Date>
{

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(value);
        jgen.writeString(formattedDate);
    }
}