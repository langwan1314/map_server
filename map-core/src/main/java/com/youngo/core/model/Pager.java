package com.youngo.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * 分页vo
 *
 * @author atao 2014-10-14
 */
public class Pager
{
    private Integer page = 1;// 当前页码
    private Integer size = 10;// 每页记录数
    private Integer count = 0;// 总记录数
    private List list;// 数据List

    /**
     * @return 获取偏移量
     */
    @JsonIgnore
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
     * @return 获取限制数
     */
    @JsonIgnore
    public int getLimit()
    {
        if (size > 0)
        {
            return size;
        }
        return 18;
    }

    /**
     * @return 获取总页数
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
    @JsonIgnore
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

    public List getList()
    {
        return list;
    }

    public void setList(List list)
    {
        this.list = list;
    }

    @Override
    public String toString()
    {
        return "Pager [page=" + page + ", size=" + size + ", count=" + count + "]";
    }

}
