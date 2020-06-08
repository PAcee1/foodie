package com.pacee1.controller;

import com.pacee1.enums.YesOrNo;
import com.pacee1.pojo.*;
import com.pacee1.pojo.vo.CategoryVO;
import com.pacee1.pojo.vo.ItemInfoVO;
import com.pacee1.pojo.vo.NewItemsVO;
import com.pacee1.service.CarouselService;
import com.pacee1.service.CategoryService;
import com.pacee1.service.ItemService;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
