package com.pacee1.service.center;

import com.pacee1.pojo.Users;
import com.pacee1.pojo.bo.center.CenterUserBO;
import com.pacee1.utils.PagedGridResult;

/**
 * @author pace
 * @version v1.0
 * @Type UserService.java
 * @Desc
 * @date 2020/5/17 15:16
 */
public interface MyOrderService {

    /**
     * 分页查询订单
     * @param userId
     * @param orderStatus
     * @param page
     * @param pagesize
     * @return
     */
    PagedGridResult queryMyOrderList(String userId,String orderStatus,Integer page,Integer pagesize);

    void updateDeliverOrder(String orderId);

    void updateConfirmOrder(String orderId);

    void updateDeleteOrder(String orderId);

    boolean checkUserOrder(String userId,String orderId);

}
