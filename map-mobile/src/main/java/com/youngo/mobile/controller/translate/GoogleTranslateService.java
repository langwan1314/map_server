package com.youngo.mobile.controller.translate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.youngo.core.exception.RestException;
import com.youngo.core.model.Result;
import com.youngo.mobile.model.google.GoogleTranslateResult;
import com.youngo.mobile.model.google.Translation;
import com.youngo.ssdb.core.HashMapOperations;

@Service
public class GoogleTranslateService implements TranslateService{
	
	private SSLContext sslcontext;  
	private X509TrustManager tm;
	private SSLConnectionSocketFactory sslsf;
    private CloseableHttpClient httpclient;
    
    @Autowired
    @Qualifier("stringHashMapOperations")
    private HashMapOperations<String, String, String> hashMapOperation;
    
    private static String googleAddress = "https://www.googleapis.com/language/translate/v2?key=AIzaSyAZ6DYW_w_iWdHmZYk-wPChP9NmkpByUz8";
    
    public GoogleTranslateService(){
    	try {
			sslcontext = SSLContext.getInstance("SSL");
			tm = new X509TrustManager() {  
				public void checkClientTrusted(X509Certificate[] xcs,  
						String string) throws CertificateException {  
				}  
				
				public void checkServerTrusted(X509Certificate[] xcs,  
						String string) throws CertificateException {  
				}  
				
				public X509Certificate[] getAcceptedIssuers() {  
					return null;  
				}
			};  
			sslcontext.init(null, new TrustManager[] { tm }, null);
			
			sslsf = new SSLConnectionSocketFactory(
		            sslcontext,
		            new String[] { "TLSv1" },
		            null,
		            SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			
			httpclient = HttpClients.custom()
		            .setSSLSocketFactory(sslsf)
		            .build();
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
		}
    }

	@Override
	public Translation translate(String source, String tl) {
		if(source == null || "".equals(source.trim())){
    		return null;//TODO 针对空数据，需要确定是抛异常还是 返回空
    	}
    	boolean b = hashMapOperation.exists("languages_en", tl);
    	if(!b){
    		throw new RestException(Result.trans_language_notSupport, Result.trans_language_notSupport_msg);
    	}
    	StringBuilder sb = new StringBuilder(googleAddress);
    	
		try {
			sb.append("&q=").append(URLEncoder.encode(source, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
				
    	sb.append("&target=").append(tl);
    	HttpGet httpget = new HttpGet(sb.toString());
    	
    	try {
        	ResponseHandler<GoogleTranslateResult> responseHandler = new GoogleResultHandler();  
        	GoogleTranslateResult result;  
        	
        	result = httpclient.execute(httpget, responseHandler);  
        	
        	if(null != result && null != result.getData() && null != result.getData().getTranslations()){
        		return result.getData().getTranslations().get(0);
        	}
        } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
        }
    	return null;
	}
	
	public Translation translate(String source, String tl, String sl) {
		//TODO
		throw new RuntimeException(" Not implement ");
	}
	
	@Deprecated
    public List<Translation> translate(String[] source, String target){
    	if(source == null || source.length <= 0 || target == null){
    		return null;//TODO 针对空数据，需要确定是抛异常还是 返回空
    	}
    	boolean b = hashMapOperation.exists("", target);
    	if(!b){
    		throw new RestException(0, "");//TODO 不正确的参数
    	}
    	StringBuilder sb = new StringBuilder(googleAddress);
    	for(int i = 0, j = source.length; i < j; i++){
    		if(source[i] != null){
    			try {
					sb.append("&q=").append(URLEncoder.encode(source[i], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
				}
    		}
    	}
    	sb.append("&target=").append(target);
    	HttpGet httpget = new HttpGet(sb.toString());
    	
    	try {
        	ResponseHandler<GoogleTranslateResult> responseHandler = new GoogleResultHandler();  
        	GoogleTranslateResult result;  
        	
        	result = httpclient.execute(httpget, responseHandler);  
        	if(null != result && null != result.getData()){
        		return result.getData().getTranslations();
        	}
        } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
        }
    	return null;
    }


	
}
