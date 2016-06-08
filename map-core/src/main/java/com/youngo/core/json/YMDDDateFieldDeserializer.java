package com.youngo.core.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class YMDDDateFieldDeserializer extends JsonDeserializer<Date>
{
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
	{
		String valueAsString = jp.getValueAsString();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date parse;
		try
		{
			parse = formatter.parse(valueAsString);
		} catch (ParseException e)
		{
			throw new IOException("日期解析出错 ： "+valueAsString,e);
		}
		return parse;
	}

}
