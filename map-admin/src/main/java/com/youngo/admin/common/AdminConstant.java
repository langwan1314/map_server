/**
 * @Title: AdminConstant.java
 * @Package com.yjh.admin.common
 * @Description: 后台管理系统常量类
 * @author yiyan
 * @date 2014-11-17 下午1:37:24
 * @version
 */

package com.youngo.admin.common;

/**
 * 类名称：AdminConstant
 * 类描述：
 * 创建人：yiyan
 * 创建时间：2014-11-17 下午1:37:24
 * 修改人：yiyan
 * 修改时间：2014-11-17 下午1:37:24
 * 修改备注：
 * 
 * @version
 */
public final class AdminConstant
{
    public static final int COUNT = 30;// 默认每次返回数目
    public static final String SALE_STATUS_PRE_SALE = "0";// 预售
    public static final String SALE_STATUS_ON_SALE = "1";// 在售
    public static final String ON_SHELVE = "1";// 上架
    public static final String UN_SHELVE = "0";// 下架

    // 订单
    public static final String DEAL_TYPE_UNPAID = "unpaid"; // 未发货
    public static final String DEAL_TYPE_UNSEND = "unsend"; // 未送货
    public static final String DEAL_TYPE_UNRECEIVED = "unreceived";// 未收货
    public static final String DEAL_TYPE_UNCOMMENT = "uncomment";// 未评论
    public static final String DEAL_TYPE_APPLYREFUND = "applyRefund";// 申请退款

    public static final String GIFT_CATEGORY_TYPE = "工具,中小样,单片蚕丝,产品,其他"; // 赠品类别，以英文逗号分隔

}
