package com.youngo.mobile.controller.location;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.user.User;
import com.youngo.core.model.user.UserBrief;
import com.youngo.mobile.model.paperPlane.Address;
import com.youngo.mobile.model.paperPlane.Address.LocationSupportedLanguage;
import com.youngo.ssdb.core.HashMapOperations;
import com.youngo.ssdb.core.SsdbConstants;
import com.youngo.ssdb.core.entity.SimpleTuple;
import com.youngo.ssdb.core.entity.Tuple;
import org.springframework.util.StringUtils;

@Service("googleLocationService")
public class GoogleLocationService implements LocationService{
	final static String URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng={0},{1}&language={2}";
	
	private SSLContext sslcontext;  
	private X509TrustManager tm;
	private SSLConnectionSocketFactory sslsf;
    private CloseableHttpClient httpclient;
    
    @Autowired
	@Qualifier("stringHashMapOperations")
	private HashMapOperations<String, String, String> hashMapOperations;
    
    @Autowired
    private UserMapper userMap;
    
    ExecutorService executorService = Executors.newSingleThreadExecutor();
	
    public GoogleLocationService(){
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
    
	public Address reverseGeoCoding(double latitude , double longitude, String local){
		if(local == null || "".equals(local.trim())){
			local = "en";
		}
		boolean valid = true;
		LocationSupportedLanguage language = null;
		try{
			language = LocationSupportedLanguage.valueOf(local);
		}catch(Throwable t){
			local = local.replace("-", "");
			try{
				language = LocationSupportedLanguage.valueOf(local);
			}catch(Throwable t1){
				valid = false;
			}
		}
		if(!valid){
			language = LocationSupportedLanguage.en;
		}
		java.text.DecimalFormat df =new java.text.DecimalFormat("0.0000000"); 
		
		String url = MessageFormat.format(URL, df.format(latitude), df.format(longitude), language.getValue(), "");
		HttpGet httpget = new HttpGet(url);
//		HttpHost proxy = new HttpHost("127.0.0.1", 1080, "http");
//		RequestConfig config = RequestConfig.custom()
//                .setProxy(proxy)
//                .build();
//		httpget.setConfig(config);
		ResponseHandler<Address> responseHandler = new GoogleLocationHandler();  
		Address adress = null;  
		try {
			adress = httpclient.execute(httpget, responseHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return adress;
	}
	
	/**
	 * 获取指定城市的相应的本地名称，优先从本地缓存中获取，本地缓存中没有则根据经纬度从google获取
	 * @param standard
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public Address getLocalNameFromEn(Address standard,double latitude, double longitude, String targetLocal){
		if(null != targetLocal && !"en".equals(targetLocal)){
			LocationSupportedLanguage language =null;
			try{
				language = LocationSupportedLanguage.valueOf(targetLocal);
			}catch(Throwable t){
				targetLocal = targetLocal.replace("-", "");
				try{
					language = LocationSupportedLanguage.valueOf(targetLocal);
				}catch(Throwable t1){
					return standard;
				}
			}
			//有英文名的优先从缓存中获取本地城市名称。
			if(null != standard){
				String country = standard.getCountry();
				String city = standard.getCity();
				Address cachedAddress = getLocalNameFromCache(country, city, language);
				if(null != cachedAddress){
					if((StringUtils.hasText(city) && StringUtils.hasText(cachedAddress.getCity())) || !StringUtils.hasText(city)){
						//能够获取到城市名，或者城市名称本来就为空的情况 视为已经正确获取到了缓存。
						return cachedAddress;
					}
				}
			}
			//没有英文名称的或者从本地缓存中获取失败的，使用经纬度获取城市名称。 用经纬度获取失败的，返回原名称。
			if(0 != longitude && 0 != longitude){
				Address address = getLocalNameFromGoogle(latitude, longitude, language);
				if(null != address){
					return address;
				}
			}
		}
		return standard;
	}
	
	/**
	 * 获取用户列表的当前城市的名称
	 * @param users
	 * @param local
	 */
	public void getLocalName(List<UserBrief> users, String local){
		if(!StringUtils.isEmpty(local) && !"en".equals(local.trim())){
			LocationSupportedLanguage language = null;
			try{
				language = LocationSupportedLanguage.valueOf(local);
			}catch(Throwable t){
				local = local.replace("-", "");
				try{
					language = LocationSupportedLanguage.valueOf(local);
				}catch(Throwable t1){
					return;
				}
			}
			if(null != users && users.size() > 0){
				List<String> keys = new ArrayList<String>();
				for(int i = 0, j = users.size(); i < j; i++){
					UserBrief ub = users.get(i);
					if(null != ub){
						String city = ub.getCurrentCity();
						String country = ub.getCurrentCountry();
						StringBuilder citySb = new StringBuilder();
						if(null != country && !country.isEmpty()){
							keys.add(country);
							citySb.append(country).append("-");
						}
						if(null != city && !city.isEmpty()){
							citySb.append(city);
							keys.add(citySb.toString());
						}
					}
				}
				if(keys.size() > 0){
					Map<String, String> result = hashMapOperations.multiGetByKeysAsMap(SsdbConstants.PaperPlaneKey.locationNameLanguagePrefix + language.getValue(), keys);
					if(null != result){
						List<String> userIdToCache =new ArrayList<String>();
						for(int i = 0, j = users.size(); i < j; i++){
							UserBrief ub = users.get(i);
							if(null != ub){
								String city = ub.getCurrentCity();
								String country = ub.getCurrentCountry();
								StringBuilder citySb = new StringBuilder();
								boolean isCached = true;
								if(null != country && !country.isEmpty()){
									String localCoutry = result.get(country);
									citySb.append(country).append("-");
									if(null != localCoutry){
										ub.setCurrentCountry(localCoutry);
									}else{
										isCached = false;
									}
								}
								if(null != city && !city.isEmpty()){
									citySb.append(city);
									String localCity = result.get(citySb.toString());
									if(null != localCity){
										ub.setCurrentCity(localCity);;
									}else{
										isCached = false;
									}
								}
								if(!isCached){
									// 异步获取城市名称
									userIdToCache.add(ub.getId());
								}
							}
						}
						if(userIdToCache.size() > 0){
							executorService.submit(new GoogleAddressCompleteTask(userIdToCache, language));
						}
					}
				}
			}
		}
	}
	
	private class GoogleAddressCompleteTask implements Runnable{
		private LocationSupportedLanguage language;
		private List<String> userIds;
		
		private GoogleAddressCompleteTask(List<String> userIds, LocationSupportedLanguage language){
			this.language = language;
			this.userIds = userIds;
		}
		@Override
		public void run() {
			if(null != userIds && userIds.size() > 0){
				List<User> users = userMap.getByIds(userIds);
				if(null != users && users.size() > 0){
					for(int i = 0, j = users.size(); i < j; i++){
						User user = users.get(i);
						if(null != user){
							Address address = getLocalNameFromCache(user.getCurrentCountry(), user.getCurrentCity(), language);
							if(null != address){
								if((StringUtils.hasText(user.getCurrentCity()) && StringUtils.hasText(address.getCity())) || !StringUtils.hasText(user.getCurrentCity())){
									//能够获取到城市名，或者城市名称本来就为空的情况 视为已经正确获取到了缓存。不再从google获取城市信息
									continue;
								}
							}
							if(null != user.getLatitude() && null != user.getLongitude()){
								getLocalNameFromGoogle(user.getLatitude(), user.getLongitude(), language);
							}
							
						}
					}
				}
				
			}
		}
	}
	
	/**
	 * 获取缓存中的城市名称信息
	 * 当仅获取到一个名称的时候需要调用处进行处理。
	 * @param country
	 * @param cityName
	 * @return
	 */
	private Address getLocalNameFromCache(String country, String cityName, LocationSupportedLanguage language){
		List<String> keys = new ArrayList<String>(2);
		StringBuilder citySb = new StringBuilder();
		if(StringUtils.hasText(country)){
			keys.add(country);
			citySb.append(country).append("-");
		}
		if(StringUtils.hasText(cityName)){
			citySb.append(cityName);
			keys.add(citySb.toString());
		}
		Address address = null;
		if(keys.size() > 0){
			Map<String, String> names = hashMapOperations.multiGetByKeysAsMap(SsdbConstants.PaperPlaneKey.locationNameLanguagePrefix + language.getValue(), keys);
			if(null != names && names.size() > 0){
				address = new Address();
				address.setCountry(names.get(country));
				address.setCity(names.get(citySb.toString()));
			}
		}
		return address;
	}
	
	private Address getLocalNameFromGoogle(double latitude, double longitude, LocationSupportedLanguage languae){
		Address localAddress = reverseGeoCoding(latitude, longitude, languae.name());
		if(null != localAddress){
			Address en = reverseGeoCoding(latitude, longitude, "en");
			if(null != en){
				Set<Tuple<String, String>> maps = new HashSet<Tuple<String, String>>();
				Tuple<String, String> country = new SimpleTuple<String, String>(en.getCountry(), localAddress.getCountry());
				//TODO city 与country有一个为空的情况。
				StringBuilder sb = new StringBuilder();
				sb.append(en.getCountry()).append('-').append(en.getCity());
				Tuple<String, String> city = new SimpleTuple<String, String>(sb.toString(), localAddress.getCity());
				maps.add(city);
				maps.add(country);
				hashMapOperations.multiSet(SsdbConstants.PaperPlaneKey.locationNameLanguagePrefix + languae.getValue(), maps);
			}
		}
		return localAddress;
	}
}
