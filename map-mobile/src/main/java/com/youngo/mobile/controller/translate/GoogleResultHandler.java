package com.youngo.mobile.controller.translate;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngo.mobile.model.google.GoogleTranslateResult;

public class GoogleResultHandler implements ResponseHandler<GoogleTranslateResult> {
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public GoogleTranslateResult handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
		final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        try{
        	if (statusLine.getStatusCode() == 200 || statusLine.getStatusCode() == 400) {
        		//400是全局参数异常  按理来说正确的配置不会产生400错误
        		if (entity == null)
        			return null;
        		else{
        			byte[] bytes = EntityUtils.toByteArray(entity);
        			GoogleTranslateResult result = objectMapper.readValue(bytes, 0, bytes.length, GoogleTranslateResult.class);
        			return result;
        		}
        	}else if(statusLine.getStatusCode() == 403){
        		
        		//用户没有权限访问google接口，可能的原因为账号关闭，
        		//TODO 用户账号缺钱需要测试是不是返回该状态
        		return null;
        	}
        	else{
        		System.out.println(statusLine.getStatusCode());
        		throw new HttpResponseException(statusLine.getStatusCode(),
        				statusLine.getReasonPhrase());
        	}
        }finally{
        	EntityUtils.consume(entity);
        }
	}
}