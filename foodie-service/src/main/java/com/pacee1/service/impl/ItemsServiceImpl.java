package com.pacee1.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pacee1.enums.CommentLevel;
import com.pacee1.mapper.*;
import com.pacee1.pojo.*;
import com.pacee1.pojo.vo.CommentLevelCountsVO;
import com.pacee1.pojo.vo.ItemCommentVO;
import com.pacee1.pojo.vo.SearchItemsVO;
import com.pacee1.pojo.vo.ShopcartVO;
import com.pacee1.service.ItemService;
import com.pacee1.utils.DesensitizationUtil;
import com.pacee1.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 10:06
 * @Classname CarouselServiceImpl
 */
@Service
public class ItemsServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items queryItems(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsImg> queryItemsImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsSpec> queryItemsSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsParam queryItemsParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCountsByLevel(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCountsByLevel(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCountsByLevel(itemId, CommentLevel.BAD.type);
        // 总平均数
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountsVO result = new CommentLevelCountsVO();
        result.setGoodCounts(goodCounts);
        result.setNormalCounts(normalCounts);
        result.setBadCounts(badCounts);
        result.setTotalCounts(totalCounts);

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryItemComments(String itemId, Integer level, Integer page, Integer size) {
        // 分页
        PageHelper.startPage(page,size);

        List<ItemCommentVO> commentVOS = itemsMapperCustom.queryItemComments(itemId, level);
        // 对用户名脱敏
        for (ItemCommentVO commentVO : commentVOS) {
            commentVO.setNickname(DesensitizationUtil.commonDisplay(commentVO.getNickname()));
        }

        return setPagedGridResult(commentVOS,page);
    }

    @Override
    public PagedGridResult searchItems(String keyword, String sort, Integer page, Integer size) {
        // 分页
        PageHelper.startPage(page,size);

        List<SearchItemsVO> list = itemsMapperCustom.searchItems(keyword, sort);

        return setPagedGridResult(list,page);
    }

    @Override
    public PagedGridResult searchItemsByCat(Integer catId, String sort, Integer page, Integer size) {
        // 分页
        PageHelper.startPage(page,size);

        List<SearchItemsVO> list = itemsMapperCustom.searchItemsByCat(catId, sort);

        return setPagedGridResult(list,page);
    }

    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        // 分隔ids，组装成集合
        String[] strings = specIds.split(",");
        List list = new ArrayList();
        Collections.addAll(list,strings);

        List<ShopcartVO> shopcartVOS = itemsMapperCustom.queryItemsBySpecIds(list);
        return shopcartVOS;
    }

    /**
     * 获取各个等级评价数
     * @param itemId
     * @param level
     * @return
     */
    private Integer getCommentCountsByLevel(String itemId,Integer level){
        ItemsComments itemsComments = new ItemsComments();
        itemsComments.setItemId(itemId);
        itemsComments.setCommentLevel(level);

        return itemsCommentsMapper.selectCount(itemsComments);
    }

    /**
     * 封装分页结果
     * @param list
     * @param page
     * @return
     */
    private PagedGridResult setPagedGridResult(List<?> list,Integer page){
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult result = new PagedGridResult();
        result.setPage(page);
        result.setRows(list);
        result.setRecords(pageInfo.getTotal());
        result.setTotal(pageInfo.getPages());

        return result;
    }
}
