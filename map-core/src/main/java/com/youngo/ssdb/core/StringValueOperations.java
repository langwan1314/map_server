package com.youngo.ssdb.core;

import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.stereotype.Repository;

@Repository("stringValueOperations")
public class StringValueOperations extends DefaultValueOperations<String, String> {
	
	public StringValueOperations() {
		super(null, String.class, String.class);
	}
	
	public StringValueOperations(SSDB ssdb) {
		super(ssdb, String.class, String.class);
	}
	
}