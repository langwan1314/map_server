package com.youngo.mobile.controller.translate;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

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

import com.youngo.core.common.MURLEncoder;
import com.youngo.core.exception.RestException;
import com.youngo.core.model.Result;
import com.youngo.mobile.model.google.Translation;
import com.youngo.ssdb.core.HashMapOperations;

@Service
public class GoogleFreeTranslateService implements TranslateService{
	
    private static String Wb = "+-a^+6", Vb = "+-3^+b+-f";
	private static char t = 'a'; 
	private static char Ub = '+';
	
	private volatile String[] TKK = null;
	
	private SSLContext sslcontext;  
	private X509TrustManager tm;
	private SSLConnectionSocketFactory sslsf;
    private CloseableHttpClient httpclient;
    //sl 源语言    tl 目标语言  hl 可能为请求的本地语言  
    
    //&source=btn去掉对结果没影响  &tco=2去掉没影响  kc=0去掉没影响  &tsel=6去掉没影响 &ssel=3去掉没影响
    //&srcrom=1 去掉没影响
    final static String URL = "https://translate.google.cn/translate_a/single?client=t&tl={0}&dt=ld&dt=t&ie=UTF-8&oe=UTF-8&tk={2}&q={1}";
    final static String URLWithSl = "https://translate.google.cn/translate_a/single?client=t&sl={3}&tl={0}&dt=ld&dt=t&ie=UTF-8&oe=UTF-8&tk={2}&q={1}";
    @Autowired
    @Qualifier("stringHashMapOperations")
    private HashMapOperations<String, String, String> hashMapOperation;
	
    public GoogleFreeTranslateService(){
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
		return translate(source, tl, null);
	}
    
	public Translation translate(String source, String tl, String sl) {
    	if(source == null || "".equals(source.trim())){
    		return null;
    	}
    	boolean b = false;
    	if(null == tl || "".equals(tl.trim())){
    		throw new RestException(0, "");//TODO
    	}else{
    		b = hashMapOperation.exists("languages_en", tl);
    		if(!b){
    			throw new RestException(Result.trans_language_notSupport, Result.trans_language_notSupport_msg);
    		}
    	}
    	String tk = getTk2(source);
    	try {
    		source = MURLEncoder.encode(source, "utf-8");
    	} catch (UnsupportedEncodingException e1) {
    	}
    	String url = null;
    	if(null != sl && !"".equals(sl.trim())){
    		b = hashMapOperation.exists("languages_en", sl);
    		if(!b){
    			throw new RestException(Result.trans_language_notSupport, Result.trans_language_notSupport_msg);
    		}
    		url = MessageFormat.format(URLWithSl, tl, source, tk, sl);
    	}else{
    		url = MessageFormat.format(URL, tl, source, tk);
    		
    	}
		
    	HttpGet httpget = new HttpGet(url);
    	try {
        	ResponseHandler<Translation> responseHandler = new GoogleFreeResultHandler();  
        	Translation transtated;  
        	
        	transtated = httpclient.execute(httpget, responseHandler);  
            if(null != transtated){
            	b = hashMapOperation.exists("languages_en", transtated.getDetectedSourceLanguage());
            	if(!b){
            		transtated.setDetectedSourceLanguage(null);
            	}
            	return transtated;
            }
        } catch(RestException restException){
        	refreshTKK();
        	return translate(source, tl, sl);
        }
    	catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
        }
    	
