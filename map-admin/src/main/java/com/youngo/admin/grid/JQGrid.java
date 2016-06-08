/**
 * @Title: JQGrid.java
 * @Package com.yjh.admin.model
 * @Description: jqGrid 表格组件模型
 * @author yiyan
 * @date 2014-11-20 下午1:49:56
 * @version
 */
package com.youngo.admin.grid;

import java.util.List;

/**
 * 类名称：JQGrid
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-11-20 下午1:49:56
 * 修改人：yiyan
 * 修改时间：2014-11-20 下午1:49:56
 * 修改备注：
 * 
 * @version
 */
public class JQGrid<T>
{
    /**
     * @Fields 总页数
     */
    private int totalPages;

    /**
     * @Fields 当前页
     */
    private int currentPage;

    /**
     * @Fields 总记录数
     */
    private long totalRecords;

    /**
     * @Fields 数据
     */
    private List<T> data;

    /**
     * @return the totalPages
     */
    public int getTotalPages()
    {
        return totalPages;
    }

    /**
     * @param totalPages the totalPages to set
     */
    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    /**
     * @return the currentPage
     */
    public int getCurrentPage()
    {
        return currentPage;
    }

    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    /**
     * @return the totalRecords
     */
    public long getTotalRecords()
    {
        return totalRecords;
    }

    /**
     * @param totalRecords the totalRecords to set
     */
    public void setTotalRecords(long totalRecords)
    {
        this.totalRecords = totalRecords;
    }

    /**
     * @return the data
     */
    public List<T> getData()
    {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<T> data)
    {
        this.data = data;
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
        return "JQGrid [totalPages=" + totalPages + ", currentPage=" + currentPage + ", totalRecords=" + totalRecords
                + ", data=" + data + "]";
    }
}
