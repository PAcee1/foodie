package com.pacee1.service.impl;

import com.pacee1.mapper.CarouselMapper;
import com.pacee1.pojo.Carousel;
import com.pacee1.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 10:06
 * @Classname CarouselServiceImpl
 */
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    /**
     * 查询首页轮播图
     * @param isShow
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Carousel> queryAll(Integer isShow) {
        // 使用Example来做查询条件，根据sort排序，并且isShow为1
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow",isShow);

        List<Carousel> carousels = carouselMapper.selectByExample(example);

        return carousels;
    }
}
