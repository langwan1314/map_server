package com.youngo.ssdb.serializer;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.youngo.core.exception.SerializationException;

/**
 * 
 * @author zhezhiren
 *
 * @param <T>
 * @since 1.0
 */
public class Jackson2JsonSsdbSerializer<T> implements SsdbSerializer<T>{
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	private ObjectMapper objectMapper ;//线程安全  但是否造成多线程竞争资源，需要考虑，能否避免资源竞争。
	
	private Type type;
	
	public Jackson2JsonSsdbSerializer(Class<T> type){
		this.type = type;
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@Override
	public byte[] serialize(Object t) throws SerializationException {
		if (t == null) {
			return SerializerUtils.EMPTY_ARRAY;
		}
		try {
			return this.objectMapper.writeValueAsBytes(t);
		} catch (Exception ex) {
			throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (SerializerUtils.isEmpty(bytes)) {
			return null;
		}
		try {
			return this.objectMapper.readValue(bytes, 0, bytes.length, (Class<T>) type);
		} catch (Exception ex) {
			throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}
		
	protected JavaType getJavaType(Class<?> clazz) {
		return TypeFactory.defaultInstance().constructType(clazz);
	}
}
