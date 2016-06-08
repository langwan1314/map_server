package com.youngo.mobile.controller.auth;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.common.Context;
import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.ForceLogin;
import com.youngo.core.model.Login;
import com.youngo.core.model.Result;
import com.youngo.core.model.user.User;
import com.youngo.core.model.user.UserBrief;
import com.youngo.core.smapper.AuthMapper;
import com.youngo.core.model.user.User.UserStatus;
import com.youngo.core.mapper.user.UserDeviceMapper;
import com.youngo.mobile.mapper.user.SocializationMapper;
import com.youngo.mobile.model.auth.Auth;
import com.youngo.mobile.model.auth.ChagePassword;
import com.youngo.mobile.model.auth.LoginInfo;
import com.youngo.mobile.model.auth.SocializationAuth;
import com.youngo.mobile.model.user.Socialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("login/")
public class LoginController {

    private static final String mail_check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";//邮箱验证正则表达式
    private static final String buff_password_check = "[a-z]{1}[0-9]{1}[a-z]{1}[0-9]{1}[a-z0-9]{6}";//随机密码验证正则表达式
    //	private static final String password_check = "[A-Za-z0-9]+";//密码格式验证
    public static Pattern mail_regex = Pattern.compile(mail_check);
    static Pattern buff_pasword_regex = Pattern.compile(buff_password_check);
    //	static Pattern pasword_regex = Pattern.compile(password_check);
    private static byte[] emptyByte = new byte[0];
    private static int randomPasswordLength = 10;//随机密码长度
    private static int randomSaltLength = 12;//随机盐值长度
    //private static String mainContentFormat = "";

    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserDeviceMapper deviceMapper;

    @Autowired
    SocializationMapper socializationMapper;

    @Autowired
    AuthMapper authMapper;


    /**
     * 必定返回passport与token。新用户不存在已有pasport与token。
     * <p>
     * 邮件注册
     *
     * @return
     */
    @RequestMapping(value = "regist", method = RequestMethod.POST)
    @Transactional
    public Result regist(@RequestBody Auth auth, HttpServletResponse response) {
        Result result = new Result();
        if (null == auth || null == auth.getName() || "".equals(auth.getName().trim())
                || null == auth.getPassword() || "".equals(auth.getPassword().trim())) {
            result.setCode(Result.empty_param);
            result.setMsg(Result.empty_param_msg);
            return result;
        }
        String userName = auth.getName().trim();
        String password = auth.getPassword().trim();
        Matcher matcher = mail_regex.matcher(userName);
        boolean nameValidation = true;//matcher.matches();
        boolean passwordValidation = checkPasswordFormat(password);
        if (!nameValidation || userName.length() > 60) {
            result.setCode(Result.regist_name_invalid);
            result.setMsg(Result.regist_name_invalid_msg);
            return result;
        }
        if (!passwordValidation) {
            result.setCode(Result.regist_password_invalid);
            result.setMsg(Result.regist_password_invalid_msg);
            return result;
        }

        User user = userMapper.getUserByUserName(userName);
        if (user != null) {//TODO
            result.setCode(Result.regist_name_exist);//用户已存在
            result.setMsg(Result.regist_name_exist_msg);
            return result;
        }

        user = new User();
        user.setUserName(userName);
        user.setEmail(userName);
        user.setMobile(auth.getMobile());
        String salt = getRandomString(randomSaltLength);
        String storgePassword = shaEncode(password, salt);
        user.setPassword(storgePassword);
        user.setSalt(salt);
        user.setOnlineStatus("online");
        Date time = new Date();
        user.setCreatetime(time);
        user.setLastupdatetime(time);
        user.setLastaccesstime(time);
        try {
            userMapper.register(user);
        } catch (DuplicateKeyException e) {
            // 主键冲突，重新修改用户名
            result.setCode(Result.regist_name_exist);
            result.setMsg(Result.regist_name_exist_msg);
            return result;
        }

        String passport = authMapper.createNewPassport(user.getId());
        String token = authMapper.createToken(user.getId());

        response.addHeader(CommonStaticConstant.PASSPORT, passport);
        LoginInfo info = getLoginInfoFromUser(null, passport, token);
        result.setData(info);

        return result;
    }

