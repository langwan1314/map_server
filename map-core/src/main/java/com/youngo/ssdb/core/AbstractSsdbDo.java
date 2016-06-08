package com.youngo.ssdb.core;

import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;

/**
 * 
 * @author zhezhiren
 * 
 * @param <T>
 * @since 1.0
 */
public abstract class AbstractSsdbDo<T> implements SsdbDo<T>{
	public final static String status_eeror = "error";
	public final static String status_client_error = "client_error";//客户端参数出错时返回了这个状态
	
	
	public final T doInSsdb(SSDB ssdb) {
		Response resp = inSsdb(ssdb);
		if(resp.ok()){
			return dowithResponse(resp);
		}else if(resp.notFound()){
			return null;
		}else{
			throw new RuntimeException();			
			//TODO
			//对于value的incr方法，当实际值不为整数时，抛出异常正确。但需要带异常信息
		}
	}
	
	abstract T dowithResponse(Response resp);
	
	protected abstract Response inSsdb(SSDB ssdb);
}

abstract class DeserializingStringSsdbDo extends AbstractSsdbDo<String>{
	String dowithResponse(Response resp){
		return resp.asString();
	}
}

abstract class StatusBoolSsdbDo implements SsdbDo<Boolean>{
	public final Boolean doInSsdb(SSDB ssdb) {
		Response resp = inSsdb(ssdb);
		return resp.ok();
	}
	
	protected abstract Response inSsdb(SSDB ssdb);
}

abstract class DeserializingBoolFromResultSsdbDo extends AbstractSsdbDo<Boolean>{
	Boolean dowithResponse(Response resp){
		int i = resp.asInt();
		return i == 1;
	}
}

abstract class DeserializingIntSsdbDo extends AbstractSsdbDo<Integer>{
	Integer dowithResponse(Response resp){
		return resp.asInt();
	}
}

abstract class DeserializingLongSsdbDo extends AbstractSsdbDo<Long>{
	Long dowithResponse(Response resp){
		return resp.asLong();
	}
}

abstract class DeserializingDoubleSsdbDo extends AbstractSsdbDo<Double>{
	Double dowithResponse(Response resp){
		return resp.asDouble();
	}
}

abstract class DeserializingNullSsdbDo extends AbstractSsdbDo<Null>{
	Null dowithResponse(Response resp){
		return null;
	}
}

class Null{
}