package com.youngo.mobile.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.nutz.ssdb4j.SSDBs;
import org.nutz.ssdb4j.spi.SSDB;

import com.youngo.ssdb.core.DefaultHashMapOperations;
import com.youngo.ssdb.core.DefaultListOperations;
import com.youngo.ssdb.core.DefaultSortedSetOperations;
import com.youngo.ssdb.core.HashMapOperations;
import com.youngo.ssdb.core.ListOperations;
import com.youngo.ssdb.core.SortedSetOperations;
import com.youngo.ssdb.core.entity.Tuple;

public class LanguageInit {
	
	SSDB ssdb = null;
	public HashMapOperations<String, String, String> operation = null;
	public ListOperations<String, String> loperation = null;
	public SortedSetOperations<String, String> sOperation = null;
	
	
	private Map<String, String> languages_en;
	private Map<String, String> languages_zh;
	
	public LanguageInit(){
		languages_en = new HashMap<String, String>();
		languages_zh = new HashMap<String, String>();
//		ssdb = SSDBs.pool("47.89.28.120", 8888, 30000, null);
		ssdb = SSDBs.pool("47.89.49.135", 8888, 30000, null);
		operation = new DefaultHashMapOperations<String, String,String>(ssdb, String.class, String.class, String.class);
		loperation = new DefaultListOperations<>(ssdb, String.class, String.class);
		sOperation = new DefaultSortedSetOperations<>(ssdb, String.class, String.class);
		
		
	}
	
