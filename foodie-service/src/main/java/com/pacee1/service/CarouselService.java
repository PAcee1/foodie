package com.pacee1.service;

import com.pacee1.pojo.Carousel;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 10:05
 * @Classname CarouselService
 */
public interface CarouselService {

    List<Carousel> queryAll(Integer isShow);
}
