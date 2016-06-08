package com.youngo.ssdb.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nutz.ssdb4j.spi.Cmd;
import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;

import com.youngo.ssdb.core.entity.Tuple;

public class DefaultHashMapOperations<N, K, V> extends AbstractTernaryOperations<N, K, V> implements HashMapOperations<N, K, V> {
	
	
	public DefaultHashMapOperations(SSDB ssdb, Class<N> nameClass, Class<K> keyClass, Class<V> valueClass) {
		super(ssdb, nameClass, keyClass, valueClass);
	}
	
	/**
	 * hset
	 * 当结果返回为false时，并不意味着设置失败。结果返回为null时设置才是失败的。
	 * 从返回获取结果。修改成功 返回1，值未修改返回0。（实际是增加了新k则返回为1，否则返回为0）
	 * @param name
	 * @param key
	 * @param val
	 * @return
	 */
	public Boolean set(final N name, final K key, final V val){
		DeserializingBoolFromResultSsdbDo set = new DeserializingBoolFromResultSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hset, rawName(name), rawKey(key), rawValue(val));
			}
		};
		return set.doInSsdb(ssdb);
	}
	
	/**
	 * hget
	 * @param name
	 * @param key
	 * @return
	 */
	public V get(final N name, final K key){
		DeserializingValueSsdbDo get = new DeserializingValueSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hget, rawName(name), rawKey(key));
			}
		};
		return get.doInSsdb(ssdb);
	}
	
	/**
	 * hdel
	 * 从返回获取结果
	 * @param name
	 * @param key
	 * @return
	 */
	public Boolean delete(final N name, final K key){
		StatusBoolSsdbDo delete = new StatusBoolSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hdel, rawName(name), rawKey(key));
			}
		};
		return delete.doInSsdb(ssdb);
	}
	
	/**
	 * hincr
	 * hash值自增指定值
	 * @see ValueOperations.increment方法
	 * @param name
	 * @param key
	 * @param value
	 * @return
	 */
	public Integer increment(final N name, final K key, final int value){
		DeserializingIntSsdbDo increment = new DeserializingIntSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hincr, rawName(name), rawKey(key), Integer.toString(value).getBytes());
			}
		};
		return increment.doInSsdb(ssdb);
	}
	
	public Long incrementForLong(final N name, final K key, final int value){
		DeserializingLongSsdbDo increment = new DeserializingLongSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hincr, rawName(name), rawKey(key), Integer.toString(value).getBytes());
			}
		};
		return increment.doInSsdb(ssdb);
	}
	
	/**
	 * hexists
	 * 从返回获取结果
	 * @param name
	 * @param key
	 * @return
	 */
	public Boolean exists(final N name, final K key){
		DeserializingBoolFromResultSsdbDo exists = new DeserializingBoolFromResultSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hexists, rawName(name), rawKey(key));
			}
		};
		return exists.doInSsdb(ssdb);
	}
	
	/**
	 * hsize
	 * name 对应的hash 不存在时，返回0.
	 * @param name
	 * @return
	 */
	public Long size(final N name){
		DeserializingLongSsdbDo size = new DeserializingLongSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hsize, rawName(name));
			}
		};
		return size.doInSsdb(ssdb);
	}
	
	/**
	 * hlist
	 * 返回位于 nameStart 与nameEnd之间的name列表，name排序按照字典排序。
	 * @param nameStart
	 * @param nameEnd
	 * @param limit
	 * @return
	 */
	public Set<N> getNames(final N nameStart, final N nameEnd, final int limit){
		DeserializingNameAsSetSsdbDo listNames = new DeserializingNameAsSetSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hlist, rawName(nameStart), rawName(nameEnd), Integer.toString(limit).getBytes());
			}
		};
		return listNames.doInSsdb(ssdb);
	}
	
	/**
	 * hrlist
	 * 获取nameStart 与nameEnd之间的name列表， name排序按照字典倒序
	 * @param nameStart
	 * @param nameEnd
	 * @param limit
	 * @return
	 */
	public Set<N> getNamesReverse(final N nameStart, final N nameEnd, final int limit){
		DeserializingNameAsSetSsdbDo listNamesReverse = new DeserializingNameAsSetSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hrlist, rawName(nameStart), rawName(nameEnd), Integer.toString(limit).getBytes());
			}
		};
		return listNamesReverse.doInSsdb(ssdb);
	}
	
	/**
	 * hkeys
	 * 获取指定hash对象中 key >= keyStart 并且 key <= keyEnd 的key列表，key按照字典排序
	 * @param name
	 * @param keyStart
	 * @param keyEnd
	 * @param limit
	 * @return
	 */
	public Set<K> getKeys(final N name, final K keyStart, final K keyEnd, final int limit){
		DeserializingKeyAsSetSsdbDo listKeys = new DeserializingKeyAsSetSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hkeys, rawKey(keyStart), rawKey(keyEnd), Integer.toString(limit).getBytes());
			}
		};
		return listKeys.doInSsdb(ssdb);
	}
	
	
	/**
	 * hgetall
	 * @param name
	 * @return
	 */
	//TODO
	public Map<K, V> getAllByName(final N name){
		DeserializingMapSsdbDo scanAllByName = new DeserializingMapSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hgetall, rawName(name));
			}
		};
		return scanAllByName.doInSsdb(ssdb);
	}
	
	
	
	/**
	 * hgetall
	 * @param name
	 * @return
	 */
	@Deprecated
	public List<Tuple<K, V>> scanAllByNameAsList(final N name){
		DeserializingListTupleSsdbDo scanAllByName = new DeserializingListTupleSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hgetall, rawName(name));
			}
		};
		return scanAllByName.doInSsdb(ssdb);
	}
	
	/**
	 * hscan
	 * @param name
	 * @param keyStart
	 * @param keyEnd
	 * @param limit
	 * @return
	 */
	//TODO
	public List<Tuple<K, V>> scanByKeys(final N name, final K keyStart, final K keyEnd, final int limit){
		DeserializingListTupleSsdbDo scanByKeys = new DeserializingListTupleSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hscan, rawName(name), rawKey(keyStart), rawKey(keyEnd), Integer.toString(limit).getBytes());
			}
		};
		return scanByKeys.doInSsdb(ssdb);
	}
	
	/**
	 * hrscan
	 * @param name
	 * @param keyStart
	 * @param keyEnd
	 * @param limit
	 * @return
	 */
	public List<Tuple<K, V>> scanByKeysReverse(final N name, final K keyStart, final K keyEnd, final int limit){
		DeserializingListTupleSsdbDo scanByKeyReverse = new DeserializingListTupleSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hrscan, rawName(name), rawKey(keyStart), rawKey(keyEnd), Integer.toString(limit).getBytes());
			}
		};
		return scanByKeyReverse.doInSsdb(ssdb);
	}
	
	/**
	 * hclear
	 * 返回删除条数
	 * @param name
	 * @return
	 */
	public Integer clear(final N name){
		DeserializingIntSsdbDo clearByName = new DeserializingIntSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.hclear, rawName(name));
			}
		};
		
		return clearByName.doInSsdb(ssdb);
	}
	
	/**
	 * multi_hset
	 * 从状态取结果。
	 * @param name
	 * @param values
	 * @return
	 */
	public Boolean multiSet(final N name, final Set<Tuple<K, V>> values){
		StatusBoolSsdbDo multiSet = new StatusBoolSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.multi_hset, rawNameAndTuples(name, values));
			}
		};
		return multiSet.doInSsdb(ssdb);
	}
	
	/**
	 * multi_hset
	 * @param name
	 * @param values
	 * @return
	 */
	public Boolean multiSet(final N name, final Map<K, V> values){
		StatusBoolSsdbDo multiSet = new StatusBoolSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.multi_hset, rawNameAndMap(name, values));
			}
		};
		return multiSet.doInSsdb(ssdb);
	}
	
	/**
	 *
	 * multi_hget
	 * @param name
	 * @param keys
	 * @return
	 */
	public List<Tuple<K, V>> multiGetByKeys(final N name, final Set<K> keys){
		DeserializingListTupleSsdbDo multiGetByKeys = new DeserializingListTupleSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.multi_hget, rawNameAndKeys(name, keys));
			}
		};
		return multiGetByKeys.doInSsdb(ssdb);
	}
	
	public Map<K, V> multiGetByKeysAsMap(final N name, final Set<K> keys){
		DeserializingMapSsdbDo multiGetByKeys = new DeserializingMapSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.multi_hget, rawNameAndKeys(name, keys));
			}
		};
		return multiGetByKeys.doInSsdb(ssdb);
	}
	
	public Map<K, V> multiGetByKeysAsMap(final N name, final List<K> keys){
		DeserializingMapSsdbDo multiGetByKeys = new DeserializingMapSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.multi_hget, rawNameAndKeyList(name, keys));
			}
		};
		return multiGetByKeys.doInSsdb(ssdb);
	}
	
	/**
	 * multi_hdel
	 * 从状态取结果
	 * @param name
	 * @param keys
	 * @return
	 */
	public Boolean multiDelete(final N name, final Set<K> keys){
		StatusBoolSsdbDo multiDelete = new StatusBoolSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.multi_hdel, rawNameAndKeys(name, keys));
			}
		};
		return multiDelete.doInSsdb(ssdb);
	}

}
