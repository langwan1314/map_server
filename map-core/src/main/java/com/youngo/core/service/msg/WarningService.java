package com.youngo.core.service.msg;

import com.youngo.core.model.protobuf.Header;
import com.youngo.core.model.protobuf.IMBaseDefine;
import com.youngo.core.model.protobuf.IMLogin;
import com.youngo.core.model.protobuf.SocketResult;
import org.springframework.stereotype.Service;

/**
 * Created by 浮沉 on 2016/5/11.
 */
@Service
public class WarningService
{

    /**
     * 通知用户被禁用
     */
    public SocketResult buildWarningResult(String userId)
    {
        IMLogin.IMWarningUser warningUser = IMLogin.IMWarningUser.newBuilder().setUserId(Integer.valueOf(userId)).
                setWarningReason(IMBaseDefine.WarningReasonType.WARNING_REASON_PROSECUTOR).build();
        Header header = new Header((short) IMBaseDefine.ServiceID.SID_LOGIN_VALUE, (short) IMBaseDefine.LoginCmdID.CID_LOGIN_WARNING_USER_VALUE);
        SocketResult result = new SocketResult();
        result.setHeader(header);
        result.setBody(warningUser.toByteArray());
        return result;
    }

}
