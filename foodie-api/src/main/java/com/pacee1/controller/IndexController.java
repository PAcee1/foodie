package com.pacee1.controller;

import com.pacee1.enums.YesOrNo;
import com.pacee1.pojo.Carousel;
import com.pacee1.pojo.Category;
import com.pacee1.pojo.Users;
import com.pacee1.pojo.bo.UserBO;
import com.pacee1.pojo.vo.CategoryVO;
import com.pacee1.pojo.vo.NewItemsVO;
import com.pacee1.service.CarouselService;
import com.pacee1.service.CategoryService;
import com.pacee1.service.UserService;
import com.pacee1.utils.CookieUtils;
import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.RedisOperator;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
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
@RequestMapping("index")
@Api(value = "首页展示接口",tags = "首页展示相关的接口")
public class IndexController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/carousel")
    @ApiOperation(value = "轮播图查询",notes = "获取首页轮播图列表")
    public ResponseResult carousel(){
        // 引入Redis，缓存轮播图信息
        // 判断Redis是否存在
        String carouselStr = redisOperator.get("carousel");
        List<Carousel> carousels = null;
        if(StringUtils.isBlank(carouselStr)) {
            carousels = carouselService.queryAll(YesOrNo.YES.type);
            // 默认设置一天的过期时间
            redisOperator.set("carousel", JsonUtils.objectToJson(carousels),60*60*24);
        }else {
            carousels = JsonUtils.jsonToList(carouselStr, Carousel.class);
        }
        /**
         * 问题：这里设置默认一天过期时间，如何改造？
         * 1.创建后台管理系统，一旦轮播图修改，缓存重置
         * 2.轮播图表添加过期时间字段，进行循环设置缓存
         */
        return ResponseResult.ok(carousels);
    }

    /**
     * 查询首页分类，默认查询一级分类
     * 当用户鼠标移到一级分类时，才懒加载查询二级三级分类
     */
    @GetMapping("/cats")
    @ApiOperation(value = "查询所有一级分类",notes = "查询所有一级分类")
    public ResponseResult cats(){
        // 引入缓存
        String catsStr = redisOperator.get("cats");
        List<Category> categories = null;
        if(StringUtils.isBlank(catsStr)) {
            categories = categoryService.queryAllRootLevelCat();
            // 默认设置一天的过期时间
            redisOperator.set("cats", JsonUtils.objectToJson(categories),60*60*24);
        }else {
            categories = JsonUtils.jsonToList(catsStr, Category.class);
        }
        return ResponseResult.ok(categories);
    }

    @GetMapping("/subCat/{fatherId}")
    @ApiOperation(value = "查询一级分类下的二三级分类",notes = "查询一级分类下的二三级分类")
    public ResponseResult subCat(
            @ApiParam(name = "fatherId",value = "一级分类id",required = true)
            @PathVariable Integer fatherId){
        // 引入缓存
        if(fatherId == null){
            return ResponseResult.errorMsg("分类不存在");
        }
        // key 为 subCat + fatherId
        String subCatKey = "subCat:" + fatherId;
        String subCatStr = redisOperator.get("subCatKey");
        List<CategoryVO> result = null;
        if(StringUtils.isBlank(subCatStr)) {
            result = categoryService.getSubCatList(fatherId);
            // 默认设置一天的过期时间
            redisOperator.set(subCatKey, JsonUtils.objectToJson(result),60*60*24);
        }else {
            result = JsonUtils.jsonToList(subCatStr, CategoryVO.class);
        }
        return ResponseResult.ok(result);
    }

    @GetMapping("/sixNewItems/{fatherId}")
    @ApiOperation(value = "查询一级分类下最新的6条商品信息",notes = "查询一级分类下最新的6条商品信息")
    public ResponseResult sixNewItems(
            @ApiParam(name = "fatherId",value = "一级分类id",required = true)
            @PathVariable Integer fatherId){
        if(fatherId == null){
            return ResponseResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> result = categoryService.getSixNewItemsLazy(fatherId);
        return ResponseResult.ok(result);
    }
}
