package com.youngo.mobile.controller.report;

import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.ForceLogin;
import com.youngo.core.model.Login;
import com.youngo.core.model.msg.SessionEntity;
import com.youngo.core.model.msg.UnreadEntity;
import com.youngo.core.model.user.Position;
import com.youngo.core.model.user.User;
import com.youngo.core.model.user.UserBrief;
import com.youngo.core.model.version.VersionProxy;
import com.youngo.mobile.controller.chat.ChatListController;
import com.youngo.mobile.controller.chat.UnReadMsgController;
import com.youngo.mobile.controller.friendship.FriendShipController;
import com.youngo.mobile.controller.location.GoogleLocationService;
import com.youngo.mobile.controller.user.UserDeviceService;
import com.youngo.mobile.controller.version.VersionController;
import com.youngo.mobile.model.friendship.FriendInfo;
import com.youngo.mobile.model.paperPlane.Address;
import com.youngo.mobile.model.report.ReportValue;
import com.youngo.mobile.utils.Geohash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fuchen <br>
 **/
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/report")
public class ReportController
{
    protected static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    @Autowired
    private VersionController versionController;
    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendShipController friendShipController;
    @Autowired
    private ChatListController chatListController;
    @Autowired
    private UnReadMsgController unReadMsgController;
    @Autowired
    @Qualifier("googleLocationService")
    GoogleLocationService googleLocationService;

    /**
     * 启动APP时调动
     * 上报APP基础信息<br>
     * clientVersion : 客户端版本<br>
     * clientType: 客户端类型android，ios<br>
     * screenSize:屏幕尺寸，如400,700<br>
     * registrationID:消息推送要用到的设备唯一标识<br>
     * language:客户端语言<br>
     * position: x,y
     */
    @Login(forceLogin = ForceLogin.no)
    @RequestMapping(method = RequestMethod.POST, value = "activity")
    public ReportValue reportActivity(@RequestBody Map<String, String> params, HttpServletRequest request)
    {
        logger.debug("reportOnStart : param :" + params.toString());
        ReportValue value = new ReportValue();
        //版本更新检查
        VersionProxy version = reportVersion(params);
        value.setVersion(version);

        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        if(!StringUtils.isEmpty(userId))
        {
            updateUser(userId,params);
//            List<FriendInfo> friends = friendShipController.list(request);//联系人列表
//            List<SessionEntity> sessionEntityList = chatListController.list(request);//会话列表
//            Collection<UnreadEntity> unreadEntities = unReadMsgController.list(request);
//            value.setFriends(friends);
//            value.setSessions(sessionEntityList);
//            value.setUnreads(unreadEntities);
        }
        reportDeviceInfo(params, userId);
        return value;
    }

    /**
     * 退出APP(非退出登录)或者将APP置于后台时调用
     */
    @Login(forceLogin = ForceLogin.no)
    @RequestMapping(method = RequestMethod.POST, value = "inActivity")
    public String reportInactivity(HttpServletRequest request)
    {
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        User user = new User();
        user.setId(userId);
        user.setOnlineStatus("offline");//下线
        user.setLastaccesstime(new Date());
        userMapper.updateByUserId(user);
        return "success";
    }

    private VersionProxy reportVersion(Map<String, String> params)
    {
        String version = params.get("clientVersion");
        String type = params.get("clientType");
        return versionController.getLastVersion(version, type);
    }

    /**
     * @param params
     * @param userId 上报设备信息
     *               暂时废弃，还没想清楚
     */
    private void reportDeviceInfo(Map<String, String> params, String userId)
    {
        logger.debug("******begin call ReportController.reportRegistrationID********");
        String clientType = params.get("clientType");
        String imei = params.get("imei");
        String screenSize = params.get("screenSize");
        String language = params.get("language");
        String registrationID = params.get("registrationID");
        if("android".equals(clientType) && StringUtils.isEmpty(registrationID))
        {
            registrationID = imei;
        }
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(registrationID) && !StringUtils.isEmpty(clientType))
        {
            userDeviceService.addUser(imei, registrationID, userId, clientType, screenSize, language);
        }
    }

    /**
     * 更新用户信息
     * @param userId 用户ID
     */
    public void updateUser(String userId , Map<String, String> params)
    {
        User user = new User();
        String position = params.get("position");
        if(!StringUtils.isEmpty(position))
        {
            String[] split = StringUtils.split(position, ",");
            if(split!=null && split.length==2)
            {
            	if(!CommonStaticConstant.inValidateLocation.equals(split[0]) && !CommonStaticConstant.inValidateLocation.equals(split[1])){
            		Double lat = Double.valueOf(split[0]);
            		Double lon = Double.valueOf(split[1]);
            		Position p = new Position();
            		p.setLatitude(lat);
            		
            		p.setLongitude(lon);
            		user.setPosition(p);
            		try{
	            		Address a = googleLocationService.reverseGeoCoding(lat, lon, "en");
	            		if(null != a){
	            			user.setCurrentCountry(a.getCountry());
	            			user.setCurrentCity(a.getCity());
	            		}
            		}catch(Throwable t){
            			
            		}
            		Geohash geohash = new Geohash();
            		String encode = geohash.encode(lat, lon);
            		user.setGeohash(encode);
            	}
            }
        }
        user.setId(userId);
        user.setOnlineStatus("online");
        user.setLastaccesstime(new Date());
        userMapper.updateByUserId(user);
    }

}
