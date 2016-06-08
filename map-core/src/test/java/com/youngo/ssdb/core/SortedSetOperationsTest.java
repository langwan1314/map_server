package com.youngo.ssdb.core;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.nutz.ssdb4j.SSDBs;
import org.nutz.ssdb4j.spi.SSDB;

import com.youngo.ssdb.core.entity.Tuple;
import com.youngo.ssdb.entity.User;

/**
 * 
 * @author zhezhiren
 * 
 * @see SortedSetOperations
 * @since 1.0
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SortedSetOperationsTest {
	SSDB ssdb = null;
	SortedSetOperations<String, User> operation = null;
	
	String name1 = "zhe1";
	String name2 = "zhe2";
	String name3 = "zhe3";
	
	User key11 = new User("zhe11", "zhi11", "ren11");
	User key12 = new User("zhe12", "zhi12", "ren12");
	User key13 = new User("zhe13", "zhi13", "ren13");
	
	User key21 = new User("zhe21", "zhi21", "ren21");
	User key22 = new User("zhe22", "zhi22", "ren22");
	User key23 = new User("zhe23", "zhi23", "ren23");
	
	User key31 = new User("zhe31", "zhi31", "ren31");
	User key32 = new User("zhe32", "zhi32", "ren32");
	User key33 = new User("zhe33", "zhi33", "ren33");
	
	
	
	@Before
	public void setUp() throws Exception {
		ssdb = SSDBs.pool("121.42.32.149", 8888, 30000, null);
		operation = new DefaultSortedSetOperations<String, User>(ssdb, String.class, User.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSet() {
		Boolean b = operation.set(name1, key11, 11L);
		assertTrue(b);
		b = operation.set(name1, key11, 12L);
		assertTrue(b);
		b = operation.set(name1, key13, 13L);
		assertTrue(b);
		Long score = operation.get(name1, key11);
		assertEquals(new Long(11), score);
		score = operation.get(name1, key12);
		assertEquals(new Long(12), score);
		score = operation.get(name1, key13);
		assertEquals(new Long(13), score);
		operation.clear(name1);
	}

	@Test
	public void testGet() {
		Boolean b = operation.set(name1, key11, 11L);
		assertTrue(b);
		b = operation.set(name1, key11, 12L);
		assertTrue(b);
		b = operation.set(name1, key13, 13L);
		assertTrue(b);
		Long score = operation.get(name1, key11);
		assertEquals(new Long(11), score);
		score = operation.get(name1, key12);
		assertEquals(new Long(12), score);
		score = operation.get(name1, key13);
		assertEquals(new Long(13), score);
		operation.clear(name1);
	}

	@Test
	public void testDelete() {
		Boolean b = operation.delete(name1, key11);
		assertTrue(b);
		Long score = operation.get(name1, key11);
		assertNull(score);
		b = operation.set(name1, key11, 11L);
		assertTrue(b);
		score = operation.get(name1, key11);
		assertEquals(new Long(11), score);
		operation.clear(name1);
	}

	@Test
	public void testIncrement() {
		operation.set(name1, key11, 3L);
		Long score = operation.increment(name1, key11, 2);
		assertEquals(new Long(5), score);
		score = operation.increment(name1, key11, -2);
		assertEquals(new Long(3), score);
		operation.clear(name1);
	}

	@Test
	public void testExists() {
		operation.set(name1, key11, 1L);
		Boolean b = operation.exists(name1, key11);
		assertTrue(b);
		operation.delete(name1, key11);
		b = operation.exists(name1, key11);
		assertTrue(!b);
		Long i = operation.clear(name1);
		assertEquals(new Long(0), i);
		operation.clear(name1);
	}

	@Test
	public void testSize() {
		Boolean b = operation.set(name1, key11, 11L);
		assertTrue(b);
		b = operation.set(name1, key11, 12L);
		assertTrue(b);
		b = operation.set(name1, key13, 13L);
		assertTrue(b);
		Long size = operation.size(name1);
		assertEquals(new Long(3), size);
		operation.clear(name1);
		
	}

	@Test
	public void testGetNames() {
		Boolean b = operation.set(name1, key11, 11L);
		assertTrue(b);
		b = operation.set(name2, key22, 22L);
		assertTrue(b);
		b = operation.set(name3, key33, 33L);
		assertTrue(b);
		Set<String> names = operation.getNames(name1, name3, 10);
		assertEquals(new Long(2), new Long(names.size()));
		assertEquals(name2, names.iterator().next());
		operation.clear(name1);
		operation.clear(name2);
		operation.clear(name3);
		
	}

	@Test
	public void testGetNamesReverse() {
		Boolean b = operation.set(name1, key11, 11L);
		assertTrue(b);
		b = operation.set(name2, key22, 22L);
		assertTrue(b);
		b = operation.set(name3, key33, 33L);
		assertTrue(b);
		Set<String> names = operation.getNamesReverse(name1, name3, 10);
		assertEquals(new Long(0), new Long(names.size()));
		names = operation.getNamesReverse(name3, name1, 10);
		assertEquals(new Long(2), new Long(names.size()));
		Iterator<String> iterator = names.iterator();
		assertEquals(name2,iterator.next());
		assertEquals(name1, iterator.next());
		operation.clear(name1);
		operation.clear(name2);
		operation.clear(name3);
		
	}

	@Test
	public void testScanByScore() {
		Boolean b = operation.set(name1, key11, 11L);
		assertTrue(b);
		b = operation.set(name1, key11, 12L);
		assertTrue(b);
		b = operation.set(name1, key13, 13L);
		assertTrue(b);
		
		List<Tuple<User, Long>> keys = operation.scanByScore(name1, null, 11L, 13L, 100);
		assertEquals(new Long(3), new Long(keys.size()));
		assertEquals(key11, keys.get(0).getKey());
		assertEquals(new Long(11), keys.get(0).getValue());
		assertEquals(key12, keys.get(1).getKey());
		assertEquals(new Long(12), keys.get(1).getValue());
		assertEquals(key13, keys.get(2).getKey());
		assertEquals(new Long(13), keys.get(2).getValue());
		
		operation.clear(name1);
		
	}

	@Test
	public void testScanScoreReverse() {
		Boolean b = operation.set(name1, key11, 11L);
		assertTrue(b);
		b = operation.set(name1, key11, 12L);
		assertTrue(b);
		b = operation.set(name1, key13, 13L);
		assertTrue(b);
		
		List<Tuple<User, Long>> keys = operation.scanScoreReverse(name1, null, 11L, 13L, 100);
		assertEquals(new Long(0), new Long(keys.size()));
		
		
		keys = operation.scanScoreReverse(name1, null, 13L, 11L, 100);
		assertEquals(new Long(3), new Long(keys.size()));
		assertEquals(key13, keys.get(0).getKey());
		assertEquals(new Long(13), keys.get(0).getValue());
		assertEquals(key12, keys.get(1).getKey());
		assertEquals(new Long(12), keys.get(1).getValue());
		assertEquals(key11, keys.get(2).getKey());
		assertEquals(new Long(11), keys.get(2).getValue());
		
		operation.clear(name1);
	}

	@Test
	public void testGetKeyByScoreAsSet() {
		Boolean b = operation.set(name1, key11, 11L);
		assertTrue(b);
		b = operation.set(name1, key11, 12L);
		assertTrue(b);
		b = operation.set(name1, key13, 13L);
		assertTrue(b);
		
		Set<User> keys = operation.getKeysByScoreAsSet(name1, null, 11L, 12L, 100);
		assertEquals(new Long(2), new Long(keys.size()));
		Iterator<User> users = keys.iterator();
		
		assertEquals(key11, users.next());
		assertEquals(key12, users.next());
		
		operation.clear(name1);
	}

	@Test
	public void testRank() {
		
		
	}

	@Test
	public void testRankReverse() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetByrange() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetByrangeReverse() {
		fail("Not yet implemented");
	}

	@Test
	public void testClear() {
		fail("Not yet implemented");
	}

	@Test
	public void testCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testSum() {
		fail("Not yet implemented");
	}

	@Test
	public void testAvg() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteByrank() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteByScore() {
		fail("Not yet implemented");
	}

	@Test
	public void testPopFront() {
		fail("Not yet implemented");
	}

	@Test
	public void testPopBack() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiSetNSetOfTupleOfKLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiSetNMapOfKLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiDelete() {
		fail("Not yet implemented");
	}

}
