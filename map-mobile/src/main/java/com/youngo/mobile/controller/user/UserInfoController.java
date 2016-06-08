package com.youngo.mobile.controller.user;

import com.youngo.core.common.Context;
import com.youngo.core.common.SupportedLanguage;
import com.youngo.core.exception.RestException;
import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.Login;
import com.youngo.core.model.Pager;
import com.youngo.core.model.Result;
import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.user.User;
import com.youngo.core.model.user.UserBrief;
import com.youngo.core.service.msg.ChatService;
import com.youngo.core.upload.ImageUploadService;
import com.youngo.core.upload.VoiceUploadService;
import com.youngo.mobile.controller.friendship.RecommendController;
import com.youngo.mobile.controller.location.GoogleLocationService;
import com.youngo.mobile.mapper.user.GalleryMapper;
import com.youngo.mobile.model.paperPlane.Address;
import com.youngo.mobile.model.user.Gallery;
import com.youngo.mobile.smapper.LanguageSsdbMapper;
import com.youngo.ssdb.core.HashMapOperations;
import com.youngo.ssdb.core.SortedSetOperations;
import com.youngo.ssdb.core.SsdbConstants;
import com.youngo.ssdb.core.entity.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("user/")
public class UserInfoController {
    private static final String nickName_check = "[A-Za-z0-9\\s]*";// 昵称格式验证
    private Pattern nickName_regex = Pattern.compile(nickName_check);
    @Autowired
    UserMapper userMapper;
    @Autowired
    GalleryMapper galleryMapper;

    @Autowired
    @Qualifier("stringHashMapOperations")
    HashMapOperations<String, String, String> hashMapOperation;

    @Autowired
    @Qualifier("stringSortedSetOperations")
    SortedSetOperations<String, String> sortedSetOperation;

    @Autowired
    @Qualifier("googleLocationService")
    GoogleLocationService googleLocationService;

    @Autowired
    private ImageUploadService imageService;
    @Autowired
    private VoiceUploadService voiceService;
    @Autowired
    private ChatService chatService;

    @Autowired
    @Qualifier("languageSsdbMapper")
    LanguageSsdbMapper languageSsdbMapper;

    @Autowired
    private RecommendController recommendControlle;

    /**
     * 用户信息完善
     *
     * @return
     */
    @RequestMapping(value = "completeInfo", method = RequestMethod.POST)
    @Transactional
    @Login
    public Result completeInfo(@RequestBody User user) {
        Result result = new Result();
        String userId = Context.getUserId();
        user.setId(userId);
        validateUserInfo(user);
        try {
            userMapper.updateByUserId(user);
        } catch (DuplicateKeyException e) {
            // 主键冲突，重新修改用户名
            result.setCode(Result.completeInfo_nickName_exist);
            result.setMsg(Result.completeInfo_nickName_exist_msg);
            return result;
        }

        insertWelcomeMessage(userId, user.getLocal());
        return result;
    }

    /**
     * 插入一条欢迎语
     *
     * @param userId 用户ID
     * @param local  客户端系统语言
     */
    private void insertWelcomeMessage(String userId, String local) {
        SupportedLanguage language = getSupportLanguage(local);
        insertAdminWelcome(userId, language);
    }

    private void insertAdminWelcome(String userId, SupportedLanguage language) {
        String message;
        switch (language) {
            case en:
                message = "Welcome to \"Bello\"! If you have any questions or suggestions, please contact me.";
                break;
            default:
                message = "欢迎来到Bello！如果您在使用中遇到任何问题或者有任何建议，请直接联系此官方账号~！";
                break;
        }
        MessageEntity welComeMessage = chatService.createTextMessage(0, Integer.valueOf(userId), message);
        chatService.save(welComeMessage);
    }


