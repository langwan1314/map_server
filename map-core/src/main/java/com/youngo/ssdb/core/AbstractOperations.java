package com.youngo.ssdb.core;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.nutz.ssdb4j.spi.Cmd;
import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;

import com.youngo.ssdb.core.entity.Tuple;
import com.youngo.ssdb.serializer.Jackson2JsonSsdbSerializer;
import com.youngo.ssdb.serializer.SerializerUtils;
import com.youngo.ssdb.serializer.SsdbSerializer;
import com.youngo.ssdb.serializer.StringSsdbSerializer;

/**
 * 
 * @author zhezhiren
 * 
 * @param <K>
 * @param <V>
 * @since 1.0
 */
public abstract class AbstractOperations<K, V> {
	public static final Cmd qset = new Cmd("qset", false, false);//TODO
	public static final Cmd zpop_front = new Cmd("zpop_front", false, false);//TODO
	public static final Cmd zpop_back  = new Cmd("zpop_back", false, false);//TODO
	public static final Cmd rkeys  = new Cmd("rkeys", false, false);//TODO
	public static final Cmd zremrangebyscore =  new Cmd("zremrangebyscore", false, false);
	
	
	
	private SsdbSerializer<K> keySerializer;
	private SsdbSerializer<V> valueSerializer;
			
	protected SSDB ssdb;
	
	@Autowired
	public void setSsdb(SSDB ssdb){
		this.ssdb = ssdb;
	}
	
	@SuppressWarnings("unchecked")
	public AbstractOperations(SSDB ssdb, Class<K> keyClass, Class<V> valueClass){
		this.ssdb = ssdb;
		if(keyClass.equals(String.class)){
			this.keySerializer = (SsdbSerializer<K>) new StringSsdbSerializer();
		}else{
			this.keySerializer = new Jackson2JsonSsdbSerializer<K>(keyClass);
		}
		if(valueClass.equals(String.class)){
			this.valueSerializer = (SsdbSerializer<V>) new StringSsdbSerializer();
		}else{
			this.valueSerializer = new Jackson2JsonSsdbSerializer<V>(valueClass);
		}
	}
	
	abstract class DeserializingValueSsdbDo extends AbstractSsdbDo<V>{
		@Override
		V dowithResponse(Response resp){
			return deserializeValue(resp.datas.get(0));
		}
	}
	
	abstract class DeserializingKeyAsListSsdbDo extends AbstractSsdbDo<List<K>>{
		@Override
		List<K> dowithResponse(Response resp){
			return deserializeKeysAsList(resp.datas);
		}
	}
	
	abstract class DeserializingKeyAsSetSsdbDo extends AbstractSsdbDo<Set<K>>{
		@Override
		Set<K> dowithResponse(Response resp){
			return deserializeKeysAsSet(resp.datas);
		}
	}
	
	abstract class DeserializingValueAsListSsdbDo extends AbstractSsdbDo<List<V>>{
		@Override
		List<V> dowithResponse(Response resp){
			return deserializeValuesAsList(resp.datas);
		}
	}
	
	abstract class DeserializingListTupleSsdbDo extends AbstractSsdbDo<List<Tuple<K, V>>>{
		@Override
		List<Tuple<K, V>> dowithResponse(Response resp){
			return deserializeValuesAsTupleList(resp.datas);
		}
	}
	
	abstract class DeserializingMapSsdbDo extends AbstractSsdbDo<Map<K, V>>{
		@Override
		Map<K, V> dowithResponse(Response resp){
			return deserializeValuesAsMap(resp.datas);
		}
	}
	
	@SuppressWarnings("unchecked")
	V deserializeValue(byte[] value) {
		if (getValueSerializer() == null) {
			return (V) value; //TODO
		}
		return getValueSerializer().deserialize(value);
	}
	
	@SuppressWarnings("unchecked")
	List<V> deserializeValuesAsList(List<byte[]> rawValues) {
		if (getValueSerializer() == null) { //TODO
			return (List<V>) rawValues;
		}
		return SerializerUtils.deserializeValuesAsList(rawValues, getValueSerializer());
	}
	
