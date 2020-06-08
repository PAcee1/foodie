package com.pacee1.service.impl;

import com.pacee1.mapper.CategoryMapper;
import com.pacee1.mapper.CategoryMapperCustom;
import com.pacee1.pojo.Category;
import com.pacee1.pojo.vo.CategoryVO;
import com.pacee1.pojo.vo.NewItemsVO;
import com.pacee1.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 10:29
 * @Classname CategoryServiceImpl
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    /**
     * 查询所有一级分类
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type",1);

        List<Category> categories = categoryMapper.selectByExample(example);

        return categories;
    }

    /**
     * 查询二级三级分类
     * @param fatherId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CategoryVO> getSubCatList(Integer fatherId) {
        List<CategoryVO> result = categoryMapperCustom.getSubCatList(fatherId);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<NewItemsVO> getSixNewItemsLazy(Integer fatherId) {
        return categoryMapperCustom.getSixNewItemsLazy(fatherId);
    }
}
