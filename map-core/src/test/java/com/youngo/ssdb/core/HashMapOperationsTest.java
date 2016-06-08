package com.youngo.ssdb.core;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.nutz.ssdb4j.SSDBs;
import org.nutz.ssdb4j.spi.SSDB;

import com.youngo.ssdb.entity.Key;
import com.youngo.ssdb.entity.User;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HashMapOperationsTest {
	
	SSDB ssdb = null;
	public HashMapOperations<String, Key, User> operation = null;
	
	String name1 = "zhe1";
	String name2 = "zhe2";
	String name3 = "zhe3";
	
	Key key11 = new Key("kye11");
	Key key12 = new Key("kye12");
	Key key13 = new Key("kye13");
	
	Key key21 = new Key("kye21");
	Key key22 = new Key("kye22");
	Key key23 = new Key("kye23");
	
	Key key31 = new Key("kye31");
	Key key32 = new Key("kye32");
	Key key33 = new Key("kye33");
	
	
	User user11 = new User("zhe11", "zhi11", "ren11");
	User user12 = new User("zhe12", "zhi12", "ren12");
	User user13 = new User("zhe13", "zhi13", "ren13");
	
	User user21 = new User("zhe21", "zhi21", "ren21");
	User user22 = new User("zhe22", "zhi22", "ren22");
	User user23 = new User("zhe23", "zhi23", "ren23");
	
	User user31 = new User("zhe31", "zhi31", "ren31");
	User user32 = new User("zhe32", "zhi32", "ren32");
	User user33 = new User("zhe33", "zhi33", "ren33");
	
	
	
	@Before
	public void setUp() throws Exception {
		ssdb = SSDBs.pool("192.168.10.56", 8888, 30000, null);
		operation = new DefaultHashMapOperations<String, Key, User>(ssdb, String.class, Key.class, User.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSet() {
		operation.clear(name1);
		boolean b = operation.set(name1, key11, user11);
		assertTrue(b);
		b = operation.set(name1, key12, user12);
		assertTrue(b);
		b = operation.set(name1, key13, user13);
		assertTrue(b);
		User s = operation.get(name1, key11);
		assertEquals(user11, s);
		operation.clear(name1);
	}

	@Test
	public void testGet() {
		operation.clear(name1);
		boolean b = operation.set(name1, key11, user11);
		assertTrue(b);
		User s = operation.get(name1, key11);
		assertEquals(user11, s);
		s = operation.get(name1, key12);
		assertNull(s);
		operation.clear(name1);
	}

	@Test
	public void testDelete() {
		operation.clear(name1);
		operation.set(name1, key11, user11);
		operation.set(name1, key12, user12);
		operation.set(name1, key13, user13);
		Boolean b = operation.delete(name1, key11);
		assertTrue(b);
		User s = operation.get(name1, key11);
		assertNull(s);
		operation.clear(name1);
	}

	@Test
	public void testIncrement() {
		fail("Not yet implemented");
	}

	@Test
	public void testExists() {
		fail("Not yet implemented");
	}

	@Test
	public void testSize() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNames() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNamesReverse() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetKeys() {
		fail("Not yet implemented");
	}

	@Test
	public void testScanAllByName() {
		operation.clear(name1);
		operation.set(name1, key11, user11);
		operation.set(name1, key12, user12);
		operation.set(name1, key13, user13);
		Map<Key, User> users = operation.getAllByName(name1);
		assertEquals(new Integer(3), new Integer(users.size()));
		User s = users.get(key11);
		assertEquals(user11, s);
	}

	@Test
	public void testScanByKeys() {
		fail("Not yet implemented");
	}

	@Test
	public void testScanByKeysReverse() {
		fail("Not yet implemented");
	}

	@Test
	public void testClear() {
		operation.set(name1, key11, user11);
		Integer i = operation.clear(name1);
		assertEquals(new Integer(1), i);
	}

	@Test
	public void testMultiSet() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiGetByKeys() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiDelete() {
		fail("Not yet implemented");
	}

}
