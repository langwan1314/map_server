package com.youngo.ssdb.core;

import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.dao.DataAccessException;

/**
 * 
 * @author zhezhiren
 *
 * @param <T>
 * @since 1.0
 */
public interface SsdbDo<T> {
	T doInSsdb(SSDB ssdb) throws DataAccessException;
}