		return null;
	}
    
	@Override
	@Deprecated
	public List<Translation> translate(String[] source, String target) {
		if(source == null || source.length != 0 || target == null){
    		return null;//TODO 针对空数据，需要确定是抛异常还是 返回空
    	}
		if(source.length != 1){
			return null;//TODO 仅支持一个的翻译。
		}
    	boolean b = hashMapOperation.exists("", target);
    	if(!b){
    		throw new RestException(0, "");//TODO 不正确的参数
    	}
    	String url = MessageFormat.format(URL, target, source[0], getTk2(source[0]));
		
    	HttpGet httpget = new HttpGet(url);
    	try {
        	ResponseHandler<Translation> responseHandler = new GoogleFreeResultHandler();  
        	Translation result;  
        	
        	result = httpclient.execute(httpget, responseHandler);  
            if(null != result){
            	List<Translation> l = new ArrayList<Translation>(1);
            	l.add(result);
            	return l;
            }
        } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
        }
    	
		return null;
	}
	
	
	public static String getTk(String a){
		Long l = System.currentTimeMillis();
		long i = l /(1000 * 3600 );
		String b =i+ "";
		List<Integer> d = new ArrayList<Integer>();
		int e = 0; 
		for (int f = 0; f < a.length(); f++) {
		    char g = a.charAt(f);
		    if(128 > g){
		    	 d.add((int) g);
		    }else{
		    	if(2048 > g ){
		    		 d.add(g >> 6 | 192);
		    	}else{
		    		if(55296 == (g & 64512) && f + 1 < a.length() && 56320 == (a.charAt(f + 1) & 64512)){
		    			g = (char) (65536 + ((g & 1023) << 10) + (a.charAt(++f) & 1023));
		    			d.add(g >> 18 | 240);
    					d.add(g >> 12 & 63 | 128);
    				    
		    		}else{
		    			d.add(g >> 12 | 224);
		    			d.add(g >> 6 & 63 | 128);
		    			d.add(g & 63 | 128);
		    		}
		    	}
		    }
		}
		long q = 0L;
		if(b == null || "".equals(b.trim())){
		}else{
			q = Long.parseLong(b);
		}
		for (e = 0; e < d.size(); e++){
			q += d.get(e);
			q = oM(q, Wb);
		}
		
		q = oM(q, Vb);
		if(0 > q){
			q = (q & 2147483647) + 2147483648L;
		}
		q = q % 1000000;
		a = q + "";
//		int length = a.length();
//		if(length < 6){
//			StringBuilder sb = new StringBuilder();
//			for(int i = 0; i < 6-length; i++){
//				sb.append("0");
//			}
//			sb.append(a);
//			a = sb.toString();
//		}
		return a + "." + (q ^ Long.parseLong(b));
	}
	
	public static long oM(long a, String b){
		for (int c = 0; c < b.length() - 2; c += 3) {
		    char d = b.charAt(c + 2);
		    long xx = d;
		    xx = (xx >= t ? xx - 87 : Long.parseLong(new Character(d).toString()));
		    xx = b.charAt(c + 1) == Ub ? a >>> xx : a << xx;
		    xx = new Long(xx).intValue();
		    a = b.charAt(c) == Ub ? a + xx & 4294967295L : a ^ xx;
		}
		return a;
	}
	public String getTk2(String a){
		return getTk2(a, false);
	}
	
	public String getTk2(String a, boolean fressTKK){
		List<Integer> e = new ArrayList<Integer>();
		String[] tkk;
		if(this.TKK == null || fressTKK){
			refreshTKK();
		}
		tkk = this.TKK;
		long b  = Long.parseLong(tkk[0]);
		for (int g = 0; g < a.length(); g++) {
	        char m = a.charAt(g);
	        if(128 > m){
	        	e.add((int)m);
	        }else{
	        	if(2048 > m){
	        		e.add(m >>6 | 192);
	        	}else{
	        		if(55296 == (m & 64512) && g + 1 < a.length() && 56320 == (a.charAt(g + 1) & 64512)){
	        			m = (char) (65536 + ((m & 1023) << 10) + (a.charAt(++g) & 1023));
	        			e.add(m >> 18 | 240);
	        			e.add(m >> 12 & 63 | 128);
	        		}else{
	        			e.add(m >> 12 | 224);
	        			e.add(m >> 6 & 63 | 128);
	        			e.add(m & 63 | 128);
	        		}
	        	}
	        }
	    }
		
		long q = b;
		for (int f = 0; f < e.size(); f++){
			q += e.get(f);
			q = tM(q, Wb);
		}
		q = tM(q, Vb);
		
		long dbuff = Long.parseLong(tkk[1]);
		int d = (int) dbuff;
		q ^= d;
		
		if(0 > q){
			q = (q & 2147483647L) + 2147483648l;
		}
		q = q % 1000000;
		a = q + "";
		
		return a + "." + (q ^ b);
	}
	
	public static long tM(long a , String b){
		for (int c = 0; c < b.length() - 2; c += 3) {
	        char d = b.charAt(c + 2);
	        long xx = d;
	        xx = (xx >= t ? xx - 87 : Long.parseLong(new Character(d).toString()));
	        if(b.charAt(c + 1) == Ub ){
	        	int ii = new Long(a).intValue();
	        	xx = ii>>>xx;
	        }else{
	        	 xx = a << xx;
	        	 xx = new Long(xx).intValue();
	        }
	       // xx = b.charAt(c + 1) == Yb ? a >>> xx :
	        a = b.charAt(c) == Ub ? a + xx & 4294967295L : a ^ xx;
	        a = new Long(a).intValue();
	    }
	    return a;
	}
	
	public String[] refreshTKK(){
//		String[] result = {"406443", "2682408158"};
//		return result;
		String[] TKK = null;
		String result = getGoogleContent();
		if(null != result){
			try{
				int index = result.indexOf("TKK=eval");
				
				int start1 = result.indexOf("\\x3d", index);
				int end1 = result.indexOf(';', start1);
				String as = result.substring(start1 + 4, end1);
				long a = Long.parseLong(as.trim());
				
				int start2 = result.indexOf("\\x3d", end1);
				int end2 = result.indexOf(';', start2);
				String bs = result.substring(start2 + 4, end2);
				long b = Long.parseLong(bs.trim());
				
				int hstart = result.indexOf("return", end2);
				int hend = result.indexOf('+', hstart);
				String hs = result.substring(hstart + 6, hend);
				
				TKK = new String[2];
				TKK[0] = hs.trim();
				long v = a + b;
				TKK[1] = new String(v + "");
				System.out.print(TKK);
			}catch(Throwable t){
				//TOOD
			}
		}
		this.TKK = TKK;
		return TKK;
	}
	
	public static String getGoogleContent(){
		HttpURLConnection conn = null;
		try {
			URL ur = new URL("http://translate.google.cn/");
			conn = (HttpURLConnection) ur.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(10000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Host", "translate.google.cn");
			conn.setRequestProperty("Pragma", "no-cache");
			conn.setRequestProperty("Proxy-Connection", "keep-alive");
			conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");
			conn.setRequestProperty("X-Client-Data", "CJe2yQEIo7bJAQjBtskBCN+UygEI/ZXKAQ==");
			InputStream in = null;
			GZIPInputStream zin = null;
			byte[] buff = new byte[1024];
			int i = -1;
			try{
				in = conn.getInputStream();
				//int j = conn.getContentLength();
				//System.out.println(j);
				StringBuilder sb = new StringBuilder();
				zin = new GZIPInputStream(in);
				while ((i = zin.read(buff)) != -1) {
					String s = new String(buff, 0, i);
					sb.append(s);
				}
				//System.out.println(sb);
				String content = sb.toString();
				
				return content;
				
				
			}catch (IOException e) {
				e.printStackTrace();
				return null;
			}finally {
				if(null != in){
					try{in.close();}catch(Throwable t){}
					try{zin.close();}catch(Throwable t){}
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(null != conn){
				conn.disconnect();
			}
		}
		return null;
	}

}
