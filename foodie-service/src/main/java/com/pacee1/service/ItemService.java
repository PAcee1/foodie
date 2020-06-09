package com.pacee1.service;

import com.pacee1.pojo.Items;
import com.pacee1.pojo.ItemsImg;
import com.pacee1.pojo.ItemsParam;
import com.pacee1.pojo.ItemsSpec;
import com.pacee1.pojo.vo.CommentLevelCountsVO;
import com.pacee1.pojo.vo.ItemCommentVO;
import com.pacee1.pojo.vo.SearchItemsVO;
import com.pacee1.utils.PagedGridResult;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 查询评价数量
     * @param itemId
     * @return
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 分页查询评价
     * @param itemId
     * @param level
     * @param page
     * @param size
     * @return
     */
    PagedGridResult queryItemComments(String itemId, Integer level, Integer page, Integer size);

    /**
     * 分页搜索商品
     * @param keyword
     * @param sort
     * @param page
     * @param size
     * @return
     */
    PagedGridResult searchItems(String keyword, String sort, Integer page, Integer size);

    /**
     * 分页搜索商品根据分类
     * @param catId
     * @param sort
     * @param page
     * @param size
     * @return
     */
    PagedGridResult searchItemsByCat(Integer catId, String sort, Integer page, Integer size);
}
