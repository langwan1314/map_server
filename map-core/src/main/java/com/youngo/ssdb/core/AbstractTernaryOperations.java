package com.youngo.ssdb.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;

import com.youngo.ssdb.core.entity.Tuple;
import com.youngo.ssdb.serializer.Jackson2JsonSsdbSerializer;
import com.youngo.ssdb.serializer.SerializerUtils;
import com.youngo.ssdb.serializer.SsdbSerializer;
import com.youngo.ssdb.serializer.StringSsdbSerializer;

/**
 * 
 * @author zhezhiren
 * @param <N>
 * @param <K>
 * @param <V>
 * @since 1.0
 */
public abstract class AbstractTernaryOperations<N, K, V> extends AbstractOperations<K, V>{
	private SsdbSerializer<N> nameSerializer;
	
	@SuppressWarnings("unchecked")
	public AbstractTernaryOperations(SSDB ssdb, Class<N> nameClass, Class<K> keyClass, Class<V> valueClass) {
		super(ssdb, keyClass, valueClass);
		if(nameClass.equals(String.class)){
			nameSerializer = (SsdbSerializer<N>) new StringSsdbSerializer();
		}else{
			nameSerializer = new Jackson2JsonSsdbSerializer<N>(nameClass);
		}
	}
	
	abstract class DeserializingNameAsSetSsdbDo extends AbstractSsdbDo<Set<N>>{
		Set<N> dowithResponse(Response resp){
			return deserializeNamesAsSet(resp.datas);
		}
	}
	
	protected byte[] rawName(N name){
		if (getKeySerializer() == null && name instanceof byte[]) {
			return (byte[]) name;  //TODO
		}
		return getNameSerializer().serialize(name);
	}
	
	byte[][] rawNameList(List<N> names){
		if(names == null || names.isEmpty()){
			return null;
		}
		byte[][] rawnames = new byte[names.size()][];
		for(int i = 0, j = names.size(); i < j; i++){
			N name = names.get(i);
			if(null == name){
				throw new RuntimeException();
			}else{
				rawnames[i] = rawName(name);
			}
		}
		return rawnames;
	}
	
	byte[][] rawNameAndKeys(N name, Collection<K> keys){
		if(name == null){
			throw new RuntimeException();//TODO  name不能为空
		}
		if(keys == null || keys.isEmpty()){
			throw new RuntimeException();//TODO  键列表不能为欸空，并且不能存在为空的键
		}
		byte[][] rawnameKeys = new byte[keys.size() + 1][];
		rawnameKeys[0] = rawName(name);
		int current = 1;
		for(K key : keys){
			if(null == key){
				throw new RuntimeException(); //TODO 键不能为空
			}else{
				rawnameKeys[current] = rawKey(key);
			}
			current++;
		}
		return rawnameKeys;
	}
	
	byte[][] rawNameAndKeyList(N name, List<K> keys){
		if(name == null){
			throw new RuntimeException();//TODO  name不能为空
		}
		if(keys == null || keys.isEmpty()){
			throw new RuntimeException();//TODO  键列表不能为欸空，并且不能存在为空的键
		}
		byte[][] rawnameKeys = new byte[keys.size() + 1][];
		rawnameKeys[0] = rawName(name);
		int current = 1;
		for(int i = 0, j = keys.size(); i < j; i++){
			K key = keys.get(i);
			if(null == key){
				throw new RuntimeException(); //TODO 键不能为空
			}else{
				rawnameKeys[current] = rawKey(key);
			}
			current++;
		}
		return rawnameKeys;
	}
	
	byte[][] rawNameAndTuples(N name, Set<Tuple<K,V>> tuples){
		if(name == null){
			throw new RuntimeException();//TODO  name不能为空
		}
		if(tuples == null || tuples.isEmpty()){
			throw new RuntimeException();//TODO  键值对不能为欸空，并且不能存在未空的值
		}
		byte[][] rawkeyValues = new byte[tuples.size()*2 + 1][];
		rawkeyValues[0] = rawName(name);
		int current = 0;
		for(Tuple<K, V> tuple : tuples){
			addTuplebyte(rawkeyValues, tuple.getKey(), tuple.getValue(), current * 2 + 1);
			current++;
		}
		return rawkeyValues;
	}
	
	byte[][] rawNameAndMap(N name, Map<K,V> values){
		if(name == null){
			throw new RuntimeException();//TODO  name不能为空
		}
		if(values == null || values.isEmpty()){
			throw new RuntimeException();//TODO  键值对不能为欸空，并且不能存在未空的值
		}
		byte[][] rawkeyValues = new byte[values.size()*2 + 1][];
		rawkeyValues[0] = rawName(name);
		int current = 0;
		Set<Entry<K, V>> etryies = values.entrySet();
		for(Entry<K, V> entry : etryies){
			addTuplebyte(rawkeyValues, entry.getKey(), entry.getValue(), current * 2 + 1);
			current++;
		}
		return rawkeyValues;
	}
	
	List<N> deserializeNamesAsList(List<byte[]> rawValues) {
		if (getValueSerializer() == null) {
			return null; //TODO
		}
		return SerializerUtils.deserializeValuesAsList(rawValues, getNameSerializer());
	}
	
	Set<N> deserializeNamesAsSet(List<byte[]> rawValues) {
		if (getValueSerializer() == null) {
			return null; //TODO
		}
		return SerializerUtils.deserializeValuesAsSet(rawValues, getNameSerializer());
	}
	
	SsdbSerializer<N> getNameSerializer(){
		return nameSerializer;
	}
}