    /**
     * 用户名密码登陆
     * <p>
     * 必定返回passport与token，并且判断是否已存在passport，存在则使之失效
     *
     * @param auth
     * @return
     */
    @RequestMapping(value = "nameAuth", method = RequestMethod.POST)
    public Result login(@RequestBody Auth auth, HttpServletResponse response) {
        Result result = new Result();
        if (null == auth || null == auth.getName() || "".equals(auth.getName().trim())
                || null == auth.getPassword() || "".equals(auth.getPassword().trim())) {
            //参数异常
            result.setCode(Result.empty_param);
            result.setMsg(Result.empty_param_msg);
            return result;
        }

        String userName = auth.getName().trim();
        String password = auth.getPassword().trim();
        User user = userMapper.getUserByUserName(userName);

        if (user == null) {
            //用户名密码错误
            result.setCode(Result.nameAuth_name_notExist);
            result.setMsg(Result.nameAuth_name_notExist_msg);
            return result;
        } else if (!shaEncode(password, user.getSalt()).equals(user.getPassword())) {
            Matcher m = buff_pasword_regex.matcher(password);
            if (m.matches() && password.length() == randomPasswordLength) {
                //可能为忘记密码后系统生成的随机密码。可至ssdb查询是否有未失效的随机密码
//				StringBuilder key = new StringBuilder(userName).append(SsdbConstants.Authentication.SSDB_USER_BUFFPASSWORD_SUFFFIX);
//				String buffPassword = valueOperations.get(key.toString());
                String buffPassword = authMapper.getBuffPassword(user.getId());
                if (!password.equals(buffPassword)) {
                    result.setCode(Result.nameAuth_password_invalid);
                    result.setMsg(Result.nameAuth_password_invalid_msg);
                    return result;
                } else {
                    user.setPassword(buffPassword);
                }
            } else {
                //用户名密码错误
                result.setCode(Result.nameAuth_password_invalid);
                result.setMsg(Result.nameAuth_password_invalid_msg);
                return result;
            }
        }

        if (UserStatus.disable.name().equals(user.getStatus())) {
            result.setCode(Result.nameAuth_disabled);
            result.setMsg(Result.nameAuth_disabled_msg);
            return result;
        }
//		long lockTime = System.currentTimeMillis();
        String passport = null;
        String token = null;
        String userId = user.getId();
        boolean lock = authMapper.getLoginLock(userId, 3);
        if (lock) {
            try {
                authMapper.invalidateOldPassport(userId);
                if (!authMapper.invalidateOldToken(userId)) {
                    result.setCode(Result.nameAuth_distroyToken_failed);
                    result.setMsg(Result.nameAuth_distroyToken_failed_msg);
                    return result;
                }
                passport = authMapper.createNewPassport(userId);
                token = authMapper.createToken(userId);
            } finally {
                authMapper.releaseLoginLock(userId);
            }
        } else {
            result.setCode(Result.auth_another_request);
            result.setMsg(Result.auth_another_request_msg);
            return result;
        }

        user.setLastaccesstime(new Date());
        user.setOnlineStatus("online");
        userMapper.updateByUserId(user);


        response.addHeader(CommonStaticConstant.PASSPORT, passport);
        LoginInfo info = getLoginInfoFromUser(user, passport, token);
        result.setData(info);
        return result;
    }

