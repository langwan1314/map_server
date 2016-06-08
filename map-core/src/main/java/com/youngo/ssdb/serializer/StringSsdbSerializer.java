package com.youngo.ssdb.serializer;

import java.io.UnsupportedEncodingException;

import com.youngo.core.exception.SerializationException;

/**
 * 
 * 
 * null字符都存储后，获取到的都是""字符
 * @author zhezhiren
 * 
 * @since 1.0
 */
public class StringSsdbSerializer implements SsdbSerializer<String> {
	
	@Override
	public byte[] serialize(Object t) throws SerializationException {
		if(null == t){
			return SerializerUtils.EMPTY_ARRAY;
		}
		try {
			return t.toString().getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new SerializationException("not surported chartset utf-8", e);
		}
	}

	@Override
	public String deserialize(byte[] bytes) throws SerializationException {
		if (SerializerUtils.isEmpty(bytes)) {
			return "";
		}
		try {
			return new String(bytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new SerializationException("not surported chartset utf-8", e);
		}
	}
}
