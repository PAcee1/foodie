package com.pacee1.service.center.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pacee1.enums.OrderStatusEnum;
import com.pacee1.enums.YesOrNo;
import com.pacee1.mapper.OrderMapperCustom;
import com.pacee1.mapper.OrderStatusMapper;
import com.pacee1.mapper.OrdersMapper;
import com.pacee1.mapper.UsersMapper;
import com.pacee1.pojo.OrderStatus;
import com.pacee1.pojo.Orders;
import com.pacee1.pojo.Users;
import com.pacee1.pojo.bo.center.CenterUserBO;
import com.pacee1.pojo.vo.MyOrdersVO;
import com.pacee1.service.center.CenterUserService;
import com.pacee1.service.center.MyOrderService;
import com.pacee1.utils.PagedGridResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pace
 * @version v1.0
 * @Type UserServiceImpl.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@Service
public class MyOrderServiceImpl implements MyOrderService {

    @Autowired
    private OrderMapperCustom orderMapperCustom;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PagedGridResult queryMyOrderList(String userId, String orderStatus, Integer page, Integer pagesize) {
        PageHelper.startPage(page,pagesize);

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId",userId);
        if(orderStatus != null){
            paramMap.put("orderStatus",orderStatus);
        }
        List<MyOrdersVO> myOrdersVOS = orderMapperCustom.queryMyOrderList(paramMap);

        return setPagedGridResult(myOrdersVOS,page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeliverOrder(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        orderStatus.setDeliverTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateConfirmOrder(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeleteOrder(String orderId) {
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsDelete(YesOrNo.YES.type);
        order.setUpdatedTime(new Date());

        ordersMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean checkUserOrder(String userId, String orderId) {
        Orders order = new Orders();
        order.setId(orderId);
        order.setUserId(userId);

        Orders orders = ordersMapper.selectOne(order);

        return orders == null ? false : true;
    }

    /**
     * 封装分页结果
     * @param list
     * @param page
     * @return
     */
    private PagedGridResult setPagedGridResult(List<?> list,Integer page){
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult result = new PagedGridResult();
        result.setPage(page);
        result.setRows(list);
        result.setRecords(pageInfo.getTotal());
        result.setTotal(pageInfo.getPages());

        return result;
    }
}
