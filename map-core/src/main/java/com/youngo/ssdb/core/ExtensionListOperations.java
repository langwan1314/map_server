package com.youngo.ssdb.core;

import java.util.Collection;
import java.util.List;

import org.nutz.ssdb4j.spi.SSDB;

public class ExtensionListOperations<V> extends AbstractOperations<String, V>{
	List<String> list = null;
	
	public ExtensionListOperations(SSDB ssdb, Class<V> valueClass) {
		super(ssdb, String.class, valueClass);
	}
	
	/**
	 * 获取列表大小
	 * 调用zsize(name)实现
	 * @param name
	 * @return
	 */
	public int size(String name){
		return 0;
	}
	
	/**
	 * 大小为0则为空
	 * 调用zsize(name)实现
	 * @param name
	 * @param value
	 * @return
	 */
	public boolean isEmpty(String name, V value){
		
		return false;
	}
	
	/**
	 * 
	 * 指定列表是否存在
	 * 调用zexists(name, key)
	 * @param name
	 * @return
	 */
	public boolean contains(String name, V value){
		return false;
	}
	
	/**
	 * 调用zset添加至末尾
	 * @param name
	 * @return
	 */
	public boolean add(String name){
		
		return false;
	}
	
	/**
	 * 调用zdel
	 * @param name
	 * @param value
	 * @return
	 */
	public boolean remove(String name, V value){
		
		return false;
	}
	
	/**
	 * 
	 * 调用zremrangebyrank(name,start, end)
	 * @param index
	 * @return
	 */
	public boolean remove(int index){
		return false;
	}
	
	/**
	 * 调用multi_zset(name, key1, score1, key2, score2 ...)
	 * @param values
	 * @return
	 */
	public boolean addAll(Collection<? extends V> values){
		
		return false;
	}
	
	/**
	 * 调用multi_zdel(name, key1, key2 ...)
	 * @param values
	 * @return
	 */
	public boolean removeAll(Collection<? extends V> values){
		return false;
	}
	
	/**
	 * 调用 zclear(name);
	 * @return
	 */
    public Integer clear(){
    	return 0;
    }
	
    /**
     * zrange name offset limit
     * @param index
     * @return
     */
    V get(int index){
    	return null;
    }
    
    /**
     * zrange name offset limit
     * @param start
     * @param end
     * @return
     */
	List<V> get(int start, int end){
		
		return null;
	}
	
	/**
	 * zrank name key
	 * @param v
	 * @return
	 */
	//SSDB 中的获取排名  只支持在离线环境下使用。
	public Integer indexOf(V v){
		
		return null;
	}
	
}