	public void init(){
		
		operation.clear("languages_en");
		operation.clear("languages_zh");
		
		languages_en.put("sq", "Albanian");
		languages_en.put("ar", "Arabic");
		languages_en.put("az", "Azerbaijani");
		languages_en.put("ga", "Irish");
		languages_en.put("et", "Estonian");
		languages_en.put("eu", "Basque");
		languages_en.put("be", "Belarusian");
		languages_en.put("bg", "Bulgarian");
		languages_en.put("is", "Icelandic");
		languages_en.put("pl", "Polish");
		languages_en.put("bs", "Bosnian");
		languages_en.put("fa", "Persian");
		languages_en.put("af", "Afrikaans");
		languages_en.put("da", "Danish");
		languages_en.put("de", "German");
		languages_en.put("ru", "Russian");
		languages_en.put("fr", "French");
		languages_en.put("tl", "Filipino");
		languages_en.put("fi", "Finnish");
		languages_en.put("km", "Khmer");
		languages_en.put("ka", "Georgian");
		languages_en.put("gu", "Gujarati");
		languages_en.put("kk", "Kazakh");
		languages_en.put("ht", "Haitian Creole");
		languages_en.put("ko", "Korean");
		languages_en.put("ha", "Hausa");
		languages_en.put("nl", "Dutch");
		languages_en.put("gl", "Galician");
		languages_en.put("ca", "Catalan");
		languages_en.put("cs", "Czech");
		languages_en.put("kn", "Kannada");
		languages_en.put("hr", "Croatian");
		languages_en.put("la", "Latin");
		languages_en.put("lv", "Latvian");
		languages_en.put("lo", "Lao");
		languages_en.put("lt", "Lithuanian");
		languages_en.put("ro", "Romanian");
		languages_en.put("mg", "Malagasy");
		languages_en.put("mt", "Maltese");
		languages_en.put("mr", "Marathi");
		languages_en.put("ml", "Malayalam");
		languages_en.put("ms", "Malay");
		languages_en.put("mk", "Macedonian");
		languages_en.put("mi", "Maori");
		languages_en.put("mn", "Mongolian");
		languages_en.put("bn", "Bengali");
		languages_en.put("my", "Myanmar (Burmese)");
		languages_en.put("hmn", "Hmong");//TODO
		languages_en.put("zu", "Zulu");
		languages_en.put("ne", "Nepali");
		languages_en.put("no", "Norwegian");
		languages_en.put("ma", "Punjabi");
		languages_en.put("pt", "Portuguese");
		languages_en.put("ny", "Chichewa");
		languages_en.put("ja", "Japanese");
		languages_en.put("sv", "Swedish");
		languages_en.put("sr", "Serbian");
		languages_en.put("st", "Sesotho");
		languages_en.put("si", "Sinhala");
		languages_en.put("eo", "Esperanto");
		languages_en.put("sk", "Slovak");
		languages_en.put("sl", "Slovenian");
		languages_en.put("sw", "Swahili");
		languages_en.put("ceb", "Cebuano");
		languages_en.put("so", "Somali");
		languages_en.put("tg", "Tajik");
		languages_en.put("te", "Telugu");
		languages_en.put("ta", "Tamil");
		languages_en.put("th", "Thai");
		languages_en.put("tr", "Turkish");
		languages_en.put("cy", "Welsh");
		languages_en.put("ur", "Urdu");
		languages_en.put("uk", "Ukrainian");
		languages_en.put("uz", "Uzbek");
		languages_en.put("iw", "Hebrew");
		languages_en.put("el", "Greek");
		languages_en.put("es", "Spanish");
		languages_en.put("hu", "Hungarian");
		languages_en.put("hy", "Armenian");
		languages_en.put("ig", "Igbo");
		languages_en.put("it", "Italian");
		languages_en.put("yi", "Yiddish");
		languages_en.put("hi", "Hindi");
		languages_en.put("su", "Sudanese");
		languages_en.put("id", "Indonesian");
		languages_en.put("jw", "Javanese");
		languages_en.put("en", "English");
		languages_en.put("yo", "Yoruba");
		languages_en.put("vi", "Vietnamese");
		languages_en.put("zh-CN", "Chinese Simplified");
		languages_en.put("zh-TW", "Chinese Traditional");
		
		
		
		
		languages_zh.put("sq", "阿尔巴尼亚语");
		languages_zh.put("ar", "阿拉伯语");
		languages_zh.put("az", "阿塞拜疆语");
		languages_zh.put("ga", "爱尔兰语");
		languages_zh.put("et", "爱沙尼亚语");
		languages_zh.put("eu", "巴斯克语");
		languages_zh.put("be", "白俄罗斯语");
		languages_zh.put("bg", "保加利亚语");
		languages_zh.put("is", "冰岛语");
		languages_zh.put("pl", "波兰语");
		languages_zh.put("bs", "波斯尼亚语");
		languages_zh.put("fa", "波斯语");
		languages_zh.put("af", "布尔语（南非荷兰语）");
		languages_zh.put("da", "丹麦语");
		languages_zh.put("de", "德语");
		languages_zh.put("ru", "俄语");
		languages_zh.put("fr", "法语");
		languages_zh.put("tl", "菲律宾语");
		languages_zh.put("fi", "芬兰语");
		languages_zh.put("km", "高棉语");
		languages_zh.put("ka", "格鲁吉亚语");
		languages_zh.put("gu", "古吉拉特语");
		languages_zh.put("kk", "哈萨克语");
		languages_zh.put("ht", "海地克里奥尔语");
		languages_zh.put("ko", "韩语");
		languages_zh.put("ha", "豪萨语");
		languages_zh.put("nl", "荷兰语");
		languages_zh.put("gl", "加利西亚语");
		languages_zh.put("ca", "加泰罗尼亚语");
		languages_zh.put("cs", "捷克语");
		languages_zh.put("kn", "卡纳达语");
		languages_zh.put("hr", "克罗地亚语");
		languages_zh.put("la", "拉丁语");
		languages_zh.put("lv", "拉托维亚语");
		languages_zh.put("lo", "老挝语");
		languages_zh.put("lt", "立陶宛语");
		languages_zh.put("ro", "罗马尼亚语");
		languages_zh.put("mg", "马尔加什语");
		languages_zh.put("mt", "马耳他语");
		languages_zh.put("mr", "马拉地语");
		languages_zh.put("ml", "马拉雅拉姆语");
		languages_zh.put("ms", "马来语");
		languages_zh.put("mk", "马其顿语");
		languages_zh.put("mi", "毛利语");
		languages_zh.put("mn", "蒙古语");
		languages_zh.put("bn", "孟加拉语");
		languages_zh.put("my", "缅甸语)");
		languages_zh.put("hmn", "苗语");
		languages_zh.put("zu", "南非祖鲁语");
		languages_zh.put("ne", "尼泊尔语");
		languages_zh.put("no", "挪威语");
		languages_zh.put("ma", "旁遮普语");
		languages_zh.put("pt", "葡萄牙语");
		languages_zh.put("ny", "齐切瓦语");
		languages_zh.put("ja", "日语");
		languages_zh.put("sv", "瑞典语");
		languages_zh.put("sr", "塞尔维亚语");
		languages_zh.put("st", "塞索托语");
		languages_zh.put("si", "僧伽罗语");
		languages_zh.put("eo", "世界语");
		languages_zh.put("sk", "斯洛伐克语");
		languages_zh.put("sl", "斯洛文尼亚语");
		languages_zh.put("sw", "斯瓦西里语");
		languages_zh.put("ceb", "宿务语");
		languages_zh.put("so", "索马里语");
		languages_zh.put("tg", "塔吉克语");
		languages_zh.put("te", "泰卢固语");
		languages_zh.put("ta", "泰米尔语");
		languages_zh.put("th", "泰语");
		languages_zh.put("tr", "土耳其语");
		languages_zh.put("cy", "威尔士语");
		languages_zh.put("ur", "乌尔都语");
		languages_zh.put("uk", "乌克兰语");
		languages_zh.put("uz", "乌兹别克语");
		languages_zh.put("iw", "希伯来语");
		languages_zh.put("el", "希腊语");
		languages_zh.put("es", "西班牙语");
		languages_zh.put("hu", "匈牙利语");
		languages_zh.put("hy", "亚美尼亚语");
		languages_zh.put("ig", "伊博语");
		languages_zh.put("it", "意大利语");
		languages_zh.put("yi", "意地绪语");
		languages_zh.put("hi", "印地语");
		languages_zh.put("su", "印尼巽他语");
		languages_zh.put("id", "印尼语");
		languages_zh.put("jw", "印尼爪哇语");
		languages_zh.put("en", "英语");
		languages_zh.put("yo", "约鲁巴语");
		languages_zh.put("vi", "越南语");
		languages_zh.put("zh-CN", "中文(简体)");
		languages_zh.put("zh-TW", "中文(繁体)");
		
		boolean b = operation.multiSet("languages_en", languages_en);
		b = operation.multiSet("languages_zh", languages_zh);
		
		
		System.out.println(b);
		
	}
	
	public void initOfen(){
		Map<String, Long> often = new HashMap<String, Long>();
		often.put("zh-CN", 21000L);
		often.put("en", 20000L);
		often.put("ja", 19000L);
		often.put("ko", 18000L);
		often.put("fr", 17000L);
		often.put("de", 16000L);
		often.put("es", 15000L);
		often.put("ru", 14000L);
		often.put("it", 13000L);
		often.put("zh-TW", 10000L);
		
		sOperation.multiSet("languages_often", often);
	}
	

	public static void main(String[] args){
		
		LanguageInit init = new LanguageInit();
		init.init();
		init.sOperation.clear("languages_often");
		
		init.initOfen();
		Map<String, String> lans =  init.operation.getAllByName("languages_en");
		
		System.out.println(lans);
		
		Map<String, String> lans2 = init.operation.getAllByName("languages_zh");
		
		System.out.println(lans2);
		
		List<Tuple<String, Long>> lans3 = init.sOperation.getByrangeReverse("languages_often", 0, 10);
		System.out.println(lans3);
	}
	
	
	
}
