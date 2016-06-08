package com.youngo.ssdb.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.youngo.ssdb.core.entity.Tuple;

/**
 * 
 * @author zhezhiren
 *
 * @param <N>
 * @param <K>
 * @since 1.0
 */
public interface SortedSetOperations <N, K>{
	
	/**
	 * zset(name, key,score)
	 * 设置 zset 中指定 key 对应的权重值.
	 * @param name
	 * @param key
	 * @param score
	 * @return
	 */
	//TODO 从状态获取返回结果。
	Boolean set(final N name, final K key, final long score);
	
	/**
	 * zget(name, key)
	 * 获取 zset 中指定 key 的权重值.当指定的key不存在时
	 * @param name
	 * @param key
	 * @return
	 */
	Long get(final N name, final K key);
	
	/**
	 * zdel(name, key)
	 * 根据name和key删除 key对应的数据,即时数据本身不存在，只要执行成功，都是返回true(ssdb返回ok)
	 * @param name
	 * @param key
	 * @return
	 */
	//TODO
	boolean delete(final N name, final K key);
	
	/**
	 * zincr(name, key, num)
	 * 增加指定name对应的sort中 key对应对象的score值
	 * 参数 num 可以为负数. 如果num的值不是整数(字符串形式的整数), 它会被先转换成整数.
	 * @param name 列表名称
	 * @param key  列表中的对象
	 * @param val  增加的值
	 * @return 增加后的score
	 */
	Long increment(final N name, final K key, final int val);
	
	/**
	 * zexists(name, key)
	 * 存在则ssdb返回1.不存在返回0.
	 * @param name
	 * @param key
	 * @return
	 */
	boolean exists(final N name, final K key);
	
	/**
	 * zsize(name)
	 * 获取指定有序列表的key的个数,当name对应列表没设置时，返回0
	 * @param name
	 * @return
	 */
	Long size(final N name);
	
	/**
	 * zlist(name_start, name_end, limit)
	 * 查询name 位于start和end之间的列表名称，按照字典排序        start,end 分别为"", ""表示查询所有
	 * @param start 
	 * @param end
	 * @param limit
	 * @return
	 */
	Set<N> getNames(final N start, final N end, final int limit);
	
	/**
	 * zrlist(name_start, name_end, limit)
	 * 
	 * @param start
	 * @param end
	 * @param limit
	 * @return
	 */
	Set<N> getNamesReverse(final N start, final N end, final int limit);
	
	/**
	 * zscan(name, key_start, score_start, score_end, limit)
	 * 获取指定条件的key和key对应socre列表
	 * 当keyStart为空时，为获取name对应的列表中score>= scoreStart, 并且score <= scoreEnd的key以及score列表
	 * 当keyStart不为空时，对于 score = scoreStart 的key数据，需要判断他的key是不是> keyStart(key按字典排序判断大小)
	 * 需要获取所有的key时 scoreStart, scoreEnd 可以设置为"", "".
	 * 在不清楚keyStart的情况的作用的情况下，建议设置为空。
	 * @param name 列表名称
	 * @param keyStart 对score等于scoreStart的数据需要 该数据的key大于这个key才会返回。
	 * @param scoreStart 起始的score
	 * @param scoreEnd  终结的score
	 * @param limit 限定条数
	 * @return 符合条件的key 与score的键值对
	 */
	List<Tuple<K, Long>> scanByScore(final N name, final K keyStart, final Long scoreStart, final Long scoreEnd, final int limit);
	
	/**
	 * zrscan(name, key_start, score_start, score_end, limit)
	 * 同 scanByScore ，只是是score <= scoreStart 且 score >= endScore 并且key是倒序排列
	 * @param name
	 * @param scoreStart
	 * @param scoreEnd
	 * @param limit
	 * @param keyStart
	 * @return
	 */
	List<Tuple<K, Long>> scanScoreReverse(final N name, final K keyStart, final Long scoreStart, final Long scoreEnd, final int limit);
	
	/**
	 * zkeys(name, key_start, score_start, score_end, limit)
	 * 同scanByScore方法  {@link SortedSetOperations scanScore()}，只获取key，不获取score。
	 * @param name
	 * @param scoreStart
	 * @param scoreEnd
	 * @param limit
	 * @param keyStart
	 * @return
	 */
	Set<K> getKeysByScoreAsSet(final N name, final K keyStart, final Long scoreStart, final Long scoreEnd, final int limit);
	
	/**
	 * zkeys(name, key_start, score_start, score_end, limit)
	 * 同scanByScore方法  {@link SortedSetOperations scanScore()}，只获取key，不获取score。
	 * @param name
	 * @param scoreStart
	 * @param scoreEnd
	 * @param limit
	 * @param keyStart
	 * @return
	 */
	List<K> getKeysByScore(final N name, final K keyStart, final Long scoreStart, final Long scoreEnd, final int limit);
	
