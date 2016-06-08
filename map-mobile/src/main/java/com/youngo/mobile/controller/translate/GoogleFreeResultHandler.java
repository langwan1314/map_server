package com.youngo.mobile.controller.translate;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.youngo.core.exception.RestException;
import com.youngo.mobile.model.google.Translation;

public class GoogleFreeResultHandler  implements ResponseHandler<Translation> {
	
	@Override
	public Translation handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
		final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        try{
        	if (statusLine.getStatusCode() == 200 || statusLine.getStatusCode() == 400) {
        		//400是全局参数异常  按理来说正确的配置不会产生400错误
        		if (entity == null)
        			return null;
        		else{
        			byte[] bytes = EntityUtils.toByteArray(entity);
        			String googleResult = new String(bytes, "utf-8");
        			if(null != googleResult && googleResult.startsWith("[")){
        				int index1 = googleResult.indexOf("\"");
        				if(index1 > 0){
        					int index2 = googleResult.indexOf("\"", index1 + 1);
        					if(index2 > 0){
        						String translatedText = googleResult.substring(index1+1, index2);
        						int index3 = googleResult.lastIndexOf("\"");
        						int index4 = googleResult.lastIndexOf("\"", index3-1);
        						String sourceLanguage = googleResult.substring(index4 + 1, index3);
        						Translation t = new Translation();
        						t.setTranslatedText(translatedText);
        						t.setDetectedSourceLanguage(sourceLanguage);
        						return t;
        					}
        				}
        				
        			}
        		}
        	}else if(statusLine.getStatusCode() == 403){
        		
        		//用户没有权限访问google接口，可能原因为tk失效或者 google改变tk策略。
        		//TODO 
        		//return null;
        		throw new RestException(0 , "");
        	}
        	else{
        		System.out.println(statusLine.getStatusCode());
        		throw new HttpResponseException(statusLine.getStatusCode(),
        				statusLine.getReasonPhrase());
        	}
        }finally{
        	EntityUtils.consume(entity);
        }
        return null;
	}
}
