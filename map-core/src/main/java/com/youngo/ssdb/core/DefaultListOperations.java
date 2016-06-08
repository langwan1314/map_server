package com.youngo.ssdb.core;

import java.util.List;
import java.util.Set;

import org.nutz.ssdb4j.spi.Cmd;
import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;

public class DefaultListOperations<K, V> extends AbstractOperations<K, V> implements ListOperations <K, V>{

	public DefaultListOperations(SSDB ssdb, Class<K> keyClass, Class<V> valueClass) {
		super(ssdb, keyClass, valueClass);
	}
	
	/**
	 * qpush
	 * 在队列的末尾加入一条记录，返回增加后队列的长度
	 * @param name
	 * @param val
	 * @return
	 */
	public Integer push(final K key, final V val){
		DeserializingIntSsdbDo push = new DeserializingIntSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qpush, rawKey(key), rawValue(val));
			}
		};
		return push.doInSsdb(ssdb);
	}
	
	/**
	 * qpush_front
	 * 在队列的头部增加一条记录，返回增加后队列的长度
	 * @param name
	 * @param val
	 * @return
	 */
	public Integer pushFront(final K key, final V val){
		DeserializingIntSsdbDo pushFront = new DeserializingIntSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qpush_front, rawKey(key), rawValue(val));
			}
		};
		return pushFront.doInSsdb(ssdb);
	}
	
	//TODO 当size为1时返回的是字符列表，需要处理，当size不为1时，返回的是value列表
	/**
	 * qpop
	 * 删除并且返回队首的数据
	 * @param name
	 * @return
	 */
	public V pop(final K key){
		DeserializingValueSsdbDo pop = new DeserializingValueSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qpop, rawKey(key));
			}
		};
		return pop.doInSsdb(ssdb);
	}
	
	//TODO 返回的是字符列表，需要处理
	/**
	 * qpop_back
	 * 删除并且返回队尾的数据
	 * @param name
	 * @return
	 */
	public V popBack(final K key){
		DeserializingValueSsdbDo popBack = new DeserializingValueSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qpop_back, rawKey(key));
			}
		};
		return popBack.doInSsdb(ssdb);
	}
	
	/**
	 * qfront
	 * 返回队首的数据，列表不存在(无数据)时状态返回为not_found
	 * @param key
	 * @return
	 */
	public V front(final K key){
		DeserializingValueSsdbDo front = new DeserializingValueSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qfront, rawKey(key));
			}
		};
		return front.doInSsdb(ssdb);
	}
	
	/**
	 * qback
	 * 返回队尾的数据，列表不存在(无数据)时状态返回为not_found
	 * @param key 
	 * @return
	 */
	public V back(final K key){
		DeserializingValueSsdbDo back = new DeserializingValueSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qback, rawKey(key));
			}
		};
		return back.doInSsdb(ssdb);
	}
	
	/**
	 * qsize
	 * 获取指定列表的长度,不存该列表则在返回0
	 * @param key
	 * @return
	 */
	public Integer size(final K key){
		DeserializingIntSsdbDo size = new DeserializingIntSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qsize, rawKey(key));
			}
		};
		return size.doInSsdb(ssdb);
	}
	
	/**
	 * qclear
	 * 清除指定列表的数据，并且返回清除的列表长度
	 * @param key
	 * @return
	 */
	public Integer clear(final K key){
		DeserializingIntSsdbDo clear = new DeserializingIntSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qclear, rawKey(key));
			}
		};
		return clear.doInSsdb(ssdb);
	}
	
	/**
	 * qget
	 * 返回指定序号的列表数据，不存在对应的index的数据时，状态为not_found
	 * @param index
	 * @return
	 */
	public V get(final int index){
		DeserializingValueSsdbDo get = new DeserializingValueSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qget, Integer.toString(index).getBytes());
			}
			
		};
		return get.doInSsdb(ssdb);
	}
	
	/**
	 *qset
	 * 从状态中获取返回结果。当index>size 时返回错误，
	 * error: index out of range
	 * @param index
	 * @param val
	 * @return
	 */
	public boolean set(final K key, final int index, final V val){
		StatusBoolSsdbDo set = new StatusBoolSsdbDo() {
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(qset, rawKey(key), Integer.toString(index).getBytes(), rawValue(val));
			}
		};
		return set.doInSsdb(ssdb);
	}
	
	/**
	 * qrange
	 * 返回指定列表从指定偏移offset(offset为正从头计数，offset为负从队尾计数)开始的往队尾(无论offset正负)的limit条数记录
	 * @param key
	 * @param offset 当offset为负数时， limit<|offset|才有数据返回，并且offset是从队尾起计数，offset为正数时从头计数。 offset>list.size时返回空数据
	 * @param limit 当limit为不合法数据时(非正整数)，获取从offset起所有(无论offset正负)
	 * @return
	 */
	public List<V> range(final K key, final int offset, final int limit){
		DeserializingValueAsListSsdbDo range = new DeserializingValueAsListSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qrange, rawKey(key), Integer.toString(offset).getBytes(), Integer.toString(limit).getBytes());
			}
		};
		return range.doInSsdb(ssdb);
	}
	
	/**
	 * qslice
	 * 返回指定列表从指定偏移start开始至指定偏移end之间的记录，即 index >= 偏移(start) && index <= 偏移(end)，
	 * 其中偏移的算法为：start与end 为负数时，从尾部从-1起计数，-1表示倒数第一个，-2表示倒数第2个
	 * start与end 为0，-0或者正数时，从头部从0开始计数， 0表示顺数第一个，1表示顺数地2个。
	 * @param key 
	 * @param start 偏移的起始值， 负数从尾部偏移， 正数从头部偏移
	 * @param end  偏移的结束值， 负数从尾部偏移， 正数从头部偏移
	 * 
	 * 如以下列表start与end的值代表的偏移如下
	 * 0/-10  1/-9  2/-8  3/-7  4/-6  5/-5  6/-4  7/-3  8/-2  9/-1
	 * 1      2     3     4     5     6     7     8     9     10 
	 * 
	 * @return 处于区间的的数据值列表
	 */
	public List<V> slice(final K key, final int start, final int end){
		DeserializingValueAsListSsdbDo slice = new DeserializingValueAsListSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qslice, rawKey(key), Integer.toString(start).getBytes(), Integer.toString(end).getBytes());
			}
		};
		return slice.doInSsdb(ssdb);
	}
	
	/**
	 * qtrim_front
	 * 删除指定list前面size个数据
	 * @param key
	 * @param size
	 * @return
	 */
	public Integer deleteFront(final K key, final int size){
		DeserializingIntSsdbDo deleteFront = new DeserializingIntSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qtrim_front, rawKey(key), Integer.toString(size).getBytes());
			}
		};
		return deleteFront.doInSsdb(ssdb);
	}
	
	/**
	 * qtrim_back
	 * 删除指定list后面size个数据
	 * @param key
	 * @param size
	 * @return
	 */
	public Integer deleteBack(final K key, final int size){
		DeserializingIntSsdbDo deleteBack = new DeserializingIntSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qtrim_back, rawKey(key), Integer.toString(size).getBytes());
			}
		};
		return deleteBack.doInSsdb(ssdb);
	}
	
	/**
	 * qlist
	 * 以List形式返回符合条件 key >= keystart&& key <= keyEnd 的列表名称的 列表，其中key以字典顺序排序
	 * @param nameStart
	 * @param nameEnd
	 * @param limit
	 * @return
	 */
	public List<K> getKeys(final K keyStart, final K keyEnd, final int limit){
		DeserializingKeyAsListSsdbDo listKeys = new DeserializingKeyAsListSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qlist, rawKey(keyStart), rawKey(keyEnd), Integer.toString(limit).getBytes());
			}
		};
		return listKeys.doInSsdb(ssdb);
	}
	
	/**
	 * qlist
	 * 以Set形式返回符合条件 key >= keystart&& key <= keyEnd 的列表名称的 列表，其中key以字典顺序排序
	 * @param nameStart
	 * @param nameEnd
	 * @param limit
	 * @return
	 */
	public Set<K> getKeysAsSet(final K keyStart, final K keyEnd, final int limit){
		DeserializingKeyAsSetSsdbDo listKeysAsSet = new DeserializingKeyAsSetSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qlist, rawKey(keyStart), rawKey(keyEnd), Integer.toString(limit).getBytes());
			}
		};
		return listKeysAsSet.doInSsdb(ssdb);
	}
	
	/**
	 * qrlist
	 * 以List形式返回符合条件 key >= keystart&& key <= keyEnd 的列表名称的 列表，其中key以字典倒序排序
	 * @param nameStart
	 * @param nameEnd
	 * @param limit
	 * @return
	 */
	public List<K> getKeysReverse(final K keyStart, final K keyEnd, final int limit){
		DeserializingKeyAsListSsdbDo getKeysReverse = new DeserializingKeyAsListSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qrlist, rawKey(keyStart), rawKey(keyEnd), Integer.toString(limit).getBytes());
			}
		};
		return getKeysReverse.doInSsdb(ssdb);
	}
	
	/**
	 * qrlist
	 * 以Set形式返回符合条件 key >= keystart&& key <= keyEnd 的列表名称的 列表，其中key以字典倒序排序
	 * @param nameStart
	 * @param nameEnd
	 * @param limit
	 * @return
	 */
	public Set<K> getKeysReverseAsSet(final K keyStart, final K keyEnd, final int limit){
		DeserializingKeyAsSetSsdbDo getKeysReverseAsSet = new DeserializingKeyAsSetSsdbDo(){
			@Override
			protected Response inSsdb(SSDB ssdb) {
				return ssdb.req(Cmd.qrlist, rawKey(keyStart), rawKey(keyEnd), Integer.toString(limit).getBytes());
			}
		};
		return getKeysReverseAsSet.doInSsdb(ssdb);
	}
}
