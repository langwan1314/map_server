package com.youngo.ssdb.core;

import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.stereotype.Repository;

@Repository("stringHashMapOperations")
public class StringHashMapOperation extends DefaultHashMapOperations<String, String, String>{
	
	public StringHashMapOperation() {
		super(null, String.class, String.class, String.class);
	}
	
	public StringHashMapOperation(SSDB ssdb) {
		super(ssdb, String.class, String.class, String.class);
	}

}
