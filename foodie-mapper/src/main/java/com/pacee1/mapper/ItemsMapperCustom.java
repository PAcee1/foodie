package com.pacee1.mapper;

import com.pacee1.pojo.vo.ItemCommentVO;
import com.pacee1.pojo.vo.SearchItemsVO;
import com.pacee1.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemsMapperCustom {

    /**
     * 查询商品评价
     * @param itemId
     * @param level
     * @return
     */
    List<ItemCommentVO> queryItemComments(@Param("itemId")String itemId, @Param("level") Integer level);

    /**
     * 搜索商品
     * @param keyword
     * @param sort
     * @return
     */
    List<SearchItemsVO> searchItems(@Param("keyword")String keyword, @Param("sort") String sort);

    /**
     * 根据分类搜索商品
     * @param catId
     * @param sort
     * @return
     */
    List<SearchItemsVO> searchItemsByCat(@Param("catId")Integer catId, @Param("sort") String sort);

    /**
     * 根据规格id获取商品信息列表
     * @param specIdsList
     * @return
     */
    List<ShopcartVO> queryItemsBySpecIds(@Param("specIdsList")List specIdsList);

    /**
     * 简单使用乐观锁，减少库存防止超卖
     * @param specId
     * @param pendingCounts
     * @return
     */
    Integer decreaseItemSpecStock(@Param("specId")String specId, @Param("pendingCounts") Integer pendingCounts);
}