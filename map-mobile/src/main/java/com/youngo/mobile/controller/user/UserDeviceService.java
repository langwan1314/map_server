package com.youngo.mobile.controller.user;

import com.youngo.core.model.user.UserDevice;
import com.youngo.core.mapper.user.UserDeviceMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserDeviceService
{
    private final Logger logger = LoggerFactory.getLogger(UserDeviceService.class);
    @Autowired
    private UserDeviceMapper userPushDeviceMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized int addUser(String imei, String registration_id, String userId, String device_source, String screenSize, String language)
    {
        registration_id = registration_id.trim();
        userPushDeviceMapper.disableLoginByUser(userId, registration_id);

        UserDevice device = getDeviceInfo(userId);
        if (device == null)
        {
            UserDevice userDevice = new UserDevice();
            userDevice.setRegistrationId(registration_id);
            userDevice.setUserId(userId);
            userDevice.setDeviceSource(device_source);
            userDevice.setImei(imei);
            userDevice.setScreenSize(screenSize);
            userDevice.setLanguage(language);
            userPushDeviceMapper.insert(userDevice);
        } else if (!imei.equals(device.getImei()) || "0".equals(device.getLoginStatus()) || !userId.equals(device.getUserId()) || !registration_id.equals(device.getRegistrationId()))
        {
            UserDevice userDevice = new UserDevice();
            userDevice.setRegistrationId(registration_id);
            userDevice.setUserId(userId);
            userDevice.setDeviceSource(device_source);
            userDevice.setImei(imei);
            userDevice.setId(device.getId());
            userDevice.setLoginStatus("1");
            userDevice.setScreenSize(screenSize);
            userDevice.setLanguage(language);
            userPushDeviceMapper.updateById(userDevice);
        }
        return 1;
    }

    private UserDevice getDeviceInfo(String userId)
    {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        return userPushDeviceMapper.getDetailInfo(param);
    }

    public synchronized void addRegistrationId(String registrationId, String screenSize, String language)
    {
        Map<String, Object> params = new HashMap<>(1);
        params.put("registrationId", registrationId);
        UserDevice detailInfo = userPushDeviceMapper.getDetailInfo(params);
        if (detailInfo == null)
        {
            UserDevice userDevice = new UserDevice();
            userDevice.setRegistrationId(registrationId);
            userDevice.setScreenSize(screenSize);
            userDevice.setLanguage(language);
            userPushDeviceMapper.insert(userDevice);
        }
    }

}
