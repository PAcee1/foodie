package com.pacee1.service.center;

import com.pacee1.pojo.OrderItems;
import com.pacee1.pojo.Orders;
import com.pacee1.pojo.bo.center.OrderItemsCommentBO;
import com.pacee1.utils.PagedGridResult;

import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type UserService.java
 * @Desc
 * @date 2020/5/17 15:16
 */
public interface MyCommentService {

    List<OrderItems> queryPendingComment(String orderId);

    void saveCommentList(String userId,String orderId,List<OrderItemsCommentBO> orderItemList);

    PagedGridResult queryCommentList(String userId,Integer page,Integer pagesize);
}
