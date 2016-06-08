package com.youngo.redis2ssdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.youngo.ssdb.core.SortedSetOperations;
import com.youngo.ssdb.core.entity.SimpleTuple;
import com.youngo.ssdb.core.entity.Tuple;

public class RSortedSetOperation<N, K> extends BaseOperations<N, K> implements SortedSetOperations<N, K>{
	private org.springframework.data.redis.core.ZSetOperations<N, K> zSetOperations;
	
	public RSortedSetOperation(RedisConnectionFactory factory, Class<N> nameClass, Class<K> keyClass){
		super(factory, nameClass, keyClass);
		this.zSetOperations = redisTemple.opsForZSet();
	}
	
	@Override
	public Boolean set(N name, K key, long score) {
		double d = new Double(score);
		boolean b = zSetOperations.add(name, key, d);
		return b;
	}

	@Override
	public Long get(N name, K key) {
		Double b = zSetOperations.score(name, key);
		return b.longValue();
	}

	@Override
	public boolean delete(N name, K key) {
		Long l = zSetOperations.remove(name, key);
		return l > 0;
	}

	@Override
	public Long increment(N name, K key, int val) {
		double d = new Double(val);
		Double dr = zSetOperations.incrementScore(name, key, d);
		return dr.longValue();
	}

	@Override
	public boolean exists(N name, K key) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public Long size(N name) {
		return zSetOperations.zCard(name);
	}

	@Override
	public Set<N> getNames(N start, N end, int limit) {
		//keys
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public Set<N> getNamesReverse(N start, N end, int limit) {
		//keys
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public List<Tuple<K, Long>> scanByScore(N name, K keyStart, Long scoreStart, Long scoreEnd, int limit) {
		Double ds = new Double(scoreStart);
		Double de = new Double(scoreEnd);
		Set<TypedTuple<K>> r = zSetOperations.rangeByScoreWithScores(name, ds, de, 0, limit);
		List<Tuple<K, Long>> result = null;
		if(r != null && r.size() > 0){
			result = new ArrayList<Tuple<K, Long>>(r.size());
			for(TypedTuple<K> t : r){
				Long l = new Double(t.getScore()).longValue();
				result.add(new SimpleTuple<K, Long>(t.getValue(), l));
			}
		}
		return result;
	}

	@Override
	public List<Tuple<K, Long>> scanScoreReverse(N name, K keyStart, Long scoreStart, Long scoreEnd, int limit) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public Set<K> getKeysByScoreAsSet(N name, K keyStart, Long scoreStart, Long scoreEnd, int limit) {
		Double ds = new Double(scoreStart);
		Double de = new Double(scoreEnd);
		Set<K> r = zSetOperations.rangeByScore(name, ds, de, 0, limit);
		return r;
	}
	
	public List<K> getKeysByScore(final N name, final K keyStart, final Long scoreStart, final Long scoreEnd, final int limit){
		throw new RuntimeException();
	}

	@Override
	public Long rank(N name, K key) {
		Long rank = zSetOperations.rank(name, key);
		return rank;
	}

	@Override
	public Long rankReverse(N name, K key) {
		Long rrank = zSetOperations.reverseRank(name, key);
		return rrank;
	}

	@Override
	public List<Tuple<K, Long>> getByrange(N name, int offset, int limit) {
		Set<TypedTuple<K>> r = zSetOperations.rangeWithScores(name, offset, offset + limit);
		List<Tuple<K, Long>> result = null;
		if(r != null && r.size() > 0){
			result = new ArrayList<Tuple<K, Long>>(r.size());
			for(TypedTuple<K> t : r){
				Long l = new Double(t.getScore()).longValue();
				result.add(new SimpleTuple<K, Long>(t.getValue(), l));
			}
		}
		return result;
	}

	@Override
	public List<Tuple<K, Long>> getByrangeReverse(N name, int offset, int limit) {
		Set<TypedTuple<K>> r = zSetOperations.reverseRangeWithScores(name, offset, offset + limit);
		List<Tuple<K, Long>> result = null;
		if(r != null && r.size() > 0){
			result = new ArrayList<Tuple<K, Long>>(r.size());
			for(TypedTuple<K> t : r){
				Long l = new Double(t.getScore()).longValue();
				result.add(new SimpleTuple<K, Long>(t.getValue(), l));
			}
		}
		return result;
	}

	@Override
	public Long clear(N name) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public Long count(N name, long start, long end) {
		Double ds = new Double(start);
		Double de = new Double(end);
		Long num = zSetOperations.count(name, ds, de);
		return num;
	}

	@Override
	public Long sum(N name, long start, long end) {
//		Double ds = new Double(start);
//		Double de = new Double(end);
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public Long avg(N name, long start, long end) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public Long deleteByrank(N name, long rankStart, long rankEnd) {
		Long l = zSetOperations.removeRange(name, rankStart, rankEnd);
		return l;
	}

	@Override
	public Long deleteByScore(N name, long scoreStart, long scoreEnd) {
		Double ds = new Double(scoreStart);
		Double de = new Double(scoreEnd);
		Long l = zSetOperations.removeRangeByScore(name, ds, de);
		return l;
	}

	@Override
	public List<Tuple<K, Long>> popFront(N name, int limit) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public List<Tuple<K, Long>> popBack(N name, int limit) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}

	@Override
	public Boolean multiSet(N name, Set<Tuple<K, Long>> tuples) {
		if(null != tuples && tuples.size() > 0){
			Set<TypedTuple<K>> buff = new HashSet<TypedTuple<K>>(tuples.size());
			for(Tuple<K, Long> tuple : tuples){
				Double d = tuple.getValue().doubleValue();
				TypedTuple<K> typeTuple = new DefaultTypedTuple<K>(tuple.getKey(), d);
				buff.add(typeTuple);
			}
			Long l = zSetOperations.add(name, buff);
			if(l > 0 ){
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean multiSet(N name, Map<K, Long> values) {
		if(null != values){
			
			Set<TypedTuple<K>> buff = new HashSet<TypedTuple<K>>();
			for(Entry<K, Long> entry : values.entrySet()){
				Double d = entry.getValue().doubleValue();
				TypedTuple<K> typeTuple = new DefaultTypedTuple<K>(entry.getKey(), d);
				buff.add(typeTuple);
			}
			Long l = zSetOperations.add(name, buff);
			if(l > 0 ){
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Tuple<K, Long>> multiGet(N name, Set<K> keys) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not serported");
	}
	
	public Map<K, Long> multiGetAsMap(final N name, final Set<K> keys){
		throw new RuntimeException("not serported");
	}

	@Override
	public Boolean multiDelete(N name, Set<K> keys) {
		if(null != keys && keys.size() > 0){
			Object[] b = keys.toArray();
			Long l = zSetOperations.remove(name, b);
			System.out.println(l);
			return true;
		}
		return false;
	}

}
