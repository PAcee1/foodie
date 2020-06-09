package com.pacee1.controller;

import com.pacee1.enums.YesOrNo;
import com.pacee1.pojo.*;
import com.pacee1.pojo.vo.CategoryVO;
import com.pacee1.pojo.vo.CommentLevelCountsVO;
import com.pacee1.pojo.vo.ItemInfoVO;
import com.pacee1.pojo.vo.NewItemsVO;
import com.pacee1.service.CarouselService;
import com.pacee1.service.CategoryService;
import com.pacee1.service.ItemService;
import com.pacee1.utils.PagedGridResult;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type IndexController.java
 * @Desc
 * @date 2020/6/8 15:20
 */
@RestController
@RequestMapping("items")
@Api(value = "商品相关接口",tags = "商品相关接口")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/info/{itemId}")
    @ApiOperation(value = "根据id查询商品相关信息",notes = "根据id查询商品相关信息")
    public ResponseResult info(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @PathVariable String itemId){
        if(itemId == null){
            return ResponseResult.errorMsg("商品ID不存在");
        }
        Items items = itemService.queryItems(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemsImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemsSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemsParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);
        itemInfoVO.setItemParams(itemsParam);

        return ResponseResult.ok(itemInfoVO);
    }

    @GetMapping("/commentLevel")
    @ApiOperation(value = "查询商品评价数量",notes = "查询商品评价数量")
    public ResponseResult commentLevel(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @RequestParam String itemId){
        if(itemId == null){
            return ResponseResult.errorMsg("商品ID不存在");
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);

        return ResponseResult.ok(countsVO);
    }

    @GetMapping("/comments")
    @ApiOperation(value = "查询商品评价",notes = "查询商品评价")
    public ResponseResult comments(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level",value = "评价类型",required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page",value = "当前页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页数量",required = false)
            @RequestParam Integer pageSize){
        if(itemId == null){
            return ResponseResult.errorMsg("商品ID不存在");
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }

        PagedGridResult result = itemService.queryItemComments(itemId, level, page, pageSize);

        return ResponseResult.ok(result);
    }

    @GetMapping("/search")
    @ApiOperation(value = "搜索商品",notes = "搜索商品接口")
    public ResponseResult search(
            @ApiParam(name = "keywords",value = "关键字",required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort",value = "排序类型，c为销量，p为价格，k为名称",required = false)
            @RequestParam String sort,
            @ApiParam(name = "page",value = "当前页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页数量",required = false)
            @RequestParam Integer pageSize){
        if(keywords == null){
            return ResponseResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }

        PagedGridResult result = itemService.searchItems(keywords, sort, page, pageSize);

        return ResponseResult.ok(result);
    }

    @GetMapping("/catItems")
    @ApiOperation(value = "搜索商品通过分类",notes = "搜索商品通过分类接口")
    public ResponseResult catItems(
            @ApiParam(name = "catId",value = "分类ID",required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort",value = "排序类型，c为销量，p为价格，k为名称",required = false)
            @RequestParam String sort,
            @ApiParam(name = "page",value = "当前页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页数量",required = false)
            @RequestParam Integer pageSize){
        if(catId == null){
            return ResponseResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }

        PagedGridResult result = itemService.searchItemsByCat(catId, sort, page, pageSize);

        return ResponseResult.ok(result);
    }
}
