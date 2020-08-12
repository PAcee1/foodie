package com.pacee1.controller;

import com.pacee1.service.ItemsESService;
import com.pacee1.utils.PagedGridResult;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-08-12 15:20
 **/
@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemsESService itemsESService;

    @GetMapping("/search")
    @ApiOperation(value = "搜索商品",notes = "搜索商品接口")
    public ResponseResult search(
            String keywords,
            String sort,
            Integer page,
            Integer pageSize){
        if(keywords == null){
            return ResponseResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        page --;

        if(pageSize == null){
            pageSize = 20;
        }

        PagedGridResult result = itemsESService.searchItems(keywords, sort, page, pageSize);

        return ResponseResult.ok(result);
    }
}
