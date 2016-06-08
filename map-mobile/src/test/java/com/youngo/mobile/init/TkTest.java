package com.youngo.mobile.init;

import java.util.ArrayList;
import java.util.List;

public class TkTest {
	//http://translate.google.cn/translate_tts?ie=UTF-8&q=%E4%BD%A0%E6%98%AF%E4%B8%80%E4%B8%AA%E5%A4%A7%E5%82%BB%E9%80%BC&tl=zh-CN&tk=255510.380772&client=t&prev=input&ttsspeed=0.24
	
	
	
	private static String Wb = "+-a^+6", Vb = "+-3^+b+-f";
	private static char t = 'a'; 
	private static char Ub = '+';
	
	public static void main(String[] args) {
		String a = "别管以后将如何结束至少我们曾经相聚过不必费心地彼此约束更不需要言语的承诺只要我们曾经拥有";
		String xx = getTk(a);
		String tk = "&tk=" + xx;
		System.out.println(tk);
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
		    a = b.charAt(c) == Ub ? a + xx & 4294967295L : a ^ xx;
		}
		return a;
	}
}
