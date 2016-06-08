package com.youngo.mobile.other.translate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.youngo.mobile.controller.translate.GoogleResultHandler;
import com.youngo.mobile.model.google.GoogleTranslateResult;

public class GoogleTranslate {

	public static void main(String[] args) {
		
		
		
//		String s = "不 要 脸";
//			String b;
//				b = URLEncoder.encode(s);
//				//b = URLEncoder.encode(b);
//				System.out.println(b);
//				
//				BasicNameValuePair bnv = new BasicNameValuePair("q", s);
//				ArrayList<BasicNameValuePair> sss = new ArrayList<BasicNameValuePair>();
//				sss.add(bnv);
//				String oo =  URLEncodedUtils.format(sss, "utf-8");
//				System.out.println("oo=" +oo);
//				
//		String ss = "%E4%B8%8D%20%E8%A6%81%20%E8%84%B8";
//		String bb = URLDecoder.decode(ss);
//		System.out.println(bb);
				
				
//		
//		String googleResult = "[[[\"Do not worry about how the future will end at least we do not have to bother to have been constrained together but does not require verbal commitment to each other as long as we used to have\",\"别管以后将如何结束至少我们曾经相聚过不必费心地彼此约束更不需要言语的承诺只要我们曾经拥有\",,,0],[,,,\"Biéguǎn yǐhòu jiàng rúhé jiéshù zhìshǎo wǒmen céngjīng xiāngjùguò bu bì fèi xīndì bǐcǐ yuēshù gèng bù xūyào yányǔ de chéngnuò zhǐyào wǒmen céngjīng yǒngyǒu\"]],,\"\",,,[[\"别 管\",1,[[\"Do not worry about\",999,true,false],[\"Do not worry\",0,true,false],[\"Do not control\",0,true,false],[\"Never mind\",0,true,false],[\"Leave\",0,true,false]],[[0,2]],\"别管以后将如何结束至少我们曾经相聚过不必费心地彼此约束更不需要言语的承诺只要我们曾经拥有\",0,4],[\"如何\",2,[[\"how\",966,true,false],[\"what\",11,true,false],[\"how to\",0,true,false],[\"how the\",0,true,false],[\"on how\",0,true,false]],[[5,7]],,4,5],[\"以后 将\",3,[[\"the future will\",966,true,false],[\"later\",0,true,false],[\"would later\",0,true,false]],[[2,5]],,5,8],[\"结束\",4,[[\"end\",966,true,false],[\"end of\",0,true,false],[\"the end\",0,true,false],[\"the end of\",0,true,false],[\"ended\",0,true,false]],[[7,9]],,8,9],[\"至少 我们\",5,[[\"at least we\",695,true,false],[\"least we\",0,true,false]],[[9,13]],,9,12],[\"不必\",6,[[\"do not have to\",266,true,false],[\"having to\",358,true,false],[\"do not have\",0,true,false],[\"need not\",0,true,false],[\"not have to\",0,true,false]],[[18,20]],,12,16],[\"费心 地\",7,[[\"bother to\",280,true,false],[\"bother\",349,true,false]],[[20,23]],,16,18],[\"曾经\",8,[[\"have\",280,true,false],[\"had\",0,true,false],[\"once\",0,true,false],[\"ever\",0,true,false],[\"used to\",0,true,false]],[[13,15]],,18,19],[\"过\",9,[[\"been\",282,true,false],[\"over\",233,true,false],[\"had\",15,true,false],[\"over the\",0,true,false],[\"too\",0,true,false]],[[17,18]],,19,20],[\"约束\",10,[[\"constrained\",321,true,false],[\"constraints\",365,true,false],[\"the constraints\",8,true,false],[\"constraint\",0,true,false],[\"restraint\",0,true,false]],[[25,27]],,20,21],[\"相聚\",11,[[\"together\",321,true,false],[\"gathered\",153,true,false],[\"meet\",8,true,false],[\"phase\",0,true,false],[\"get together\",0,true,false]],[[15,17]],,21,22],[\"更 不需要\",12,[[\"but does not require\",317,true,false],[\"but do not need\",8,true,false]],[[27,31]],,22,26],[\"言语 的\",13,[[\"verbal\",972,true,false],[\"speech\",0,true,false],[\"of speech\",0,true,false]],[[31,34]],,26,27],[\"承诺\",14,[[\"commitment to\",714,true,false],[\"commitment\",248,true,false],[\"commitments\",9,true,false],[\"promise\",0,true,false],[\"promised\",0,true,false]],[[34,36]],,27,29],[\"彼此\",15,[[\"each other\",608,true,false],[\"to each other\",225,true,false],[\"one another\",126,true,false],[\"to one another\",29,true,false],[\"another\",0,true,false]],[[23,25]],,29,31],[\"只要 我们\",16,[[\"as long as we\",799,true,false],[\"if we\",0,true,false],[\"long as we\",0,true,false],[\"so long as we\",0,true,false],[\"as long as our\",0,true,false]],[[36,40]],,31,35],[\"曾经 拥有\",17,[[\"used to have\",957,true,false],[\"once had\",0,true,false],[\"happened\",0,true,false],[\"once owned\",0,true,false],[\"once you owned\",0,true,false]],[[40,44]],,35,38]],0.99942529,,[[\"zh-CN\"],,[0.99942529]]]";
//		int index1 = googleResult.indexOf("\"");
//		int index2 = googleResult.indexOf("\"", index1 + 1);
//		System.out.println(index1);
//		System.out.println(index2);
//		System.out.println(googleResult.substring(index1+1, index2));
//		
//		
//		
//		int index3 = googleResult.lastIndexOf("\"");
//		int index4 = googleResult.lastIndexOf("\"", index3-1);
//		System.out.println(index3);
//		System.out.println(index4);
//		System.out.println(googleResult.substring(index4 + 1, index3));
		
		try {
			test2();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void test2() throws NoSuchAlgorithmException, KeyManagementException, IOException{
		SSLContext sslcontext = SSLContext.getInstance("SSL");  
        X509TrustManager tm = new X509TrustManager() {
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
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
        	String url2 = "https://www.googleapis.com/language/translate/v2";
        	
            HttpGet httpget = new HttpGet(url2 + "?key=AIzaSyAjapM-2VJHyIhnUHMzwrIWeGZN8qiUmyI&source=en&target=de&q=Hello%20world");
            HttpHost proxy = new HttpHost("127.0.0.1", 1080, "http");
    		RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();
    		httpget.setConfig(config);

            System.out.println("executing request" + httpget.getRequestLine());

            //CloseableHttpResponse response = httpclient.execute(httpget);
            
            try {
            	ResponseHandler<GoogleTranslateResult> responseHandler = new GoogleResultHandler();  
            	GoogleTranslateResult responseBody;  
            	
            	responseBody = httpclient.execute(httpget, responseHandler);  
                /*HttpEntity entity = response.getEntity();
                
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                }*/
                
                
    	  
    	        System.out.println(responseBody);  
                
                //EntityUtils.consume(entity);
            } finally {
                //response.close();
            }
        } finally {
            httpclient.close();
        }
	}
	
	public static void test1(){
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet("http://translate.google.cn/translate_a/single?client=t&sl=zh-CN&tl=en&hl=zh-CN&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&dt=at&ie=UTF-8&oe=UTF-8&source=btn&rom=0&ssel=3&tsel=3&kc=0&tk=179570.301419&q=%E5%B0%BC%E7%8E%9B");
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            } finally {
                response1.close();
            }
        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		
	}

}
