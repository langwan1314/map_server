/**
 * @Title: GenericException.java
 * @Package com.yjh.admin.common.exception
 * @Description: 通用异常类
 * @author yiyan
 * @date 2014-12-9 下午5:03:31
 * @version
 */
package com.youngo.admin.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 类名称：GenericException
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-12-9 下午5:03:31
 * 修改人：yiyan
 * 修改时间：2014-12-9 下午5:03:31
 * 修改备注：
 */
public class GenericException extends RuntimeException
{

    @JsonIgnore
    private static final long serialVersionUID = 8613671863725827809L;

    private int code;

    public GenericException(int code, String msg)
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
        return "GenericException [code=" + code + "]";
    }

}