	/**
	 * zrank(name key)
	 * 获取排名，排名从0开始,所以要转换为自然排名需要+1；当key不存在时，返回状态为not_found
	 * 注意! 本方法可能会非常慢! 请在离线环境中使用.
	 * @param name
	 * @param key
	 * @return
	 */
	Long rank(final N name, final K key);
	
	/**
	 * zrrank(name, key)
	 * 同rank， key的排序为字典倒序
	 * @param name
	 * @param key
	 * @return
	 */
	Long rankReverse(final N name, final K key);
	
	/**
	 * zrange(name, offset, limit)
	 * 根据排名，从指定名次起，获取指定个  key与score对。
	 * @param name
	 * @param offset  offset越大，返回越慢
	 * @param limit
	 * @return
	 */
	List<Tuple<K, Long>> getByrange(final N name, final int offset, final int limit);
	
	/**
	 * zrrange(name, offset, limit)
	 * 根据排名倒序，从指定名次起，获取指定个  key与score对。
	 * @param name
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Tuple<K, Long>> getByrangeReverse(final N name, final int offset, final int limit);
	
	/**
	 * zclear(name)
	 * 删除 zset 中的所有 key.
	 * @param name
	 * @return 返回删除的key数量，当name不存在时返回0.
	 */
	Long clear(final N name);
	
	/**
	 * zcount(name, score_start, score_end)
	 * 获取指定列表中score位于start 与end之间的key数量
	 * @param name
	 * @param start
	 * @param end
	 * @return 
	 */
	Long count(final N name, final long start, final long end);
	
	/**
	 * zsum(name, score_start, score_end)
	 * 获取指定列表中score位于start 与end之间的key的score 的和。
	 * @param name
	 * @param start
	 * @param end
	 * @return
	 */
	//TODO  需要确定是根据排名还是score作为条件
	Long sum(final N name, final long start, final long end);
	
	/**
	 * zavg(name, score_start, score_end)
	 * 获取指定列表中score位于start 与end之间的key的score 的平均值。
	 * @param name
	 * @param start
	 * @param end
	 * @return
	 */
	//TODO  需要确定是根据排名还是score作为条件
	Long avg(final N name, final long start, final long end);
	
	/**
	 * zremrangebyrank(name, start, end)
	 * 删除指定有序序列中 排名 rank >= rankStart 且rank <= rankEnd的key，排名从0开始。
	 * @param name
	 * @param rankStart
	 * @param rankEnd
	 * @return
	 */
	Long deleteByrank(final N name, final long rankStart, final long rankEnd);
	
	/**
	 * zremrangebyscore(name, start, end)
	 * 删除指定有序序列中 权重score >= scoreStart 且score <= scoreEnd的key，排名从0开始。 
	 * @param name
	 * @param scoreStart
	 * @param scoreEnd
	 * @return
	 */
	Long deleteByScore(final N name, final long scoreStart, final long scoreEnd);
	
	/**
	 * zpop_front(name, limit)
	 * 删除并获取前面指定个数key的key与score
	 * @param name
	 * @param limit
	 * @return
	 */
	List<Tuple<K, Long>> popFront(final N name, final int limit);
	
	/**
	 * zpop_back(name, limit)
	 * 删除并获取后面指定个数key的key与score
	 * @param name
	 * @param limit
	 * @return
	 */
	List<Tuple<K, Long>> popBack(final N name, final int limit);
	
	/**
	 * multi_zset(name, key1 score1, key2, score2 ...)
	 * 从状态中获取是否设置成功
	 * @param name
	 * @param tuples
	 * @return
	 */
	Boolean multiSet(final N name, final Set<Tuple<K, Long>> tuples);
	
	/**
	 * multi_zset(name, key1 score1, key2, score2 ...)
	 * 从状态中获取是否设置成功
	 * @param name
	 * @return
	 */
	Boolean multiSet(final N name, final Map<K, Long> values);
	
	/**
	 * multi_zget(name, key1, key2 ...)
	 * 批量获取 zset 中多个 key 对应的权重值.
	 * @param name
	 * @param keys
	 * @return
	 */
	List<Tuple<K, Long>> multiGet(final N name, final Set<K> keys);
	
	Map<K, Long> multiGetAsMap(final N name, final Set<K> keys);
	
	/**
	 * multi_zdel(name, key1, key2 ...)
	 * 从状态中获取是否删除成功
	 * @param name
	 * @param keys
	 * @return
	 */
	Boolean multiDelete(final N name, final Set<K> keys);
	
}

