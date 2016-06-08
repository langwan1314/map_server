package com.youngo.ssdb.core;

import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.stereotype.Repository;

@Repository("stringSortedSetOperations")
public class StringSortedSetOperations extends DefaultSortedSetOperations<String, String>{
	
	public StringSortedSetOperations() {
		super(null, String.class, String.class);
	}
	
	public StringSortedSetOperations(SSDB ssdb) {
		super(ssdb, String.class, String.class);
	}

}