    /**
     * token自动登陆 返回passport，判断是否已存在passport，存在使之失效
     *
     * @param auth
     * @return
     */
    @RequestMapping(value = "tokenAuth", method = RequestMethod.POST)
    public Result tokenAuth(@RequestBody Auth auth, HttpServletResponse response) {
        Result result = new Result();
        if (null == auth || null == auth.getToken() || "".equals(auth.getToken().trim())) {
            //参数异常
            result.setCode(Result.empty_param);
            result.setMsg(Result.empty_param_msg);
            return result;
        }
        String token = auth.getToken().trim();
        if (auth.getToken().length() != 32) {
            result.setCode(Result.tokenAuth_token_notExists);
            result.setMsg(Result.tokenAuth_token_notExists_msg);//不正确的token
            return result;
        }
//		StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_TOKEN_PREFIX).append(token);
//		String userId = valueOperations.get(sb.toString());
        String userId = authMapper.getTokenUserId(token);
        if (null == userId) {
            result.setCode(Result.tokenAuth_token_notExists);
            result.setMsg(Result.tokenAuth_token_notExists_msg);//
            return result;
        }

        User user = userMapper.getById(userId);

        if (user == null) {
            //该用户已注销
            result.setCode(Result.tokenAuth_user_deleted);
            result.setMsg(Result.tokenAuth_user_deleted_msg);
            return result;
        }

        if (UserStatus.disable.name().equals(user.getStatus())) {
            result.setCode(Result.tokenAuth_disabled);
            result.setMsg(Result.tokenAuth_disabled_msg);
            return result;
        }

        String passport = null;
        boolean lock = authMapper.getLoginLock(userId, 3);
        if (lock) {
            try {
                authMapper.invalidateOldPassport(user.getId());//踢出已登录用户
                passport = authMapper.createNewPassport(user.getId());
            } finally {
                authMapper.releaseLoginLock(userId);
            }
        } else {
            result.setCode(Result.auth_another_request);
            result.setMsg(Result.auth_another_request_msg);
            return result;
        }

        user.setLastaccesstime(new Date());
        user.setOnlineStatus("online");
        userMapper.updateByUserId(user);//更新用户上次登录时间失败，不做处理。
        response.addHeader(CommonStaticConstant.PASSPORT, passport);
        LoginInfo info = getLoginInfoFromUser(user, passport, token);
        result.setData(info);
        return result;
    }

