package com.pacee1.mapper;

import com.pacee1.pojo.vo.ItemCommentVO;
import com.pacee1.pojo.vo.SearchItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemsCommentsMapperCustom {

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
}