package com.youngo.mobile.controller.translate;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.youngo.core.service.msg.SessionConfigureService;
import com.youngo.core.smapper.SessionConfigureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.youngo.core.common.MURLEncoder;
import com.youngo.core.exception.RestException;
import com.youngo.core.model.Login;
import com.youngo.core.model.Result;
import com.youngo.core.model.msg.SessionConfigure;
import com.youngo.core.service.msg.ChatService;
import com.youngo.mobile.model.google.Translation;

@RestController
public class TranslateController {
	final static String TTS_URL = "http://translate.google.cn/translate_tts?client=t&ie=UTF-8&tl={0}&q={1}&tk={2}&textlen={3}";
	@Autowired
	GoogleFreeTranslateService service;
	@Autowired
	private SessionConfigureService sessionService;
	
	/**
	 * 文字翻译
	 * @param param
	 * @return
	 * @since 1.0.0 
	 */
	@Login
	@RequestMapping(value="translate", method=RequestMethod.POST)
	public Result translate(@RequestBody Map<String, String> param){
		String source = param.get("source");
		String target = param.get("target");
		String sl = param.get("sl");
		Result result = new Result();
		Translation trans = service.translate(source, target, sl);
		result.setData(trans);
		return result;
	}
	
	@Login
	@RequestMapping(value="getTranslateLanguage", method=RequestMethod.GET)
	public Result getTranslateLanguage(String fromUser, String toUser){
		Result result = new Result();
		HashMap<String, String> data = null;
		SessionConfigure config = sessionService.getSessionConfigure(fromUser, toUser);
		if(null != config){
			data = new HashMap<String, String>(2);
			data.put("received", config.getRtranslate());
			data.put("send", config.getStranslate());
		}
		result.setData(data);
		return result;
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	@Login
	@RequestMapping(value="tts", method=RequestMethod.POST)
	public Result tts(@RequestBody Map<String, String> param){
		String source = param.get("source");
		Result result = new Result();
		if(null == source || "".equals(source.trim()) ){
			result.setCode(Result.empty_param);
			result.setMsg(Result.empty_param_msg);
			return result;
		}
		Translation trans = null;
		try{
			trans = service.translate(source, "en");
		}catch(RestException e){
		}
		if(null == trans || trans.getDetectedSourceLanguage() == null){
			result.setCode(Result.tts_language_notSupport);
			result.setMsg(Result.tts_language_notSupport_msg);
			return result;
		}
		String q = "";
		try {
			q = MURLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		String tk = service.getTk2(source);
		String data = MessageFormat.format(TTS_URL, trans.getDetectedSourceLanguage(), q, tk, source.length());
		result.setData(data);
		return result;
	}
	
}
