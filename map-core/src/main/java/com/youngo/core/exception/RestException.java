/**
 * @Title: RestException.java
 * @Package com.yjh.admin.common.exception
 * @Description: 如果方法返回的是 application/json，则抛出此异常
 * @author yiyan
 * @date 2014-12-9 下午4:57:51
 * @version
 */
package com.youngo.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 抛出RestException后，前端将接收到遗传json形式的数据，在json中记录异常的code和msg<br>
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
     * @param @param code 异常号
     * @param @param msg  出现异常的原因
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