    public byte[] intToBytes(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));
        }
        return b;
    }

    private SupportedLanguage getSupportLanguage(String local) {
        try {
            return SupportedLanguage.valueOf(local);
        } catch (Throwable t) {
            return SupportedLanguage.en;
        }
    }

    /**
     * 检查用户名密码是否已存在
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "checkNameExsits", method = RequestMethod.POST)
    public Result checkNameExsits(@RequestBody Map<String, String> map) {
        Result result = new Result();
        String nickName = map.get("nickName");
        if (null == nickName || "".equals(nickName.trim())) {
            result.setCode(Result.empty_param);
            result.setMsg(Result.empty_param_msg);
            return result;
        }
        nickName = nickName.trim();
        Matcher nnMatcher = nickName_regex.matcher(nickName);
        if (nickName.length() > 20 || !nnMatcher.matches()) {
            result.setCode(Result.checkExist_nickName_invalid);
            result.setMsg(Result.checkExist_nickName_invalid_msg);
            return result;
        }

        int n = 0;
        n = userMapper.checkNickNameExsits(nickName);
        if (n > 0) {
            result.setCode(Result.checkExist_nickName_exist);// 用户已存在
            result.setMsg(Result.checkExist_nickName_exist_msg);
        }
        return result;
    }

    /**
     * 个人中心 获取 头像、昵称
     *
     * @return
     */
    @Deprecated
    public Result personalCenter() {
        Result result = new Result();

        return result;
    }

    @Login
    @RequestMapping(value = "uploadImage")
    public Result uploadImage(MultipartHttpServletRequest request) {
        Result result = new Result();
        String userId = Context.getUserId();
        String[] imageurl = imageService.sendToServer(request, "personalPortrait/" + userId);
        List<Gallery> galleries = new ArrayList<Gallery>();
        for (int i = 0; i < imageurl.length; i++) {
            Gallery gallery = new Gallery();
            gallery.setNormalUrl(imageurl[i]);
            gallery.setOriginalUrl(imageurl[i]);
            gallery.setThumbUrl(imageurl[i]);
            galleries.add(gallery);
        }
        result.setData(galleries);
        return result;
    }

    @Login
    @RequestMapping(value = "downloadImage", method = RequestMethod.POST)
    public Result downloadImage(@RequestBody Map<String, String> param) {
        Result result = new Result();
        String userId = Context.getUserId();
        String imageUrl = param.get("imageUrl");
        if (imageUrl == null) {

        }
        String url = imageService.sendToServerDownload(imageUrl, "personalPortrait/" + userId);
        if (null == url) {//TODO
            result.setCode(0);
            result.setMsg("");
            return result;
        }
        Gallery gallery = new Gallery();
        gallery.setNormalUrl(url);
        gallery.setOriginalUrl(url);
        gallery.setThumbUrl(url);
        result.setData(gallery);
        return result;
    }

    @Login
    @RequestMapping(value = "uploadVoice")
    public Result uploadVoice(MultipartHttpServletRequest request) {
        Result result = new Result();
        String userId = Context.getUserId();
        String[] voiceurl = voiceService.sendToServer(request, "personalVoice/" + userId);
        result.setData(voiceurl);
        return result;
    }


    /**
     * 基本个人资料 获取个人资料，包括 头像、昵称、生日、性别、国籍/地区
     *
     * @return
     */
    @Login
    @RequestMapping(value = "getBaseInfo", method = RequestMethod.POST)
    public Result getBaseInfo() {
        Result result = new Result();
        String userId = Context.getUserId();
        User user = userMapper.getById(userId);
        if (user == null) {
            // 用户号已注销
            result.setCode(Result.getBaseInfo_user_disposed);
            result.setMsg(Result.getBaseInfo_user_disposed_msg);
            return result;
        }
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		List<Language> interestLanguages = languageMapper.selectInterestByUserId(Integer.parseInt(userId));
//		List<Language> goodatLanguages = languageMapper.selectGoodatByUserId(Integer.parseInt(userId));

//		user.setInterestedLanguages(interestLanguages);
//		user.setGoodatLanguages(goodatLanguages);
//		map.put("interestedLanguages", user.getInterestedLanguages());
//		map.put("goodatLanguages", user.getGoodatLanguages());
//		map.put("userBrief", user.changeToBrief());
        UserBrief userBrief = user.changeToBrief();
        result.setData(userBrief);
        return result;
    }

    /**
     * 基本个人资料 设置个人资料，包括 头像、昵称、生日、性别、国籍/地区
     *
     * @return
     */
    @Login
    @RequestMapping(value = "updateBaseInfo", method = RequestMethod.POST)
    public Result updateBaseInfo(@RequestBody User user) {
        Result result = new Result();
        String userId = Context.getUserId();
        validateUpdateBaseInfo(user);
        User userBuff = new User();
        userBuff.setId(userId);
        userBuff.setIcon(user.getIcon());
        userBuff.setNickName(user.getNickName());
        userBuff.setBirthday(user.getBirthday());
        userBuff.setCountry(user.getCountry());
        userBuff.setSex(user.getSex());
        try {
            userMapper.updateByUserId(userBuff);
        } catch (DuplicateKeyException e) {
            // 主键冲突，重新修改用户名
            result.setCode(Result.completeInfo_nickName_exist);
            result.setMsg(Result.completeInfo_nickName_exist_msg);
            return result;
        }
        return result;
    }

    @Login
    @RequestMapping(value = "getPushUser", method = RequestMethod.POST)
    public Result getPushUser(HttpServletRequest request) {
        Result result = new Result();
        List<Object> objs = new ArrayList<Object>(3);
        fillPushUser(objs, 1, request);
        recordPushedUser(objs);
        result.setData(objs);
        return result;
    }

    /**
     * 填充待推送用户
     *
     * @param ubs
     * @param pageNum
     * @param request
     */
    @SuppressWarnings("unchecked")
    public void fillPushUser(List<Object> ubs, int pageNum, HttpServletRequest request) {
        String userId = Context.getUserId();
        Pager page = recommendControlle.recommend(pageNum, null, null, null, null, null, request);
        if (null != page) {
            List<Object> datas = page.getList();
            if (null != datas) {
                removePushedUser(datas, userId);
                removeChatedUser(datas, userId);
                for (int i = 0, j = datas.size(); i < j; i++) {
                    if (ubs.size() < 3) {
                        ubs.add(datas.get(i));
                    } else {
                        break;
                    }
                }
                if (ubs.size() < 3) {
                    fillPushUser(ubs, ++pageNum, request);
                }
            }
        }
    }

    /**
     * 移除已推送用户
     *
     * @param datas
     * @param userId
     */
    private void removePushedUser(List<Object> datas, String userId) {
        HashSet<String> keys = new HashSet<String>(10);
        for (int i = 0; i < datas.size(); i++) {
            UserBrief ub = (UserBrief) datas.get(i);
            if (null != ub) {
                keys.add(ub.getId());
            }
        }
        if (keys.size() > 0) {
            //移除已推送用户
            Map<String, Long> pushed = sortedSetOperation.multiGetAsMap(SsdbConstants.userPushedPrefix + userId, keys);
            if (null != pushed && pushed.size() > 0) {
                for (int i = 0, j = datas.size(); i < j; i++) {
                    UserBrief ub = (UserBrief) datas.get(i);
                    if (null != ub) {
                        if (null != pushed.get(ub.getId())) {
                            datas.remove(ub);
                            --i;
                            --j;
                        }
                    }
                }
            }
        }
    }

    /**
     * 移除已有会话信息用户
     *
     * @param datas
     * @param userId
     */
    public void removeChatedUser(List<Object> datas, String userId) {
        HashSet<String> keys = new HashSet<String>(10);
        if (null != datas && !datas.isEmpty()) {
            for (int i = 0; i < datas.size(); i++) {
                UserBrief ub = (UserBrief) datas.get(i);
                if (null != ub) {
                    StringBuilder sb = new StringBuilder(SsdbConstants.Chat.chatSessionPrefix).append(userId);//个人会话  1_2表示当前用户与2的个人会话
                    sb.append(":");
                    sb.append(ub.getId());
                    keys.add(sb.toString());
                }
            }
            //移除已有会话信息用户
//		Map<String, Long> chated = sortedSetOperation.multiGetAsMap(SsdbConstants.Chat.chatSessionListPrefix + userId, keys);
            Map<String, Long> chated = chatService.getUserChatSessionByUserIds(userId, keys);
            if (null != chated && chated.size() > 0) {
                for (int i = 0, j = datas.size(); i < j; i++) {
                    UserBrief ub = (UserBrief) datas.get(i);
                    if (null != ub) {
                        StringBuilder sb = new StringBuilder(SsdbConstants.Chat.chatSessionPrefix).append(userId).append(":");//个人会话  1_2表示当前用户与2的个人会话
                        sb.append(ub.getId());
                        if (null != chated.get(sb.toString())) {
                            datas.remove(ub);
                            --i;
                            --j;
                        }
                    }
                }
            }
        }
    }


    /**
     * 记录已推送用户
     *
     * @param datas
     */
    private void recordPushedUser(List<Object> datas) {
        if (datas.size() > 0) {
            String userId = Context.getUserId();
            HashMap<String, Long> values = new HashMap<String, Long>(3);
            Long score = 0 - System.currentTimeMillis();
            for (int i = 0; i < datas.size(); i++) {
                UserBrief ub = (UserBrief) datas.get(i);
                if (null != ub) {
                    values.put(ub.getId(), score);
                }
            }
            sortedSetOperation.multiSet(SsdbConstants.userPushedPrefix + userId, values);
        }
    }

    /**
     * 查看他人用户详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "getUserInfo", method = RequestMethod.POST)
    public Result getUserInfo(@RequestBody Map<String, String> param) {
        Result result = new Result();
        String userId = param.get("userId");
        String local = param.get("local");
        if (null == userId || "".equals(userId.trim())) {
            result.setCode(Result.empty_param);
            result.setMsg(Result.empty_param_msg);
            return result;
        }
        HashMap<String, Object> data = null;
        User user = userMapper.getById(userId);
        if (user != null) {
            data = new HashMap<String, Object>();
            UserBrief userBrief = user.changeToBrief();
            List<Gallery> galleries = galleryMapper.selectByUserId(Integer.parseInt(userId));
            data.put("portraitList", galleries);
            data.put("userBrief", userBrief);
            if (null != local) {
                if (null != user.getLatitude() && null != user.getLongitude()) {
                    Address address = new Address(user.getCurrentCountry(), user.getCurrentCity());
                    address = googleLocationService.getLocalNameFromEn(address, user.getLatitude(), user.getLongitude(), local);
                    userBrief.setCurrentCountry(address.getCountry());
                    userBrief.setCurrentCity(address.getCity());
                }
            }
        }
        result.setData(data);
        return result;
    }

    /**
     * 获取用户简介（除用户详情和个人设置界面都可以使用用户简介）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "getUserBrief", method = RequestMethod.POST)
    public Result getUserBrief(@RequestBody Map<String, List<String>> param) {
        Result result = new Result();
        List<String> userIds = param.get("userIds");
        if (null != userIds) {
            List<User> users = userMapper.getByIds(userIds);
            if (users != null) {
                List<UserBrief> ubs = new ArrayList<UserBrief>();
                for (int i = 0; i < users.size(); i++) {
                    UserBrief ub = users.get(i).changeToBrief();
                    ubs.add(ub);
                }
                result.setData(ubs);
            }
        }
        return result;
    }

    private void validateUserInfo(User user) {
        // System.out.println(user.toString());
        if (null == user || StringUtils.isEmpty(user.getId()) || StringUtils.isEmpty(user.getIcon())
                || StringUtils.isEmpty(user.getNickName()) || null == user.getBirthday()
                || StringUtils.isEmpty(user.getSex()) || StringUtils.isEmpty(user.getCountry())
                ) {
            throw new RestException(Result.empty_param, Result.empty_param_msg);
        }
        String userId = user.getId();
        String nickName = user.getNickName().trim();
        user.setNickName(nickName);

        Matcher nnMatcher = nickName_regex.matcher(nickName);
        if (nickName.length() > 20 || !nnMatcher.matches()) {
            throw new RestException(Result.completeInfo_nickName_invalid, Result.completeInfo_nickName_invalid_msg);
        }

    }

    private void validateUpdateBaseInfo(User user) {
        if (user == null || (StringUtils.isEmpty(user.getIcon()) && StringUtils.isEmpty(user.getNickName())
                && null == user.getBirthday() && StringUtils.isEmpty(user.getSex())
                && StringUtils.isEmpty(user.getCountry()))) {
            throw new RestException(Result.empty_param, Result.empty_param_msg);
        }

        if (null != user.getNickName()) {
            String nickName = user.getNickName().trim();

            Matcher nnMatcher = nickName_regex.matcher(nickName);
            if (nickName.length() > 20 || !nnMatcher.matches()) {
                throw new RestException(Result.updateBaseInfo_nickName_invalid, Result.updateBaseInfo_nickName_invalid_msg);
            }
            user.setNickName(nickName);
        } else if (null != user.getSex()) {
            if (!"man".equals(user.getSex()) && !"woman".equals(user.getSex())) {
                user.setSex("unknow");
            }
        }
    }

}
