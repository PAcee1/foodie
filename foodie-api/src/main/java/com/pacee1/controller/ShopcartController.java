package com.pacee1.controller;

import com.pacee1.pojo.Items;
import com.pacee1.pojo.ItemsImg;
import com.pacee1.pojo.ItemsParam;
import com.pacee1.pojo.ItemsSpec;
import com.pacee1.pojo.bo.ShopcartBO;
import com.pacee1.pojo.vo.CommentLevelCountsVO;
import com.pacee1.pojo.vo.ItemInfoVO;
import com.pacee1.service.ItemService;
import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.PagedGridResult;
import com.pacee1.utils.RedisOperator;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type IndexController.java
 * @Desc
 * @date 2020/6/8 15:20
 */
@RestController
@RequestMapping("shopcart")
@Api(value = "购物车相关接口",tags = "购物车相关接口")
public class ShopcartController {

    /**   购物车使用Cookie+Redis实现   **/

    @Autowired
    private RedisOperator redisOperator;

    @PostMapping("/add")
    @ApiOperation(value = "添加购物车接口",notes = "添加购物车接口")
    public ResponseResult add(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response){
        if(userId == null){
            return ResponseResult.errorMsg("用户不存在");
        }

        //System.out.println(shopcartBO);
        // 添加商品到Redis购物车中
        String shopcatStr = redisOperator.get("shopcart:" + userId);
        List<ShopcartBO> shopcartList = null;
        // 判断缓存是否存在
        if(!StringUtils.isBlank(shopcatStr)){
            shopcartList = JsonUtils.jsonToList(shopcatStr, ShopcartBO.class);
            // 如果存在，判断当前添加商品是否已经存在，存在则数量添加，不存在则直接添加
            boolean isHaving = false;
            for (ShopcartBO bo : shopcartList) {
                if(bo.getSpecId().equals(shopcartBO.getSpecId())){
                    // 数量添加
                    bo.setBuyCounts(bo.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if(!isHaving){
                shopcartList.add(shopcartBO);
            }
        }else {
            // 不存在，新建购物车
            shopcartList = new ArrayList<>();
            // 直接添加到购物车
            shopcartList.add(shopcartBO);
        }

        // 重新存放到Redis
        redisOperator.set("shopcart:"+userId , JsonUtils.objectToJson(shopcartList));

        return ResponseResult.ok();
    }


    @PostMapping("/del")
    @ApiOperation(value = "删除购物车商品接口",notes = "删除购物车商品接口")
    public ResponseResult del(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)){
            return ResponseResult.errorMsg("参数为空");
        }

        // 从购物车删除商品
        String shopcatStr = redisOperator.get("shopcart:" + userId);
        // 判断缓存是否存在
        if(!StringUtils.isBlank(shopcatStr)){
            List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcatStr, ShopcartBO.class);
            for (ShopcartBO bo : shopcartList) {
                if(bo.getSpecId().equals(itemSpecId)){
                    // 从list中删除
                    shopcartList.remove(bo);
                    break;
                }
            }
            // 更新Redis
            redisOperator.set("shopcart:"+userId , JsonUtils.objectToJson(shopcartList));
        }

        return ResponseResult.ok();
    }
}