    @RequestMapping(value = "ocializationAuth", method = RequestMethod.POST)
    public Result ocializationAuth(@RequestBody SocializationAuth auth, HttpServletResponse response) {
        Result result = new Result();
        if (null == auth || null == auth.getUserId() || 0 == auth.getType()) {
            result.setCode(Result.empty_param);
            result.setMsg(Result.empty_param_msg);
            return result;
        }
        if (auth.getType() != 1 && auth.getType() != 2) {
            result.setCode(Result.param_error);
            result.setMsg(Result.param_error_msg);
            return result;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("socialValue", auth.getUserId());
        param.put("socialType", auth.getType());
        Socialization socialization = socializationMapper.selectBySocialAndType(param);
        User user = null;
        if (null == socialization) {
            user = new User();
            StringBuilder sb = new StringBuilder();
            sb.append(auth.getUserId()).append("_").append(auth.getType());
            user.setUserName(sb.toString());
            user.setOnlineStatus("online");
            user.setPassword("");
            Date time = new Date();
            user.setCreatetime(time);
            user.setLastupdatetime(time);
            user.setLastaccesstime(time);
            try {
                userMapper.register(user);
            } catch (DuplicateKeyException e) {
                //TODO 主键冲突，重新修改用户名
                result.setCode(Result.regist_name_exist);
                result.setMsg(Result.regist_name_exist_msg);
                return result;
            }
            socialization = new Socialization();
            socialization.setCreatetime(time);
            socialization.setLastupdatetime(time);
            socialization.setSocialType(auth.getType());
            socialization.setSocialValue(auth.getUserId());
            socialization.setUserId(user.getId());
            socializationMapper.insert(socialization);
        } else {
            user = userMapper.getById(socialization.getUserId());
            Date time = new Date();
            socialization.setLastupdatetime(time);
            socializationMapper.updateByPrimaryKey(socialization);
            user.setLastaccesstime(time);
            user.setOnlineStatus("online");
            userMapper.updateByUserId(user);//更新用户上次登录时间失败，不做处理。
        }

        String passport = authMapper.createNewPassport(user.getId());
        String token = authMapper.createToken(user.getId());

        response.addHeader(CommonStaticConstant.PASSPORT, passport);
        LoginInfo info = getLoginInfoFromUser(user, passport, token);
        result.setData(info);
        return result;

    }

    /**
     * 登出接口
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @Login(forceLogin = ForceLogin.no)
    public Result logout() {
        Result result = new Result();
        String userId = Context.getUserId();
        if (userId == null) {
            return result;
        }
        authMapper.invalidateOldPassport(userId);
        if (!authMapper.invalidateOldToken(userId)) {
            result.setCode(Result.logout_distroyToken_failed);
            result.setMsg(Result.logout_distroyToken_failed_msg);
            return result;
        }
        User user = new User();
        user.setId(userId);
        user.setLastaccesstime(new Date());
        user.setOnlineStatus("offline");
        userMapper.updateByUserId(user);//更新用户登录状态。
        return result;
    }

    /**
     * 忘记密码
     * 发送新密码至邮箱
     *
     * @param auth
     * @return
     */
    @RequestMapping(value = "forgetPassword", method = RequestMethod.POST)
    public Result forgetPassword(@RequestBody final Auth auth) {
        Result result = new Result();
        if (null == auth || null == auth.getName() || "".equals(auth.getName().trim())) {
            result.setCode(Result.empty_param);
            result.setMsg(Result.empty_param_msg);
            return result;
        }
        String userName = auth.getName().trim();
        User user = userMapper.getUserByUserName(userName);
        if (user == null) {
            result.setCode(Result.forgetPassword_username_notExists);
            result.setMsg(Result.forgetPassword_username_notExists_msg);
            return result;
        }

        String newPassword = getRandomString(randomPasswordLength);
//		StringBuilder key = new StringBuilder(userName).append(SsdbConstants.Authentication.SSDB_USER_BUFFPASSWORD_SUFFFIX);
        boolean b = false;
        int i = 0;
        do {
//			b = valueOperations.set(key.toString(), newPassword);
            b = authMapper.setBuffPassword(user.getId(), newPassword);
        } while (!b && ++i < 3);//临时密码存储进ssdb，失败3次后不在尝试存储，请求失败

        if (b) {
            try {
                cachedThreadPool.execute(new Runnable() {
                    public void run() {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = formatter.format(new Date());

                        StringBuilder content = new StringBuilder();
                        content.append("您好，您在").append(formattedDate).append("进行了密码重置。\r\n")
                                .append("新密码为：").append(newPassword).append("，该密码有效时间为5分钟。在您使用该密码登录以前旧密码依旧有效。");

                        String title = "密码重置请求";

                        Mail m = new Mail("smtp.exmail.qq.com", "official-website@e-youngo.com", "ygjy2015", "465");
                        List<String> to = new ArrayList<String>();
                        List<String> cc = new ArrayList<String>();
                        to.add(auth.getName());//现阶段用户名即邮箱名，后续可能修改
                        m.send("official-website@e-youngo.com", to, cc, title, content.toString(), new Date(), "");
                    }
                });

            } catch (Exception e) {
                result.setCode(Result.forgetPassword_sentMain_failed);//发送邮件失败，请稍后重试
                result.setMsg(Result.forgetPassword_sentMain_failed_msg);//
            }
        } else {
            result.setCode(Result.forgetPassword_create_failed);//生成新密码出错，请稍后重试
            result.setMsg(Result.forgetPassword_create_failed_msg);//
        }
        return result;
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @Login
    public Result changePassword(@RequestBody ChagePassword password) {
        Result result = new Result();
        if (null == password
                || null == password.getOldPassword() || "".equals(password.getOldPassword())
                || null == password.getNewPassword() || "".equals(password.getNewPassword())) {
            result.setCode(Result.empty_param);
            result.setMsg(Result.empty_param_msg);
            return result;
        }

        boolean passwordValidation = checkPasswordFormat(password.getNewPassword());
        if (!passwordValidation) {
            //密码格式错误
            result.setCode(Result.changePassword_password_invalid);
            result.setMsg(Result.changePassword_password_invalid_msg);
            return result;
        }

        String userId = Context.getUserId();

        User user = userMapper.getById(userId);
        if (null != user && shaEncode(password.getOldPassword(), user.getSalt()).equals(user.getPassword())) {
            user.setPassword(shaEncode(password.getNewPassword(), user.getSalt()));
        } else {
            result.setCode(Result.changePassword_password_error);//用户名密码错误
            result.setMsg(Result.changePassword_password_error_msg);
        }
        //注销passport与token，？
        //修改表记录。
        userMapper.updateByUserId(user);
        return result;
    }

    public static byte[] hex2Bytes1(String src) {
        byte[] res = new byte[src.length() / 2];
        char[] chs = src.toCharArray();
        int[] b = new int[2];
        for (int i = 0, c = 0; i < chs.length; i += 2, c++) {
            for (int j = 0; j < 2; j++) {
                if (chs[i + j] >= '0' && chs[i + j] <= '9') {
                    b[j] = (chs[i + j] - '0');
                } else if (chs[i + j] >= 'A' && chs[i + j] <= 'F') {
                    b[j] = (chs[i + j] - 'A' + 10);
                } else if (chs[i + j] >= 'a' && chs[i + j] <= 'f') {
                    b[j] = (chs[i + j] - 'a' + 10);
                }
            }
            b[0] = (b[0] & 0x0f) << 4;
            b[1] = (b[1] & 0x0f);
            res[c] = (byte) (b[0] | b[1]);
        }
        return res;
    }

    public String encode(String userName, String imei) {
        String s = new StringBuilder("userName").append(imei).toString();
        return mD5_32Encode(s);
    }

    public static String mD5_64Encode(String s) {
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 4];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 & 0xf];
                str[k++] = hexDigits[byte0 >>> 2 & 0xf];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 >>> 6 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
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

    public static String shaEncode(String source, String salt) {
        byte[] sources = null;
        if (source == null) {
            sources = emptyByte;
        } else {
            sources = source.getBytes();
        }
        byte[] salts = null;
        if (null == salt) {
            salts = emptyByte;
        } else {
            salts = salt.getBytes();
        }
        byte[] hashPassword = new byte[1 + sources.length + salts.length];
        System.arraycopy(salts, 0, hashPassword, 1, salts.length);
        System.arraycopy(sources, 0, hashPassword, salts.length + 1, sources.length);
        MessageDigest mdInst;
        try {
            mdInst = MessageDigest.getInstance("SHA1");
            mdInst.update(hashPassword);
            byte[] sha = mdInst.digest();
            return bytes2String(sha);
        } catch (NoSuchAlgorithmException e) {
            // TODO
            e.printStackTrace();
            return null;
        }
    }

    public static String bytes2String(byte[] source) {
        int j = source.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = source[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);

    }

    /**
     * 创建新的token
     * @param userId
     * @return
     */
//	public String createToken(String userId){
//		Random random = new Random();
//		
//		StringBuilder s = new StringBuilder(userId).append(random.nextInt(1000));
//		String token = mD5_32Encode(s.toString());
//		StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_TOKEN_PREFIX).append(token);
//		
//		boolean b = valueOperations.setIfAbsent(sb.toString(), userId);
//		if(b){
//			StringBuilder userKey = new StringBuilder(userId).append(SsdbConstants.Authentication.SSDB_USER_TOKEN_SUFFIX);
//			valueOperations.set(userKey.toString(), token);
//			return token;
//		}else{
//			return createToken(userId);
//		}
//	}

    /**
     * 删除token失败则抛出异常，提醒用户重新操作。
     * @param userId
     */
//	private boolean invalidateOldToken(String userId){
//		StringBuilder userKey = new StringBuilder(userId).append(SsdbConstants.Authentication.SSDB_USER_TOKEN_SUFFIX);
//		String oldToken = valueOperations.get(userKey.toString());
//		if(null != oldToken){
//			StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_TOKEN_PREFIX).append(oldToken);
//			Boolean b = valueOperations.delete(sb.toString());
//			if(!b){
//				return false;
//			}
//		}
//		return true;
//	}

    /**
     * 获取新的passport
     * 同时将用户的passport指向新值
     * @return
     */
//	public String createNewPassport(String userId){
//		UUID uuid = UUID.randomUUID();
//		String passport = uuid.toString(); 
//		StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_PASSPORT_PREFIX).append(passport);
//		boolean b = valueOperations.setIfAbsent(sb.toString(), userId);
//		if(b){
//			valueOperations.expire(sb.toString(), CommonStaticConstant.PASSPORT_TIME_TO_LIVE);//设置失效时间
//			
//			StringBuilder userKey = new StringBuilder(userId).append(SsdbConstants.Authentication.SSDB_USER_PASSPORT_SUFFIX);
//			valueOperations.setWithTimeToLive(userKey.toString(), passport, CommonStaticConstant.PASSPORT_TIME_TO_LIVE);
//			return passport;
//		}else{
//			return createNewPassport(userId);
//		}
//	}

    /**
     * 删除旧passport失败未做处理，时效到达后，将自动删除
     * @param userId
     */
//	private boolean invalidateOldPassport(String userId){
//		StringBuilder userKey = new StringBuilder(userId).append(SsdbConstants.Authentication.SSDB_USER_PASSPORT_SUFFIX);
//		String oldPassport = valueOperations.get(userKey.toString());
//		if(null != oldPassport){
//			StringBuilder sb = new StringBuilder(SsdbConstants.Authentication.SSDB_PASSPORT_PREFIX).append(oldPassport);
//			valueOperations.delete(sb.toString());
//		}
//		return true;
//	}

    /**
     * @param user     用户信息，需要调用源判空
     * @param passport 用户的passport
     * @param token    用户对应的token
     * @return
     */
    private LoginInfo getLoginInfoFromUser(User user, String passport, String token) {
        LoginInfo info = new LoginInfo();
        if (null != user) {
            UserBrief ub = user.changeToBrief();
            info.setUserBrief(ub);
        }
        info.setPassport(passport);
        info.setToken(token);
        return info;
    }

    /**
     * 验证密码是否符合规则
     * 必须由 6-20位字母和数字组合组成
     *
     * @param password
     * @return
     */
    private boolean checkPasswordFormat(String password) {
        if (null == password) {
            return false;
        }
        if (password.length() < 6 || password.length() > 20) {
            return false;
        }
//		Matcher pMactch = pasword_regex.matcher(password);
        return true;
    }

    /**
     * 用来生成随机盐值或者密码
     * 密码10位
     * 盐值4位
     *
     * @return
     */
    private static String getRandomString(int length) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int n = 0;
            if (i < 4 && i % 2 == 0) {
                n = r.nextInt(26) + 10;

            } else if (i < 4 && i % 2 == 1) {
                n = r.nextInt(10);
            } else {
                n = r.nextInt(36);
            }
            sb.append(passwordArray[n]);
        }
        return sb.toString();
    }

    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static char passwordArray[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
            , 'a', 'b', 'c', 'd', 'e', 'f', 'g'
            , 'h', 'i', 'j', 'k', 'l', 'm', 'n'
            , 'o', 'p', 'q', 'r', 's', 't'
            , 'u', 'v', 'w', 'x', 'y', 'z'};
}
