package com.pacee1.service;

import com.pacee1.pojo.*;
import com.pacee1.pojo.bo.OrderBO;
import com.pacee1.pojo.bo.ShopcartBO;
import com.pacee1.pojo.vo.CommentLevelCountsVO;
import com.pacee1.pojo.vo.OrderVO;
import com.pacee1.pojo.vo.ShopcartVO;
import com.pacee1.utils.PagedGridResult;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 17:20
 * @Classname ItemService
 */
public interface OrderService {

    OrderVO create(OrderBO orderBO,List<ShopcartBO> shopcartList);

    /**
     * 更新订单支付状态
     * @param merchantOrderId
     * @param type
     */
    void updateOrderStatus(String merchantOrderId, Integer type);

    OrderStatus getOrderStatus(String orderId);

    void closeOrder();
}
