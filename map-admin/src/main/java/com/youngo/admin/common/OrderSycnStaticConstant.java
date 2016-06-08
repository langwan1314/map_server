package com.youngo.admin.common;

/**
 * @Title: OrderSycnStaticConstant.java
 * @Package com.yjh.admin.common
 * @Description: 同步订单信息常量
 * @author atao
 * @date 2015-1-20 上午9:48:06
 * @version V1.0
 */
public class OrderSycnStaticConstant
{
    /**
     * dsadasd
     */
    public static final Integer ORDER_STATUS_SEND = 3; // 订单已经发货 待收货
    public static final Integer ORDER_STATUS_PAID = 2; // 订单已支付 待发货
    public static final Integer ORDER_STATUS_UNPAID = 1; // 订单未支付 待支付
    public static final Integer ORDER_STATUS_RECEIVED = 4; // 订单已经收货 待评价
    public static final Integer ORDER_STATUS_COMMENT = 5; // 订单已经评价 交易完成
    public static final Integer ORDER_STATUS_PAY_CONFIRM = 6; // 订单支付待确认
    public static final Integer ORDER_STATUS_CANCEL = -1; // 订单关闭（已取消或退款完毕）
    public static final Integer ORDER_STATUS_OVERDUE = -2; // 订单已过期

    public static final Integer REFUND_STATUS_REFUSED = 1; // 退款订单被拒绝
    public static final Integer REFUND_STATUS_CHECKPENDINGE = 0; // 退款订单待审核
    public static final Integer REFUND_STATUS_AGREE = 2; // 退款订单同意
    public static final Integer REFUND_STATUS_REFUNDED = 4; // 退款订单已退款

    // 私钥
    public static String SMIS_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK3bxc+nUkPmmDZMHMj/eYWQe6MOHUtw+BVIdEbLANo4ngJ04zjDC5EGmHkDS/Np9Ih1lr0DETrmEEjyilw465wD4dtm67riiIRRA1ljDRfOBd4X7PJ1oF7HNW+1hcdmirWs979bWabYF9DfZeUYx22HUHMlGTB81W6P2f/XR2QfAgMBAAECgYEAlSp7tKdFkNZ3ABcbl2SCOhN10LdacRDS+Ue548dmytRK1aa+EURfVyXGigVHE+hzkuT42OCZ1uPvUvctuORpWH+ABeaz2Pt/3O0uD/WGfPremQQIWckMrEkbD4v/YTAZzg4zMo32Yt/2U5N3vynkniq5Dh92hcXNl3vDHRISU4ECQQDU7l/iYsl+d39qmWsNF18+JyUp1QJsEbW406/Kz9V2FwUZSFIqewGk3L1a6uVoaIvqT/P3DPLuhY/Y3f10g/FfAkEA0QY01P92kEZk9H7lZDHHgdqUcOKOiFLaZop3kDSqy3ut4bEk4LxnhF2Q3c4d2AbPu5wGrHjNGG76YETT5pfFQQJAUJKPB9w3wLo5Jb4+CusgKCWp42VuEDS3XeQEbiss7A2T6Vg7d0WqfpbktCWUf2ioIRb2CzMELguuPfLqeI+A4wJAIbMEdYqyTNRsllaXGS5FYhQhdomwPR5Og9WzLRawqpnIEOkEbw8xCVAkeP4wJZIDj9W55bicKGJMD5VJLJYvgQJACeuUMTilooCUV6cdy0NrUU2PbVUOu0k9oMEdI/IV/rRtkKq6inFw2EC1NDZAe1IUX4aEVcy/E/Ss3PYhWpunyg==";
    // 公钥
    public static String SMIS_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt28XPp1JD5pg2TBzI/3mFkHujDh1LcPgVSHRGywDaOJ4CdOM4wwuRBph5A0vzafSIdZa9AxE65hBI8opcOOucA+HbZuu64oiEUQNZYw0XzgXeF+zydaBexzVvtYXHZoq1rPe/W1mm2BfQ32XlGMdth1BzJRkwfNVuj9n/10dkHwIDAQAB";
    // SMIS 授信 ID
    public static String SMIS_CLIENT_ID = "TLZRZDsPAv2xH5EaxmygwabzSWajtTPb";
}
