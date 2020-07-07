package com.pacee1.controller;

import com.pacee1.enums.OrderStatusEnum;
import com.pacee1.enums.PayMethod;
import com.pacee1.pojo.OrderStatus;
import com.pacee1.pojo.bo.OrderBO;
import com.pacee1.pojo.bo.ShopcartBO;
import com.pacee1.pojo.vo.MerchantOrderVO;
import com.pacee1.pojo.vo.OrderVO;
import com.pacee1.service.OrderService;
import com.pacee1.utils.CookieUtils;
import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.RedisOperator;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type IndexController.java
 * @Desc
 * @date 2020/6/8 15:20
 */
@RestController
@RequestMapping("orders")
@Api(value = "订单相关接口",tags = "订单相关接口")
public class OrderController {

/*
    // 回调地址
    private static final String RETURN_URL = "http://raqpsv.natappfree.cc/orders/notifyMerchantOrderPaid";
    // 订单中心地址
    private static final String PAYMENT_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
*/

    @Value("${pacee1.pay.returnUrl}")
    private String RETURN_URL;
    @Value("${pacee1.pay.paymentUrl}")
    private String PAYMENT_URL;

    @Autowired
    private OrderService orderService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisOperator redisOperator;

    @PostMapping("/create")
    @ApiOperation(value = "创建订单",notes = "创建订单接口")
    public ResponseResult create(
            @ApiParam(name = "orderBO",value = "订单信息",required = true)
            @RequestBody OrderBO orderBO,
            HttpServletRequest request,
            HttpServletResponse response){
       if(orderBO.getPayMethod() != PayMethod.WX.type &&
       orderBO.getPayMethod() != PayMethod.ZFB.type){
           return ResponseResult.errorMsg("支付方式不支持");
       }

        /**
         * 获取购物车缓存
         */
        // 从购物车删除商品
        String shopcatStr = redisOperator.get("shopcart:" + orderBO.getUserId());
        // 判断缓存是否存在
        if(StringUtils.isBlank(shopcatStr)){
            return ResponseResult.errorMsg("购物数据出错，无购物车");
        }
        List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcatStr, ShopcartBO.class);

        /**
         * 1.创建订单
         */
        OrderVO orderVO = orderService.create(orderBO,shopcartList);
        String orderId = orderVO.getOrderId();

        /**
         * 2.清除购物车中数据
         */
        // 从redis获取购物车数据，对商品清除，同步cookie
        shopcartList.removeAll(orderVO.getNeedRemoveList());
        redisOperator.set("shopcart:" + orderBO.getUserId(),JsonUtils.objectToJson(shopcartList));
        // 当前直接重置cookie的购物车
        CookieUtils.setCookie(request,response,"shopcart","",true);

        /**
         *  3.调用支付中心
         */
        MerchantOrderVO merchantOrderVO = orderVO.getMerchantOrderVO();
        merchantOrderVO.setReturnUrl(RETURN_URL);
        // 设置为1分钱，方便测试
        merchantOrderVO.setAmount(1);

        // 使用RestTemplate创建订单
        HttpHeaders httpHeaders = new HttpHeaders();
        // 添加权证
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("imoocUserId","imooc");
        httpHeaders.add("password","imooc");

        HttpEntity<MerchantOrderVO> httpEntity = new HttpEntity<>(merchantOrderVO,httpHeaders);
        // 远程调用支付中心
        ResponseEntity<ResponseResult> responseResult = restTemplate.postForEntity(PAYMENT_URL, httpEntity, ResponseResult.class);
        ResponseResult result = responseResult.getBody();
        if(result.getStatus() != 200){
            return ResponseResult.errorMsg("订单创建失败，支付中心调用失败");
        }

        return ResponseResult.ok(orderId);
    }

    @PostMapping("/notifyMerchantOrderPaid")
    @ApiOperation(value = "订单支付后回调接口",notes = "订单支付后回调接口，修改订单状态为已支付")
    public Integer notifyMerchantOrderPaid(String merchantOrderId){
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("/getPaidOrderInfo")
    @ApiOperation(value = "前端获取订单状态",notes = "前端获取订单状态，如果已支付会跳转支付成功页面")
    public ResponseResult getPaidOrderInfo(String orderId){
        OrderStatus orderStatus = orderService.getOrderStatus(orderId);
        return ResponseResult.ok(orderStatus);
    }

}
