package com.pacee1.mapper;

import com.pacee1.pojo.Category;
import com.pacee1.pojo.vo.CategoryVO;
import com.pacee1.pojo.vo.NewItemsVO;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 10:58
 * @Classname CategoryMapperCustom
 */
public interface CategoryMapperCustom {

    /**
     * 根据一级分类获取子分类
     * @param fatherId
     * @return
     */
    List<CategoryVO> getSubCatList(Integer fatherId);

    /**
     * 根据分类id获取最新的6个商品，推荐到首页
     * @param fatherId
     * @return
     */
    List<NewItemsVO> getSixNewItemsLazy(Integer fatherId);
}
