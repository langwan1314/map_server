package com.youngo.ssdb.serializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.youngo.ssdb.core.entity.SimpleTuple;
import com.youngo.ssdb.core.entity.Tuple;

/**
 * 
 * @author zhezhiren
 * 
 * @since 1.0
 */
public class SerializerUtils {
	static NullSsdbSerializer nullSsdbSerializer = new NullSsdbSerializer();
	
	public static final byte[] EMPTY_ARRAY = new byte[0];
	
	static boolean isEmpty(byte[] data) {
		return (data == null || data.length == 0);
	}
	
	/**
	 * 将结果转化为列表
	 * @param rawValues
	 * @param ssdbSerializer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> deserializeValuesAsList(Collection<byte[]> rawValues, SsdbSerializer<T> ssdbSerializer) {
		return deserializeValues(rawValues, List.class, ssdbSerializer);
	}
	
	/**
	 * 将结果转化为Set
	 * @param rawValues
	 * @param ssdbSerializer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> deserializeValuesAsSet(Collection<byte[]> rawValues, SsdbSerializer<T> ssdbSerializer) {
		return deserializeValues(rawValues, Set.class, ssdbSerializer);
	}
	
	/**
	 * 将结果转化为集合序列
	 * @param rawValues
	 * @param type
	 * @param ssdbSerializer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static <T extends Collection<?>> T deserializeValues(Collection<byte[]> rawValues, Class<T> type,
			SsdbSerializer<?> ssdbSerializer) {
		if (rawValues == null) {
			return null;
		}

		Collection<Object> values = (List.class.isAssignableFrom(type) ? new ArrayList<Object>(rawValues.size())
				: new LinkedHashSet<Object>(rawValues.size()));
		for (byte[] bs : rawValues) {
			values.add(ssdbSerializer.deserialize(bs));
		}

		return (T) values;
	}
	
	/**
	 * 将结果转化为tuple键值对列表
	 * @param rawValues
	 * @param keySerializer
	 * @param valueSerializer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> List<Tuple<K, V>> deserializeValuesAsTupleList(List<byte[]> rawValues, SsdbSerializer<K> keySerializer, SsdbSerializer<V> valueSerializer) {
		if (rawValues == null) {
			return null;
		}
		if(rawValues.size() % 2 != 0){
			throw new RuntimeException();//TODO 键值对不正确，错误的方法调用。
		}
		
		ArrayList<Tuple<K, V>> result = new ArrayList<Tuple<K, V>>(rawValues.size() / 2);
		for(int i = 0, j = rawValues.size() / 2; i < j; i++){
			byte[] keyBytes = rawValues.get(i * 2);
			byte[] valueBytes = rawValues.get(i * 2 + 1);
			K key = null;
			V value = null;
			if(keySerializer != null){
				key = keySerializer.deserialize(keyBytes);
			}else{
				key = (K) keyBytes;
			}
			if(null != valueSerializer){
				value = valueSerializer.deserialize(valueBytes);
			}else{
				value= (V) valueBytes;
			}
			Tuple<K, V> tuple = new SimpleTuple<K, V>(key, value);
			tuple.setKey(key);
			tuple.setValue(value);
			result.add(tuple);
		}
		return result;
	}
	
	
	/**
	 * 将结果转换为hashMap键值对
	 * @param rawValues
	 * @param keySerializer
	 * @param valueSerializer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K, V> deserializeValuseAsMap(List<byte[]> rawValues, SsdbSerializer<K> keySerializer, SsdbSerializer<V> valueSerializer) {
		if (rawValues == null) {
			return null;
		}
		if(rawValues.size() % 2 != 0){
			throw new RuntimeException();//TODO 键值对不正确，错误的方法调用。
		}
		
		Map<K, V> result = new HashMap<K, V>(rawValues.size() / 2);
		for(int i = 0, j = rawValues.size() / 2; i < j; i++){
			byte[] keyBytes = rawValues.get(i * 2);
			byte[] valueBytes = rawValues.get(i * 2 + 1);
			K key = null;
			V value = null;
			if(keySerializer != null){
				key = keySerializer.deserialize(keyBytes);
			}else{
				key = (K) keyBytes;
			}
			if(null != valueSerializer){
				value = valueSerializer.deserialize(valueBytes);
			}else{
				value= (V) valueBytes;
			}
			result.put(key, value);
		}
		return result;
	}
}