package com.youngo.ssdb.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.nutz.ssdb4j.SSDBs;
import org.nutz.ssdb4j.spi.SSDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngo.ssdb.entity.Auth;
import com.youngo.ssdb.entity.Key;
import com.youngo.ssdb.entity.User;
import com.youngo.ssdb.core.ValueOperations;
import com.youngo.ssdb.core.entity.SimpleTuple;
import com.youngo.ssdb.core.entity.Tuple;

/**
 * 
 * @author zhezhiren
 * 
 * @see ValueOperations
 * @since 1.0
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValueOperationsTest {
	SSDB ssdb = null;
	ValueOperations<Key, User> operation = null;
	Auth auth1 = new Auth("auth", "pas  swo\nrd");
	Auth auth2 = new Auth("auth2", "password2");
	Auth auth3 = new Auth("auth3", "password3");
	User user1 = new User("zhezhiren", "123456", "address", auth1);
	User user2 = new User("zhezhiren2", "123456", "address2", auth2);
	User user3 = new User("zhezhiren3", "123456", "address3", auth3);
	byte[] bys = new byte[3];
	
	User user4 = new User("zhe4", "zhi4", "ren4");
	User user5 = new User("zhe5", "zhi5", "ren5");
	User user6 = new User("zhe6", "zhi6", "ren6");
	
	Key key = new Key("key1");
	Key key2 = new Key("key2");
	Key key3 = new Key("key3");
	Key key6 = new Key("key6");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		ssdb = SSDBs.pool("47.89.28.120", 8888, 30000, null);
		operation = new DefaultValueOperations<Key, User>(ssdb, Key.class, User.class);
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testSet() {
		byte b5 = 5;
		byte b6 = 6;
		byte b7 = 7;
		bys[0] = b5;
		bys[1] = b6;
		bys[2] = b7;
		user1.setBtyes(bys);
		operation.set(key, user1);
		User s = operation.get(key);
		System.out.println(s);
		Assert.assertEquals(user1, s);
	}

	@Test
	@Ignore
	public void testSetWithTimeToLive() {
		operation.setWithTimeToLive(key2, user2, 2);
		User s = operation.get(key2);
		Assert.assertEquals(user2, s);
		
		try{
			Thread.sleep(1 * 1000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		Integer ttl = operation.getTimeToLive(key2);
		Assert.assertTrue(ttl >= 0 && ttl <=2);
		
		try {
			Thread.sleep(1 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		s = operation.get(key2);
		Assert.assertNull(s);
	}

	@Test
	@Ignore
	public void testSetIfAbsent() {
		boolean b = operation.setIfAbsent(key, user2);
		Assert.assertTrue(!b);
		User s = operation.get(key);
		Assert.assertTrue(!s.equals(user2));
		Assert.assertEquals(user1, s);
		operation.setIfAbsent(key2, user2);
		s = operation.get(key2);
		Assert.assertEquals(user2, s);
	}

	@Test
	@Ignore
	public void testExpire() {
		Integer i = operation.expire(key, 5);
		Assert.assertEquals(i, new Integer(5));
	}

	@Test
	@Ignore
	public void testGetTimeToLive() {
		boolean b = operation.setWithTimeToLive(key, user1, 2);
		Assert.assertTrue(b);
//		System.out.println(operation.getTimeToLive(key));
		Integer ttl = operation.getTimeToLive(key);
		Assert.assertTrue(ttl >= 0 && ttl <=2);
		
	}

	@Test
	@Ignore
	public void testGet() {
		operation.set(key, user1);
		User s = operation.get(key);
		Assert.assertEquals(user1, s);
	}

	@Test
	@Ignore
	public void testGetAndSet() {
		boolean b = operation.set(key, user1);
		assertTrue(b);
		User s = operation.getAndSet(key, user2);
		assertEquals(user1, s);
		s = operation.get(key);
		assertEquals(user2, s);
	}

	@Test
	@Ignore
	public void testDelete() {
		boolean b = operation.set(key, user1);
		assertTrue(b);
		b = operation.delete(key);
		assertTrue(b);
		User s = operation.get(key);
		assertNull(s);
	}

	@Test(expected  = RuntimeException.class)
	@Ignore
	public void testIncrement() {
		operation.delete(key3);
		Long b = operation.increment(key3, 3);
		assertEquals(new Long(3), b);
		b = operation.increment(key3, 2);
		assertEquals(new Long(5), b);
		operation.delete(key3);
		b = operation.increment(key, 3);
	}

	@Test
	@Ignore
	public void testExists() {
		boolean b = operation.exists(key3);
		assertTrue(b);
	}

	@Test
	@Ignore
	public void testSubStr() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String s = objectMapper.writeValueAsString(user1);
		String sub = s.substring(1, s.length());
		boolean b = operation.set(key, user1);
		assertTrue(b);
		String subr = operation.subStr(key, 1, s.length() - 1);
		assertEquals(sub, subr);
	}

	@Test
	@Ignore
	public void testStrLength() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String s =  objectMapper.writeValueAsString(user1);
		boolean b = operation.set(key, user1);
		assertTrue(b);
		Integer length = operation.strLength(key);
		assertEquals(new Integer(s.length()), length);
	}

	@Test
	@Ignore
	public void testGetKeys() {
		//System.out.println(JSON.toJSON(key));
		//System.out.println(JSON.toJSON(key3));
		Set<Key> keys = operation.getKeys(key, key3, 100);
		assertEquals(new Integer(1), new Integer(keys.size()));
	}

	@Test
	@Ignore
	public void testGetKeysReverse() {
		Set<Key> keys = operation.getKeysReverse(key, key3, 100);
		assertEquals(new Integer(0), new Integer(keys.size()));
		keys = operation.getKeysReverse(key3, key, 100);
		assertEquals(new Integer(1), new Integer(keys.size()));
		assertEquals(key, keys.iterator().next());
	}

	@Test
	@Ignore
	public void testScanValues() {
		List<Tuple<Key, User>> values = operation.scanValues(key, key6, 10);
		assertEquals(new Integer(3), new Integer(values.size()));
		//assertEquals(user1, values.get(0).getValue());
		assertEquals(user4, values.get(0).getValue());
	}

	@Test
	@Ignore
	public void testScanValuesReverse() {
		List<Tuple<Key, User>> values = operation.scanValuesReverse(key, key6, 10);
		assertTrue(0==values.size());
		values = operation.scanValuesReverse(key6, key, 10);
		//System.out.println(values.size());
		//assertEquals(user1, values.get(0));
		assertEquals(new Integer(3), new Integer(values.size()));
		assertEquals(user6, values.get(0).getValue());
		
	}

	@Test
	@Ignore
	public void testMultiSetMapOfKV() {
		Map<Key, User> map = new HashMap<Key, User>();
		map.put(key, user4);
		map.put(key2, user5);
		map.put(key3, user6);
		Boolean b = operation.multiSet(map);
		assertTrue(b);
		List<Key> keys = new ArrayList<Key>();
		keys.add(key);
		keys.add(key2);
		keys.add(key3);
		List<Tuple<Key, User>> values = operation.multiGet(keys);
		assertEquals(new Integer(3), new Integer(values.size()));
		assertEquals(user4, values.get(0).getValue());
		assertEquals(user5, values.get(1).getValue());
		assertEquals(user6, values.get(2).getValue());
	}

	@Test
	@Ignore
	public void testMultiSetListOfTupleOfKV() {
		List<Tuple<Key, User>> valuse = new ArrayList<Tuple<Key, User>>();
		valuse.add(new SimpleTuple<Key, User>(key, user1));
		valuse.add(new SimpleTuple<Key, User>(key2, user2));
		valuse.add(new SimpleTuple<Key, User>(key3, user3));
		Boolean b = operation.multiSet(valuse);
		assertTrue(b);
		List<Key> keys = new ArrayList<Key>();
		keys.add(key);
		keys.add(key2);
		keys.add(key3);
		List<Tuple<Key, User>> values = operation.multiGet(keys);
		assertEquals(new Integer(3), new Integer(values.size()));
		assertEquals(user1, values.get(0).getValue());
		assertEquals(user2, values.get(1).getValue());
		assertEquals(user3, values.get(2).getValue());
	}

	@Test
	@Ignore
	public void testMultiGet() {
		Map<Key, User> map = new HashMap<Key, User>();
		map.put(key, user4);
		map.put(key2, user5);
		map.put(key3, user6);
		Boolean b = operation.multiSet(map);
		assertTrue(b);
		List<Key> keys = new ArrayList<Key>();
		keys.add(key);
		keys.add(key2);
		keys.add(key3);
		List<Tuple<Key, User>> values = operation.multiGet(keys);
		assertEquals(new Integer(3), new Integer(values.size()));
		assertEquals(user4, values.get(0).getValue());
		assertEquals(user5, values.get(1).getValue());
		assertEquals(user6, values.get(2).getValue());
	}

	@Test
	@Ignore
	public void testMultiDelete() {
		fail("Not yet implemented");
	}

}
