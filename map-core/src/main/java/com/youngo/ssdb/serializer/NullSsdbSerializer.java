package com.youngo.ssdb.serializer;

import com.youngo.core.exception.SerializationException;

/**
 * 
 * @author zhezhiren
 * 
 * @since 1.0
 */
public class NullSsdbSerializer implements SsdbSerializer<byte[]>{
	public static NullSsdbSerializer nullSsdbSerializer = new NullSsdbSerializer();

	@Override
	public byte[] serialize(Object t) throws SerializationException {
		return (byte[])t;
	}

	@Override
	public byte[] deserialize(byte[] bytes) throws SerializationException {
		return bytes;
	}
	
}
