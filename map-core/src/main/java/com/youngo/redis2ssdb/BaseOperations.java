package com.youngo.redis2ssdb;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class BaseOperations<K, V> {
	private RedisConnectionFactory factory;
	protected RedisSerializer<K> keySerializer = null;
	protected RedisSerializer<V> valueSerializer = null;
	protected RedisSerializer<?> hashKeySerializer = null;
	protected RedisSerializer<?> hashValueSerializer = null;
	
	protected RedisTemplate<K, V> redisTemple = null;
	
	public BaseOperations(){
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseOperations(RedisConnectionFactory factory, Class<K> keyClass, Class<V> valueClass){
		this.factory = factory;
		if(String.class.equals(keyClass)){
			this.keySerializer = (RedisSerializer<K>) new StringRedisSerializer();
		}else{
			this.keySerializer = new Jackson2JsonRedisSerializer<K>(keyClass);
		}
		if(String.class.equals(valueClass)){
			this.valueSerializer = (RedisSerializer<V>) new StringRedisSerializer();
		}else{
			this.valueSerializer = new Jackson2JsonRedisSerializer<V>(valueClass);
		}
		this.redisTemple = new RedisTemplateAdapter();
	}
	
	
	class RedisTemplateAdapter extends RedisTemplate<K, V> {
		
		private RedisTemplateAdapter(){
			setKeySerializer(keySerializer);
			setValueSerializer(valueSerializer);
			setConnectionFactory(factory);
			afterPropertiesSet();
		}
	}
}
