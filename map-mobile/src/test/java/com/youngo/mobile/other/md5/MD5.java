package com.youngo.mobile.other.md5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import sun.misc.BASE64Encoder;

public class MD5 {

	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };
	protected static MessageDigest md = null;
	static{
	   try{
		   md = MessageDigest.getInstance("MD5");
	   }catch(NoSuchAlgorithmException nsaex){
	    System.err.println("error");
	    nsaex.printStackTrace();
	   }
	}
	
	
	public static void main(String[] args) {
		
		System.out.println("result:" + mD5("aaaaaa"));
		
		hex2Bytes1("0b4e7a0e5fe84ad35fb5f95b9ceeac79");
//		Set<String> values=Collections.synchronizedSet(new HashSet<String>(50));
//		CountDownLatch t = new CountDownLatch(1);
//		CountDownLatch t2 = new CountDownLatch(5);
//		for(int i = 0; i < 5; i++){
//			TestThread tt = new TestThread(t,t2, new Integer(i).toString() + "1fsdfasdfasfasdfsadfsadfasdfsadfsdfsdasfdasdfasdfasdfasdfasdfsadfsadfsdff", values);
//			tt.start();
//		}
//		t.countDown();
//		try {
//			t2.await();
//			System.out.println(values.size());
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
	}
	
	public static byte[] hex2Bytes1(String src){  
		byte[] res = new byte[src.length()/2];  
	    char[] chs = src.toCharArray();  
	    int[] b = new int[2];
	    for(int i=0,c=0; i<chs.length; i+=2,c++){              
	        for(int j=0; j<2; j++){
	            if(chs[i+j]>='0' && chs[i+j]<='9'){  
	                b[j] = (chs[i+j]-'0');  
	            }else if(chs[i+j]>='A' && chs[i+j]<='F'){  
	                b[j] = (chs[i+j]-'A'+10);  
	            }else if(chs[i+j]>='a' && chs[i+j]<='f'){  
	                b[j] = (chs[i+j]-'a'+10);  
	            }
	        }
	        b[0] = (b[0]&0x0f)<<4;  
	        b[1] = (b[1]&0x0f);
	        res[c] = (byte) (b[0] | b[1]);
	    }
	    return res;
	}
	
	public static String mD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			//btInput = new byte[]{11 , 78, 122, 14, 95, -24, 74, -45, 95, -75, -7, 91, -100, -18, -84, 121};
			byte b =1;
			long l = 1L;
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			System.out.println(md.length);
			System.out.println("str" + new String(md));
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4& 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String encode(String text, boolean judgeMd){
		StringBuffer buf = new StringBuffer("");   
		md.update(text.getBytes());   
		byte b[] = md.digest();
		
		int i;   
        for (int offset = 0; offset < b.length; offset++) {   
            i = b[offset];   
            if(i<0) i+= 256;   
            if(i<16)   
            buf.append("0");   
            buf.append(Integer.toHexString(i));   
        }   
		System.out.println(b.length);
		if(judgeMd == true){  
			return buf.toString();  
		}else{  
			return buf.toString().substring(8,24);  
		}  
	}
	
	public static final String EncoderPwdByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {  
		// 确定计算方法  
		MessageDigest md5 = MessageDigest.getInstance("MD5");  
		BASE64Encoder base64en = new BASE64Encoder();  
		// 加密后的字符串  
		String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));  
		return newstr;  
	}  
	
	
}

class TestThread extends Thread{
	private CountDownLatch t;
	CountDownLatch t2;
	String  str;
	Set<String> set;
	TestThread(CountDownLatch t,CountDownLatch t2, String str, Set<String> set){
		this.t = t;
		this.t2 = t2;
		this.str = str;
		this.set = set;
	}
	public void run(){
		try {
			t.await();
			
			String s = MD5.mD5(str);
			System.out.println(s);
			set.add(s);
			t2.countDown();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}