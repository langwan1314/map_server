package com.youngo.ssdb.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.youngo.ssdb.core.entity.Tuple;

/**
 * 
 * @author zhezhiren
 * @param <N> hash 的name
 * @param <V> hash name.key 对应的value类型
 * @param <E> hash name对应的对象的类型
 * @since 1.0
 */
public interface HashMapOperations<N, K, V> {
	/**
	 * hset
	 * 当结果返回为false时，并不意味着设置失败。结果返回为null时设置才是失败的。
	 * 从返回获取结果。修改成功 返回1，值未修改返回0。（实际是增加了新k则返回为1，否则返回为0）
	 * @param name
	 * @param key
	 * @param val
	 * @return
	 */
	Boolean set(final N name, final K key, final V val);
	
	/**
	 * hget
	 * @param name
	 * @param key
	 * @return
	 */
	V get(final N name, final K key);
	
	/**
	 * hdel
	 * 从返回获取结果
	 * @param name
	 * @param key
	 * @return
	 */
	Boolean delete(final N name, final K key);
	
	/**
	 * hincr
	 * hash值自增指定值
	 * @see ValueOperations.increment方法
	 * @param name
	 * @param key
	 * @param value
	 * @return
	 */
	Integer increment(final N name, final K key, final int value);
	
	/**
	 * hincr
	 * hash值自增指定值
	 * @see ValueOperations.increment方法
	 * @param name
	 * @param key
	 * @param value
	 * @return
	 */
	Long incrementForLong(final N name, final K key, final int value);
	
	/**
	 * hexists
	 * 从返回获取结果
	 * @param name
	 * @param key
	 * @return
	 */
	Boolean exists(final N name, final K key);
	
	/**
	 * hsize
	 * name 对应的hash 不存在时，返回0.
	 * @param name
	 * @return
	 */
	Long size(final N name);
	
	/**
	 * hlist
	 * 返回位于 nameStart 与nameEnd之间的name列表，name排序按照字典排序。
	 * @param nameStart
	 * @param nameEnd
	 * @param limit
	 * @return
	 */
	Set<N> getNames(final N nameStart, final N nameEnd, final int limit);
	
	/**
	 * hrlist
	 * 获取nameStart 与nameEnd之间的name列表， name排序按照字典倒序
	 * @param nameStart
	 * @param nameEnd
	 * @param limit
	 * @return
	 */
	Set<N> getNamesReverse(final N nameStart, final N nameEnd, final int limit);
	
	/**
	 * hkeys
	 * 获取指定hash对象中 key >= keyStart 并且 key <= keyEnd 的key列表，key按照字典排序
	 * @param name
	 * @param keyStart
	 * @param keyEnd
	 * @param limit
	 * @return
	 */
	Set<K> getKeys(final N name, final K keyStart, final K keyEnd, final int limit);
	
	
	/**
	 * hgetall
	 * @param name
	 * @return
	 */
	//TODO
	Map<K, V> getAllByName(final N name);
	
	
	
	/**
	 * hgetall
	 * @param name
	 * @return
	 */
	@Deprecated
	List<Tuple<K, V>> scanAllByNameAsList(final N name);
	
	/**
	 * hscan
	 * @param name
	 * @param keyStart
	 * @param keyEnd
	 * @param limit
	 * @return
	 */
	//TODO
	List<Tuple<K, V>> scanByKeys(final N name, final K keyStart, final K keyEnd, final int limit);
	
	/**
	 * hrscan
	 * @param name
	 * @param keyStart
	 * @param keyEnd
	 * @param limit
	 * @return
	 */
	List<Tuple<K, V>> scanByKeysReverse(final N name, final K keyStart, final K keyEnd, final int limit);
	
	/**
	 * hclear
	 * 返回删除条数
	 * @param name
	 * @return
	 */
	Integer clear(final N name);
	
	/**
	 * multi_hset
	 * 从状态取结果。
	 * @param name
	 * @param values
	 * @return
	 */
	Boolean multiSet(final N name, final Set<Tuple<K, V>> values);
	
	/**
	 * multi_hset
	 * @param name
	 * @param values
	 * @return
	 */
	Boolean multiSet(final N name, final Map<K, V> values);
	
	/**
	 *
	 * multi_hget
	 * @param name
	 * @param keys
	 * @return
	 */
	//TODO 获取map结果
	List<Tuple<K, V>> multiGetByKeys(final N name, final Set<K> keys);
	
	
	Map<K, V> multiGetByKeysAsMap(final N name, final Set<K> keys);
	
	Map<K, V> multiGetByKeysAsMap(final N name, final List<K> keys);
	
	/**
	 * multi_hdel
	 * 从状态取结果
	 * @param name
	 * @param keys
	 * @return
	 */
	Boolean multiDelete(final N name, final Set<K> keys);
}
