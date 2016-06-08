package com.youngo.ssdb.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nutz.ssdb4j.spi.Cmd;
import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;

import com.youngo.ssdb.core.entity.Tuple;

public class DefaultValueOperations<K, V> extends AbstractOperations<K, V> implements ValueOperations<K, V>{

	public DefaultValueOperations(SSDB ssdb, Class<K> keyClass, Class<V> valueClass) {
		super(ssdb, keyClass, valueClass);
	}
	
	/**
	 * set(key value)
	 * 设置指定的key为特定的值
	 * @param key
	 * @param val 设置成功与否的状态
	 * @return 
	 */
	public Boolean set(final K key, final V val){
		StatusBoolSsdbDo set = new StatusBoolSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.set, rawKey(key), rawValue(val));
			}
		};
		return set.doInSsdb(ssdb);
	}
	
	/**
	 * setx(key value ttl)
	 * 设置key的值并且附带过期时间，如果 key 存在并且 ttl 设置成功, 返回true, 否则返回 false.
	 * @param key 存储的 键(key)
	 * @param val 存储的额 值(value)
	 * @param timeToLive 键值对存活的时间，单位为秒
	 * @return 设置成功与否的结果  
	 */
	public Boolean setWithTimeToLive(final K key, final V val, final int timeToLive){
		DeserializingBoolFromResultSsdbDo setx = new DeserializingBoolFromResultSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.setx, rawKey(key), rawValue(val), Integer.toString(timeToLive).getBytes());
			}
		};
		return setx.doInSsdb(ssdb);
	}
	
	/**
	 * setnx(key value)
	 * 如果key对应的值不存在，则设置为value值 ；1: 设置成功; 0: key 已经存在.
	 * @param key
	 * @param val
	 * @return 设置成功与否的状态
	 */
	public Boolean setIfAbsent(final K key, final V val){
		DeserializingBoolFromResultSsdbDo setnx = new DeserializingBoolFromResultSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.setnx, rawKey(key), rawValue(val));
			}
		};
		return setnx.doInSsdb(ssdb);
	}
	
	/**
	 * expire(key ttl)
	 * expire 设置数据的有效时间，不能设置为永久生存，设置为-1将直接导致数据失效。
	 * @param key
	 * @param timeToLive 生存时间，单位为秒
	 * @return 数据的有效时间，单位为秒
	 */
	public Integer expire(final K key, final Integer timeToLive){
		DeserializingIntSsdbDo expire = new DeserializingIntSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.expire, rawKey(key), Integer.toString(timeToLive).getBytes());
			}
		};
		return expire.doInSsdb(ssdb);
	}
	
	/**
	 * ttl(key)
	 * 获取键的剩余有效时间，单位为秒
	 * @param key
	 * @return 剩余有效时间
	 */
	public Integer getTimeToLive(final K key ){
		DeserializingIntSsdbDo expire = new DeserializingIntSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.ttl, rawKey(key));
			}
		};
		return expire.doInSsdb(ssdb);
	}
	
	/**
	 * get(key)
	 * 获取指定 key 的值内容.返回 key 对应的值, 如果 key 不存在, 返回null,(ssdb返回 not_found 状态码).
	 * @param key
	 * @return 返回 key 对应的值, 如果 key 不存在, 返回null.
	 */
	public V get(final K key){
		DeserializingValueSsdbDo get = new DeserializingValueSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.get, rawKey(key));
			}
		};
		return get.doInSsdb(ssdb);
	}
	
	/**
	 * getset(key value)
	 * 更新 key 对应的 value, 并返回更新前的旧的 value.如果 key 不存在, 返回 null,即使返回 null, 值也会被新加进去.
	 * (如果 key 不存在, ssdb返回 not_found 状态码. )
	 * 设置的旧值与新值一样也会返回
	 * @param key 
	 * @param newValue
	 * @return 旧的value值
	 */
	public V getAndSet(final K key, final V newValue){
		DeserializingValueSsdbDo getset = new DeserializingValueSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.getset,rawKey(key), rawValue(newValue));
			}
		};
		return getset.doInSsdb(ssdb);
	}
	
	/**
	 * del(key)
	 * 删除指定的key与值，即使 key 不存在, 也会返回 true.
	 * 即使 key 不存在, ssdb也会返回 ok.
	 * @param key
	 * @return
	 */
	public Boolean  delete(final K key){
		StatusBoolSsdbDo del = new StatusBoolSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.del, rawKey(key));
			}
		};
		return del.doInSsdb(ssdb);
	}
	
	/**
	 * incr(key num)
	 * 键值自增, 必须是k对应的值为正数类型的数据才能增加(可以为字符型整数)，
	 * 一旦不是整数，则自增出错，哪怕为小数也不行， 但自增参数可以为小数，
	 * 结果为去除小数部分取整增加，参数为非数字时不报错，结果不增加。
	 * @param key
	 * @param val
	 * @return 增加后的值
	 */
	public Long increment(final K key, final int val){
		DeserializingLongSsdbDo incr = new DeserializingLongSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.incr, rawKey(key), Integer.toString(val).getBytes());
			}
		};
		return incr.doInSsdb(ssdb);
	}
	
	/**
	 * exists(key)
	 * 判断指定的 key 是否存在.
	 * @param key
	 * @return
	 */
	public Boolean exists(final K key){
		DeserializingBoolFromResultSsdbDo exists = new DeserializingBoolFromResultSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.exists,rawKey(key));
			}
		};
		return exists.doInSsdb(ssdb);
	}
	
	/**
	 * substr(key, start, size)
	 * 获取字符串的子串. 若 start 是负数, 则从字符串末尾算起. 若 size 是负数, 则表示从字符串末尾算起, 忽略掉那么多字节,
	 * @param key
	 * @param start
	 * @param size
	 * @return 子字符串
	 */
	public String subStr(final K key, final int start, final int size){
		DeserializingStringSsdbDo subStr = new DeserializingStringSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.substr, rawKey(key), Integer.toString(start).getBytes(), Integer.toString(size).getBytes());
			}
		};
		return subStr.doInSsdb(ssdb);
	}
	
	/**
	 * 计算字符串的长度(字节数).(一个utf-8三个字节)
	 * @param key
	 * @return
	 */
	public Integer strLength(final K key){
		DeserializingIntSsdbDo strLen = new DeserializingIntSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.strlen, rawKey(key));
			}
		};
		return strLen.doInSsdb(ssdb);
	}
	
	/**
	 * keys(key_start, key_end, limit)
	 * 列出处于区间 (key_start, key_end],即key >start && key <= key_end 的 key 列表. key按照字典排序
	 * ("", ""] 表示整个区间.
	 * @param start
	 * @param end
	 * @param limit 最多返回的元素个数
	 * @return key 列表
	 */
	public Set<K> getKeys(final K start, final K end, final int limit){
		DeserializingKeyAsSetSsdbDo getKeys = new DeserializingKeyAsSetSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.keys, rawKey(start), rawKey(end), Integer.toString(limit).getBytes());
			}
		};
		return getKeys.doInSsdb(ssdb);
	}
	
	/**
	 * rkeys(key_start, key_end, limit)
	 * 列出处于区间 (key_start, key_end], 即key >start && key <= key_end 的 key 列表. key按照倒序排序
	 * @param start
	 * @param end
	 * @param limit
	 * @return
	 */
	public Set<K> getKeysReverse(final K start, final K end, final int limit){
		DeserializingKeyAsSetSsdbDo getKeysReverse = new DeserializingKeyAsSetSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(rkeys, rawKey(start), rawKey(end), Integer.toString(limit).getBytes());
			}
		};
		return getKeysReverse.doInSsdb(ssdb);
	}
	
	/**
	 * scan(key_start, key_end, limit)
	 * 列出处于区间 (key_start, key_end], 即key >start && key <= key_end 的 key-value 列表. key按照字典排序
	 * ("", ""] 表示整个区间.
	 * @param start
	 * @param end
	 * @param limit
	 * @return 键值对列表
	 */
	public List<Tuple<K, V>> scanValues(final K start, final K end, final int limit){
		DeserializingListTupleSsdbDo scanValues = new DeserializingListTupleSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.scan, rawKey(start), rawKey(end), Integer.toString(limit).getBytes());
			}
			
		};
		return scanValues.doInSsdb(ssdb);
	}
	
	/**
	 * rscan(key_start, key_end, limit)
	 * 列出处于区间 (key_start, key_end], 即key >start && key <= key_end 的 key-value 列表. key按照字典倒序
	 * @param start
	 * @param end
	 * @param limit
	 * @return
	 */
	public List<Tuple<K, V>> scanValuesReverse(final K start, final K end, final int limit){
		DeserializingListTupleSsdbDo scanValuesReverse = new DeserializingListTupleSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.rscan, rawKey(start), rawKey(end), Integer.toString(limit).getBytes());
			}
		};
		return scanValuesReverse.doInSsdb(ssdb);
	}
	
	/**
	 * multi_set(key1,value1,key2,value2 ...)
	 * 批量设置一批 key-value.
	 * @param values
	 * @return 是否设置成功，结果从状态中获取
	 */
	public Boolean multiSet(final Map<K, V> values){
		//TODO 参数为Tuple的列表
		if(values == null || values.isEmpty()){
			return true;//TODO
		}
		StatusBoolSsdbDo multiSet = new StatusBoolSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				byte[][] rawKeys = rawHash(values);
				return ssdb.req(Cmd.multi_set,rawKeys);
			}
		};
		return multiSet.doInSsdb(ssdb);
	}
	
	/**
	 * multi_set(key1,value1,key2,value2 ...)
	 * 批量设置一批 key-value.
	 * @param values
	 * @return 是否设置成功，结果从状态中获取
	 */
	public Boolean multiSet(final List<Tuple<K, V>> values){
		if(values == null || values.isEmpty()){
			return true;
		}
		StatusBoolSsdbDo multiSet = new StatusBoolSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				byte[][] rawKeys = rawTupleList(values);
				return ssdb.req(Cmd.multi_set,rawKeys);
			}
		};
		return multiSet.doInSsdb(ssdb);
	}
	
	/**
	 * multi_get(key1,key2 ...)
	 * 批量获取一批 key 对应的值内容.
	 * @param keys
	 * @return 键值对列表
	 */
	public List<Tuple<K, V>> multiGet(final List<K> keys){
		if(keys == null || keys.isEmpty()){
			return null;
		}
		DeserializingListTupleSsdbDo multiGet = new DeserializingListTupleSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.multi_get, rawKeyList(keys));
			}
		};
		return multiGet.doInSsdb(ssdb);
	}
	
	/**
	 * multi_del(key1, key2 ...)
	 * 批量删除一批 key 和其对应的值内容.
	 * @param keys
	 * @return 从状态中获取结果
	 */
	public Boolean multiDelete(final List<K> keys){
		if(keys == null || keys.isEmpty()){
			return true;
		}
		StatusBoolSsdbDo multiDel = new StatusBoolSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				byte[][] rawKeys = rawKeyList(keys);
				return ssdb.req(Cmd.multi_del, rawKeys);
			}
		};
		return multiDel.doInSsdb(ssdb);
	}
	
}
