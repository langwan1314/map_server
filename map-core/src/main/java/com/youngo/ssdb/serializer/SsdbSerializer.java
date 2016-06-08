package com.youngo.ssdb.serializer;

import com.youngo.core.exception.SerializationException;

/**
 * 
 * @author zhezhiren
 * 
 * @param <T>
 * @since 1.0
 */
public interface SsdbSerializer<T>
{
	/**
	 * Serialize the given object to binary data.
	 * 
	 * @param t object to serialize
	 * @return the equivalent binary data
	 */
	byte[] serialize(Object t) throws SerializationException;

	/**
	 * Deserialize an object from the given binary data.
	 * 
	 * @param bytes object binary representation
	 * @return the equivalent object instance
	 */
	T deserialize(byte[] bytes) throws SerializationException;
}
