/**
 * @Title: ExceptionUtil.java
 * @Package com.yjh.core.common
 * @Description: 异常处理工具类
 * @author yiyan
 * @date 2014-12-9 下午2:43:32
 * @version
 */
package com.youngo.core.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 类名称：ExceptionUtil
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-12-9 下午2:43:32
 * 修改人：yiyan
 * 修改时间：2014-12-9 下午2:43:32
 * 修改备注：
 * 
 * @version
 */
public class ExceptionUtil
{
    /**
     * @param throwable
     * @return
     */
    public static String getStackTrace(final Throwable throwable)
    {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
