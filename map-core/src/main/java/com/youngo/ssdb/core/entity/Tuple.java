package com.youngo.ssdb.core.entity;

/**
 * 
 * @author zhezhiren
 *
 * @param <K>
 * @param <V>
 * @since 1.0
 */
public interface Tuple<K, V> {
	K getKey();
	
	void setKey(K key);
	
	V getValue();
	
	void setValue(V v);
}
