package com.youngo.core.model;

/**
 * @author fuchen
 *         服务端返回给客户端的结果封装
 */
public class Result implements Codes
{
    public static final int i = 0;

    private int code;
    private String msg;
    private Object data;

    public Result()
    {
        this.code = success_code;
        this.msg = success_msg;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }
}
