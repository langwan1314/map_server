/**
 * @Title: Paginator.java
 * @Package com.yjh.admin.model
 * @Description: 分页辅助类
 * @author yiyan
 * @date 2014-11-29 下午5:41:53
 * @version
 */
package com.youngo.admin.model;

/**
 * 类名称：Paginator
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-11-29 下午5:41:53
 * 修改人：yiyan
 * 修改时间：2014-11-29 下午5:41:53
 * 修改备注：
 * 
 * @version
 */
public class Paginator
{
    public static final int COUNT = 30;// 默认每次返回数目

    private int page; // 当前页数

    private int size; // 每页总数

    private int count; // 总记录数

    /**
     * @Title:
     * @Description: 有参构造方法
     * @param @param page
     * @param @param size
     * @return Paginator
     * @throws
     */
    public Paginator(int page, int size, int count)
    {
        super();
        this.page = page;
        this.size = size;
        this.count = count;
    }

    /**
     * @return
     *         获取偏移量
     */
    public int getOffset()
    {
        int result = 0;

        if (page > 1)
        {
            result = (page - 1) * size;
        }

        return result;
    }

    /**
     * @return
     *         获取限制数
     */
    public int getLimit()
    {
        int result = COUNT;

        if (size > 0)
        {
            result = size;
        }

        return result;
    }

    /**
     * @return
     *         获取总页数
     */
    public int getTotalPages()
    {
        return (count / size + ((count % size) > 0 ? 1 : 0));
    }

    /**
     * @return the page
     */
    public int getPage()
    {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(int page)
    {
        this.page = page;
    }

    /**
     * @return the size
     */
    public int getSize()
    {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size)
    {
        this.size = size;
    }

    /**
     * @return the count
     */
    public int getCount()
    {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count)
    {
        this.count = count;
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
        return "Paginator [page=" + page + ", size=" + size + ", count=" + count + "]";
    }

}
