package com.youngo.redis2ssdb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;

import com.youngo.ssdb.core.ValueOperations;
import com.youngo.ssdb.core.entity.Tuple;

public class RValueOperations<K, V> extends BaseOperations<K, V> implements ValueOperations<K, V> {
	private org.springframework.data.redis.core.ValueOperations<K, V> valueOperations;
	
	public RValueOperations(RedisConnectionFactory factory, Class<K> keyClass, Class<V> valueClass){
		super(factory, keyClass, valueClass);
		this.valueOperations = redisTemple.opsForValue();
	}
	
	/*public RValueOperations() {
		this.valueOperations =  redisTemple.opsForValue();
	}*/
	
	@Override
	public Boolean set(K key, V val) {
		valueOperations.set(key, val);
		return true;
	}

	@Override
	public Boolean setWithTimeToLive(K key, V val, int timeToLive) {
		valueOperations.set(key, val, timeToLive);
		return true;
	}

	@Override
	public Boolean setIfAbsent(K key, V val) {
		return valueOperations.setIfAbsent(key, val);
	}

	@Override
	public Integer expire(K key, Integer timeToLive) {
		redisTemple.execute(new RedisCallback<Boolean>() {

			public Boolean doInRedis(RedisConnection connection) {
				try {
					return connection.pExpire(keySerializer.serialize(key), timeToLive);
				} catch (Exception e) {
					// Driver may not support pExpire or we may be running on Redis 2.4
					return connection.expire(keySerializer.serialize(key), timeToLive);
				}
			}
		}, true);
		return null;
	}

	@Override
	public Integer getTimeToLive(K key) {
		Long r = redisTemple.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection) {
				return connection.ttl(keySerializer.serialize(key));
			}
		}, true);
		return r.intValue();
	}

	@Override
	public V get(K key) {
		return valueOperations.get(key);
	}

	@Override
	public V getAndSet(K key, V newValue) {
		return valueOperations.getAndSet(key, newValue);
	}

	@Override
	public Boolean delete(K key) {
		Long r = redisTemple.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection) {
				return connection.del(keySerializer.serialize(key));
			}
		}, true);
		return r != null && r > 0;
	}

	@Override
	public Long increment(K key, int val) {
		return valueOperations.increment(key, val);
	}

	@Override
	public Boolean exists(K key) {
		Boolean r = redisTemple.execute(new RedisCallback<Boolean>() {

			public Boolean doInRedis(RedisConnection connection) {
				return connection.exists(keySerializer.serialize(key));
			}
		}, true);
		return r;
	}

	@Override
	public String subStr(K key, int start, int size) {
		//TODO
		throw new RuntimeException("not serported");
	}

	@Override
	public Integer strLength(K key) {
		Long r = redisTemple.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection) {
				return connection.strLen(keySerializer.serialize(key));
			}
		}, true);
		return r.intValue();
	}

	@Override
	public Set<K> getKeys(K start, K end, int limit) {
		//TODO
		throw new RuntimeException("not serported");
	}

	@Override
	public Set<K> getKeysReverse(K start, K end, int limit) {
		//TODO
		throw new RuntimeException("not serported");
	}

	@Override
	public List<Tuple<K, V>> scanValues(K start, K end, int limit) {
		//TODO
		throw new RuntimeException("not serported");
	}

	@Override
	public List<Tuple<K, V>> scanValuesReverse(K start, K end, int limit) {
		//TODO
		throw new RuntimeException("not serported");
	}

	@Override
	public Boolean multiSet(Map<K, V> values) {
		valueOperations.multiSet(values);
		return true;
	}

	@Override
	public Boolean multiSet(List<Tuple<K, V>> values) {
		if(values != null && values != null){
			Map<K, V> param = new HashMap<K, V>();
			for(int i = 0, j = values.size(); i < j; i++){
				param.put(values.get(i).getKey(), values.get(i).getValue());
			}
			multiSet(param);
		}
		return true;
	}

	@Override
	public List<Tuple<K, V>> multiGet(List<K> keys) {
		//TODO
		//List<V> values = valueOperations.multiGet(keys);
		throw new RuntimeException("not serported");
	}

	@Override
	public Boolean multiDelete(List<K> keys) {
		Long r = redisTemple.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection) {
				byte[][] b = new byte[keys.size()][];
				for(int i = 0, j = keys.size(); i< j; i++){
					b[i] = keySerializer.serialize(keys.get(i));
				}
				return connection.del(b);
			}
		}, true);
		return r != null && r > 0;
	}

}
