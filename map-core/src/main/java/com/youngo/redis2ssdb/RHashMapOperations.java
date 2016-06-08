package com.youngo.redis2ssdb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;

import com.youngo.ssdb.core.HashMapOperations;
import com.youngo.ssdb.core.entity.Tuple;

public class RHashMapOperations<N, K, V> extends BaseOperations<K, V> implements HashMapOperations<N, K, V>{
	
	HashOperations<N, K, V> operations = null;
	
	public RHashMapOperations(RedisConnectionFactory factory, Class<N> nameClass, Class<K> keyClass, Class<V> valueClass){
		super(factory, keyClass, valueClass);
		this.operations = (HashOperations<N, K, V>) redisTemple.opsForHash();
	}
	
	@Override
	public Boolean set(N name, K key, V val) {
		operations.put(name, key, val);
		return true;
	}

	@Override
	public V get(N name, K key) {
		return operations.get(name, key);
	}

	@Override
	public Boolean delete(N name, K key) {
		operations.delete(name, key);
		return true;
	}

	@Override
	public Integer increment(N name, K key, int value) {
		Long l = operations.increment(name, key, value);
		return l.intValue();
	}
	
	@Override
	public Long incrementForLong(N name, K key, int value) {
		Long l = operations.increment(name, key, value);
		return l;
	}

	@Override
	public Boolean exists(N name, K key) {
		return operations.hasKey(name, key);
	}

	@Override
	public Long size(N name) {
		return operations.size(name);
	}

	@Override
	public Set<N> getNames(N nameStart, N nameEnd, int limit) {
		
		// TODO  keys
		throw new RuntimeException("not serported");
	}

	@Override
	public Set<N> getNamesReverse(N nameStart, N nameEnd, int limit) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public Set<K> getKeys(N name, K keyStart, K keyEnd, int limit) {
		//operations.keys(name);
		// TODO hkeys
		throw new RuntimeException("not serported");
	}

	@Override
	// TODO 着重测试
	public Map<K, V> getAllByName(N name) {
		
		Map<K, V> entries = operations.entries(name);
		return entries;
	}

	@Override
	@Deprecated
	public List<Tuple<K, V>> scanAllByNameAsList(N name) {
		//operations.values(name);
		return null;
	}

	@Override
	public List<Tuple<K, V>> scanByKeys(N name, K keyStart, K keyEnd, int limit) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public List<Tuple<K, V>> scanByKeysReverse(N name, K keyStart, K keyEnd, int limit) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public Integer clear(N name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public Boolean multiSet(N name, Set<Tuple<K, V>> values) {
		if(null != values && values.size() > 0){
			Map<K, V> map = new HashMap<K, V>();
			for(Tuple<K, V> tuple : values){
				map.put(tuple.getKey(), tuple.getValue());
			}
			operations.putAll(name, map);
			return true;
		}
		return false;
	}

	@Override
	public Boolean multiSet(N name, Map<K, V> values) {
		operations.putAll(name, values);
		return true;
	}

	@Override
	public List<Tuple<K, V>> multiGetByKeys(N name, Set<K> keys) {
		//List<V> valuse = operations.multiGet(name, keys);
		// TODO 
		throw new RuntimeException("not serported");
	}
	
	public Map<K, V> multiGetByKeysAsMap(final N name, final Set<K> keys){
		//TODO
		throw new RuntimeException("not serported");
	}
	
	public Map<K, V> multiGetByKeysAsMap(final N name, final List<K> keys){
		//TODO
		throw new RuntimeException("not serported");
	}

	@Override
	public Boolean multiDelete(N name, Set<K> keys) {
		if(null != keys && keys.size() > 0){
			Object os = keys.toArray();
			operations.delete(name, os);
			return true;
		}
		return false;
	}

}
