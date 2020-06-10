package com.pacee1.controller;

import com.pacee1.pojo.Items;
import com.pacee1.pojo.ItemsImg;
import com.pacee1.pojo.ItemsParam;
import com.pacee1.pojo.ItemsSpec;
import com.pacee1.pojo.bo.ShopcartBO;
import com.pacee1.pojo.vo.CommentLevelCountsVO;
import com.pacee1.pojo.vo.ItemInfoVO;
import com.pacee1.service.ItemService;
import com.pacee1.utils.PagedGridResult;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("shopcart")
@Api(value = "购物车相关接口",tags = "购物车相关接口")
public class ShopcartController {

    /**   购物车使用Cookie+Redis实现   **/

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

        System.out.println(shopcartBO);
        // TODO 添加商品到Redis购物车中

        return ResponseResult.ok();
    }

}
