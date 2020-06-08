package com.pacee1.service;

import com.pacee1.pojo.Items;
import com.pacee1.pojo.ItemsImg;
import com.pacee1.pojo.ItemsParam;
import com.pacee1.pojo.ItemsSpec;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 17:20
 * @Classname ItemService
 */
public interface ItemService {

    /**
     * 查询商品基本信息
     * @param itemId
     * @return
     */
    Items queryItems(String itemId);

    /**
     * 查询商品图片列表
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemsImgList(String itemId);

    /**
     * 查询商品规格信息
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemsSpecList(String itemId);

    /**
     * 查询商品参数信息
     * @param itemId
     * @return
     */
    ItemsParam queryItemsParam(String itemId);
}
