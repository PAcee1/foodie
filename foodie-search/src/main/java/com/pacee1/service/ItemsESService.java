package com.pacee1.service;

import com.pacee1.utils.PagedGridResult;
import org.springframework.stereotype.Service;

/**
 * <p>ES查询商品Service</p>
 *
 * @author : Pace
 * @date : 2020-08-12 17:09
 **/
public interface ItemsESService {

    PagedGridResult searchItems(String keyword, String sort, Integer page, Integer size);
}
