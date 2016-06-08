package com.youngo.core.smapper;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.ssdb.core.SsdbConstants;
import com.youngo.ssdb.core.StringValueOperations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Random;
import java.util.UUID;

import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by 浮沉 on 2016/4/8.
 * 身份认证相关，包括passport、token、密码等SSDB数据的存取
 */
@Repository("authMapper")
public class AuthMapper implements SsdbConstants.Authentication
{
    private static StringValueOperations valueOperations;

    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        valueOperations = new StringValueOperations(ssdb);
    }
    
    public String getPassportUserId(String passport)
    {
//        StringBuilder passportKey = new StringBuilder(SsdbConstants.Authentication.SSDB_PASSPORT_PREFIX).append(passport);
    	String passportKey = formatMessage(SSDB_PASSPORT_PREFIX, passport);
        return valueOperations.get(passportKey);
    }
    
    public String getTokenUserId(String token){
//    	StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_TOKEN_PREFIX).append(token);
    	String tokenKey = formatMessage(SSDB_TOKEN_PREFIX, token);
		String userId = valueOperations.get(tokenKey);
		return userId;
    }
    
    /**
	 * 删除旧passport失败未做处理，时效到达后，将自动删除
	 * @param userId
	 */
	public boolean invalidateOldPassport(String userId){
//		StringBuilder userKey = new StringBuilder(userId).append(SsdbConstants.Authentication.SSDB_USER_PASSPORT_SUFFIX);
		String userKey = formatMessage(SSDB_USER_PASSPORT_SUFFIX, userId);
		String oldPassport = valueOperations.get(userKey);
		if(null != oldPassport){
//			StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_PASSPORT_PREFIX).append(oldPassport);
			String passportKey = formatMessage(SSDB_PASSPORT_PREFIX, oldPassport);
			valueOperations.delete(passportKey);
		}
		return true;
	}
    
    /**
	 * 获取新的passport
	 * 同时将用户的passport指向新值
	 * @return
	 */
	public String createNewPassport(String userId){
		UUID uuid = UUID.randomUUID();
		String passport = uuid.toString(); 
//		StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_PASSPORT_PREFIX).append(passport);
		String passwordKey = formatMessage(SSDB_PASSPORT_PREFIX, passport);
		boolean b = valueOperations.setIfAbsent(passwordKey, userId);
		if(b){
			valueOperations.expire(passwordKey, CommonStaticConstant.PASSPORT_TIME_TO_LIVE);//设置失效时间
//			StringBuilder userKey = new StringBuilder(userId).append(SsdbConstants.Authentication.SSDB_USER_PASSPORT_SUFFIX);
			String userKey = formatMessage(SSDB_USER_PASSPORT_SUFFIX, userId);
			valueOperations.setWithTimeToLive(userKey, passport, CommonStaticConstant.PASSPORT_TIME_TO_LIVE);
			return passport;
		}else{
			return createNewPassport(userId);
		}
	}
	
	/**
	 * 创建新的token
	 * @param userId
	 * @return
	 */
	public String createToken(String userId){
		Random random = new Random();
		
		StringBuilder s = new StringBuilder(userId).append(random.nextInt(1000));
		String token = mD5_32Encode(s.toString());
//		StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_TOKEN_PREFIX).append(token);
		String tokenKey = formatMessage(SSDB_TOKEN_PREFIX, token);
		boolean b = valueOperations.setIfAbsent(tokenKey, userId);
		if(b){
//			StringBuilder userKey = new StringBuilder(userId).append(SsdbConstants.Authentication.SSDB_USER_TOKEN_SUFFIX);
			String userKey = formatMessage(SSDB_USER_TOKEN_SUFFIX, userId);
			valueOperations.set(userKey, token);
			return token;
		}else{
			return createToken(userId);
		}
	}
	
	/**
	 * 删除token失败则抛出异常，提醒用户重新操作。
	 * @param userId
	 */
	public boolean invalidateOldToken(String userId){
//		StringBuilder userKey = new StringBuilder(userId).append(SsdbConstants.Authentication.SSDB_USER_TOKEN_SUFFIX);
		String userKey = formatMessage(SSDB_USER_TOKEN_SUFFIX, userId);
		String oldToken = valueOperations.get(userKey);
		if(null != oldToken){
//			StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_TOKEN_PREFIX).append(oldToken);
			String tokenKey = formatMessage(SSDB_TOKEN_PREFIX, oldToken);
			Boolean b = valueOperations.delete(tokenKey);
			if(!b){
				return false;
			}
		}
		return true;
	}
	
	public boolean setBuffPassword(String userId, String buffPasspord){
//		StringBuilder userKey = new StringBuilder(userName).append(SsdbConstants.Authentication.SSDB_USER_BUFFPASSWORD_SUFFFIX);
		String userKey = formatMessage(SSDB_USER_BUFFPASSWORD_SUFFFIX, userId);
		return valueOperations.set(userKey, buffPasspord);
	}
	
	public String getBuffPassword(String userId){
//		StringBuilder userKey = new StringBuilder(userName).append(SsdbConstants.Authentication.SSDB_USER_BUFFPASSWORD_SUFFFIX);
		String userKey = formatMessage(SSDB_USER_BUFFPASSWORD_SUFFFIX, userId);
		String buffPassword = valueOperations.get(userKey);
		return buffPassword;
	}
	
	public boolean getLoginLock(String userId, int tryTime){
		long lockTime = System.currentTimeMillis();
		String userKey = formatMessage(LOGIN_LOCK, userId);
		String value = lockTime + "";
		boolean success = valueOperations.setIfAbsent(userKey, value);
		if(success){
			return true;
		}
		String oldValue = valueOperations.get(userKey);
		if(null != oldValue && !"".equals(oldValue)){
			long oldTime = Long.parseLong(oldValue);
			if(System.currentTimeMillis() - oldTime > CommonStaticConstant.LOGIN_LOCK_TIME){
				String currentOldValue = valueOperations.getAndSet(userKey, System.currentTimeMillis() + "");
				if(currentOldValue == oldValue){
					return true;
				}else{
					return false;
				}
			}
		}else{
			if( --tryTime > 0){
				return getLoginLock(userId, tryTime);
			}
		}
		return false;
	}
	
	public void releaseLoginLock(String userId){
		String userKey = formatMessage(LOGIN_LOCK, userId);
		valueOperations.delete(userKey);
	}
	
	public static String mD5_32Encode(String s) {
		byte[] btInput = s.getBytes();
		MessageDigest mdInst;
		try {
			mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			return bytes2String(md);
		} catch (NoSuchAlgorithmException e) {
			// TODO
			e.printStackTrace();
			return null;
		}
	}
	
	public static String bytes2String(byte[] source){
		int j = source.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = source[i];
			str[k++] = hexDigits[byte0 >>> 4& 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
		
	}
	
	private String formatMessage(String formatMessage, String... args){
		MessageFormat format = new MessageFormat(formatMessage);
		return format.format(args);
	}
	
	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };
	
}
