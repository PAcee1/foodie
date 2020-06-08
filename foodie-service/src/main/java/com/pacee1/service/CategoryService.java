package com.pacee1.service;

import com.pacee1.pojo.Category;
import com.pacee1.pojo.vo.CategoryVO;
import com.pacee1.pojo.vo.NewItemsVO;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 10:29
 * @Classname CategoryService
 */
public interface CategoryService {

    /**
     * 查询所有一级分类
     * @return
     */
    List<Category> queryAllRootLevelCat();

    /**
     * 查询二三级分类根据一级分类
     * @param fatherId
     * @return
     */
    List<CategoryVO> getSubCatList(Integer fatherId);

    /**
     * 懒加载根据分类id查询6个商品
     * @param fatherId
     * @return
     */
    List<NewItemsVO> getSixNewItemsLazy(Integer fatherId);
}