	Map<K, V> deserializeValuesAsMap(List<byte[]> rawValues) {
		if (getValueSerializer() == null) {
			return null;//TODO
		}
		return SerializerUtils.deserializeValuseAsMap(rawValues,getKeySerializer(), getValueSerializer());
	}
	
	List<Tuple<K, V>> deserializeValuesAsTupleList(List<byte[]> rawValues) {
		//TODO valueSerializer 为null
		return SerializerUtils.deserializeValuesAsTupleList(rawValues, getKeySerializer(), getValueSerializer());
	}
	
	@SuppressWarnings("unchecked")
	Set<V> deserializeValuesAsSet(List<byte[]> rawValues) {
		if (getValueSerializer() == null) {
			return (Set<V>) rawValues;//TODO  序列化列表为空时怎么处理
		}
		return SerializerUtils.deserializeValuesAsSet(rawValues, getValueSerializer());
	}
	
	
	List<K> deserializeKeysAsList(List<byte[]> rawValues) {
		if (getValueSerializer() == null) {
			return null; //TODO
		}
		return SerializerUtils.deserializeValuesAsList(rawValues, getKeySerializer());
	}
	
	Set<K> deserializeKeysAsSet(List<byte[]> rawValues) {
		if (getValueSerializer() == null) {
			return null; //TODO
		}
		return SerializerUtils.deserializeValuesAsSet(rawValues, getKeySerializer());
	}
	
	byte[] rawKey(K key){
		if (getKeySerializer() == null && key instanceof byte[]) {
			return (byte[]) key;  //TODO
		}
		return getKeySerializer().serialize(key);
	}
	
	byte[][] rawKeyList(List<K> keys){
		if(keys == null || keys.isEmpty()){
			return null; //TODO
		}
		byte[][] rawKeys = new byte[keys.size()][];
		for(int i = 0, j = keys.size(); i < j; i++){
			K key = keys.get(i);
			if(null == key){
				throw new RuntimeException(); //TODO
			}else{
				rawKeys[i] = rawKey(key);
			}
		}
		return rawKeys;
	}
	
	byte[] rawValue(V value) {
		if (getValueSerializer() == null && value instanceof byte[]) {
			return (byte[]) value; //TODO
		}
		return getValueSerializer().serialize(value);
	}
	
	byte[][] rawTupleList(List<Tuple<K, V>> list){
		if(list == null || list.isEmpty()){
			return null; //TODO
		}
		byte[][] rawkeyValues = new byte[list.size()*2][];
		for(int i = 0, j = list.size(); i < j; i++){
			Tuple<K, V> tuple= list.get(i);
			addTuplebyte(rawkeyValues, tuple.getKey(), tuple.getValue(), i * 2);
		}
		return rawkeyValues;
	}
	
	byte[][] rawHash(Map<K, V> map){
		if(map == null || map.isEmpty()){
			return null;  //TODO
		}
		byte[][] rawkeyValues = new byte[map.size()*2][];
		Set<Entry<K, V>> entries = map.entrySet();
		int current = 0;
		for(Entry<K, V> entry: entries){
			addTuplebyte(rawkeyValues, entry.getKey(), entry.getValue(), current * 2);
			current++;
		}
		return rawkeyValues;
	}
	
	void addTuplebyte(byte[][] rawkeyValues, K key, V value, int offset){
		byte[] rawkey = null;
		byte[] rawValue = null;
		if(key == null){
			throw new RuntimeException();//TODO  key 不能为null
		}else{
			rawkey = rawKey(key); 
		}
		if(value == null){
			rawValue = "".getBytes();//TODO  value是否能为null再考虑
		}else{
			rawValue = rawValue(value);
		}
		rawkeyValues[offset] = rawkey;
		rawkeyValues[offset+1] = rawValue;
	}
	
	SsdbSerializer<K> getKeySerializer(){
		return keySerializer;
	}
	
	SsdbSerializer<V> getValueSerializer(){
		return valueSerializer;
	}
}
