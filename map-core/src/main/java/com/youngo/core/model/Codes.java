package com.youngo.core.model;

import org.springframework.http.HttpStatus;

public interface Codes {
	
	int success_code = HttpStatus.OK.value();
    String success_msg = "success";
    
    int empty_param = -1;//不允许为空的参数遇到空值
    String empty_param_msg = "param can not be null";
    
    int param_error = -2;
    String param_error_msg = "非法的输入";
    
    int regist_name_invalid = -10101;
    String regist_name_invalid_msg = "用户名格式错误";
    
    int regist_password_invalid = -10102;
    String regist_password_invalid_msg = "密码格式错误";
    
    int regist_name_exist = -10103;
    String regist_name_exist_msg = "邮箱已注册";
    
    int nameAuth_name_notExist = -10201;
    String nameAuth_name_notExist_msg = "邮箱未注册";
    
    int nameAuth_password_invalid = -10202;
    String nameAuth_password_invalid_msg = "密码错误";
    
    int nameAuth_distroyToken_failed = -10203;
    String nameAuth_distroyToken_failed_msg = "当前帐号已登陆，踢出已登陆设备失败";
    
    int nameAuth_disabled = -10204;
    String nameAuth_disabled_msg = "该账号已被管理员禁用";
    
    int auth_another_request = -10205;
    String auth_another_request_msg = "请勿重复发送登陆请求，10秒后再次尝试登陆";
    
    int tokenAuth_token_notExists = -10301;
    String tokenAuth_token_notExists_msg = "无效的token";
    
    int tokenAuth_user_deleted = -10302;
    String tokenAuth_user_deleted_msg = "当前用户已注销";
    
    int tokenAuth_disabled = -10303;
    String tokenAuth_disabled_msg = "该账号已被管理员禁用";
    
    int forgetPassword_username_notExists = -10401;
    String forgetPassword_username_notExists_msg = "邮箱未注册";
    
    int forgetPassword_sentMain_failed = -10402;
    String forgetPassword_sentMain_failed_msg = "发送密码重置邮件失败。请重新发送忘记密码请求，或者联系管理员。";
    
    int forgetPassword_create_failed = -10403;
    String forgetPassword_create_failed_msg = "生成临时密码失败";
    
    int changePassword_password_invalid = -10501;
    String changePassword_password_invalid_msg = "密码格式错误";
    
    int changePassword_password_error = -10502;
    String changePassword_password_error_msg = "密码错误";
    
    int logout_distroyToken_failed = -10601;
    String logout_distroyToken_failed_msg = "用户登出失败";
    
    int completeInfo_nickName_exist = -20101;
    String completeInfo_nickName_exist_msg = "昵称已存在";
    
    int completeInfo_nickName_invalid = -20102;
    String completeInfo_nickName_invalid_msg = "昵称格式错误";
    
    int completeInfo_interest_invalid = -20103;
    String completeInfo_interest_invalid_msg = "感兴趣的语言错误";
    
    int completeInfo_goodAt_invalid = -20104;
    String completeInfo_goodAt_invalid_msg = "擅长的语言错误";
    
    int checkExist_nickName_invalid = -20201;
    String checkExist_nickName_invalid_msg = "昵称格式错误";
    
    int checkExist_nickName_exist = -20202;
    String checkExist_nickName_exist_msg = "昵称已存在";
    
    int getSelfIntro_user_distoryed = -20401;
    String getSelfIntro_user_distoryed_msg = "当前用户已注销";
    
    int updateUseLan_goodAtlan_invalid = -20701;
    String updateUseLan_goodAtlan_invalid_msg = "擅长的语言格式错误";
    
    int updateUseLan_Interestlan_invalid = -20702;
    String updateUseLan_Interestlan_invalid_msg = "感兴趣的语言格式错误";
    
    int getBaseInfo_user_disposed = -20801;
    String getBaseInfo_user_disposed_msg = "当前用户已注销";
    
    int updateBaseInfo_nickName_invalid = -20901;
    String updateBaseInfo_nickName_invalid_msg = "昵称格式错误";
    
    int session_expired = 406;//session失效，需要使用token重新登录
    String session_expired_msg = "session expired";
    
    int ssdbException_code = -5;
    String ssdbException_msg = "服务器连接缓存异常";
    
    int server_error = -99;
    String server_error_msg = "server error";
    
    int trans_language_notSupport = -30101;
    String trans_language_notSupport_msg = "不支持当前语言翻译";
    
    int tts_language_notSupport = -30201;
    String tts_language_notSupport_msg = "当前段落无法朗读";
    
    int paperPlane_max_send = -40101;
    String paperPlane_max_send_msg = "已发送了允许发送数量的纸飞机";
    
    int paperPlane_user_notExist = -40102;
    String paperPlane_user_notExist_msg = "获取用户信息出错";
    
    int paperPlane_list_noMore = -40201;
    String paperPlane_list_noMore_msg = "没有更多飞机了";
    
    int paperPlane_inactivity = -40301;
    String paperPlane_inactivity_msg = "飞机已失效";
    
    int paperPlaneGet_notExist = -40302;
    String paperPlaneGet_notExist_msg = "飞机找不到了";
    
    int paperPlane_randomGet_failed = -40401;
    String paperPlane_randomGet_failed_msg = "随机获取飞机失败";
    
    int paperPlane_randomGet_noMore = -40401;
    String paperPlane_randomGet_noMore_msg = "随机获取飞机失败";
    
    int paperPlane_delete_authFailed = -40601;
    String paperPlane_delete_authFailed_msg = "您不能删除不属于您的飞机";
    
    
}
