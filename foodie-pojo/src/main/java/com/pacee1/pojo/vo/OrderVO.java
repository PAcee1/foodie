package com.pacee1.pojo.vo;

/**
 * @Created by pace
 * @Date 2020/6/12 15:13
 * @Classname OrderVO
 *
 * 保存订单id和支付中心订单信息
 */
public class OrderVO {

    private String orderId;
    private MerchantOrderVO merchantOrderVO;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrderVO getMerchantOrderVO() {
        return merchantOrderVO;
    }

    public void setMerchantOrderVO(MerchantOrderVO merchantOrderVO) {
        this.merchantOrderVO = merchantOrderVO;
    }
}
