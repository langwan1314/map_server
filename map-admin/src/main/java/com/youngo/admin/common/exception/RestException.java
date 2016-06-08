/**
 * @Title: RestException.java
 * @Package com.yjh.admin.common.exception
 * @Description: 如果方法返回的是 application/json，则抛出此异常
 * @author yiyan
 * @date 2014-12-9 下午4:57:51
 * @version
 */
package com.youngo.admin.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 类名称：RestException
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-12-9 下午4:57:51
 * 修改人：yiyan
 * 修改时间：2014-12-9 下午4:57:51
 * 修改备注：
 * 
 * @version
 */
public class RestException extends RuntimeException
{

    /**
     * @Fields serialVersionUID
     */
    @JsonIgnore
    private static final long serialVersionUID = 2287328304891090648L;

    private int code;

    /**
     * @Title:
     * @Description: 构造方法
     * @param @param code
     * @param @param msg
     * @return RestException
     * @throws
     */
    public RestException(int code, String msg)
    {
        super(msg);
        this.code = code;
    }

    /**
     * @return the code
     */
    public int getCode()
    {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code)
    {
        this.code = code;
    }

    /*
     * (non-Javadoc)
     * <p>Title: toString</p>
     * <p>Description: </p>
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "RestException [code=" + code + "]";
    }

}
