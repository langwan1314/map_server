package com.youngo.mobile.smapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.youngo.ssdb.core.HashMapOperations;
import com.youngo.ssdb.core.SortedSetOperations;
import com.youngo.ssdb.core.SsdbConstants;
import com.youngo.ssdb.core.entity.Tuple;

@Repository("languageSsdbMapper")
public class LanguageSsdbMapper implements SsdbConstants.Language{
	@Autowired
	@Qualifier("stringHashMapOperations")
	HashMapOperations<String, String, String> hashMapOperation;
	
	@Autowired
	@Qualifier("stringSortedSetOperations")
	SortedSetOperations<String, String> sortedSetOperation;
	
	public Map<String, String> getAllLanguage(String local){
		Map<String, String> allMap = null;
		if ("zh-CN".equals(local)) {
			allMap = hashMapOperation.getAllByName("languages_zh");
		} else if ("en".equals(local)) {
			allMap = hashMapOperation.getAllByName("languages_en");
		} else {
			allMap = hashMapOperation.getAllByName("languages_en");
		}
		return allMap;
	}
	
	public List<Tuple<String, Long>> getOfenLanguage(){
		return sortedSetOperation.getByrangeReverse("languages_often", 0, 10);
	}
	
	public Map<String, String> getLanguageByKey(String local, Set<String> keys){
		String name = null;
		if ("zh-CN".equals(local)) {
			name = "languages_zh";
		} else if ("en".equals(local)) {
			name = "languages_en";
		} else {
			name = "languages_en";
		}
		return hashMapOperation.multiGetByKeysAsMap(name, keys);
		
	}
}
