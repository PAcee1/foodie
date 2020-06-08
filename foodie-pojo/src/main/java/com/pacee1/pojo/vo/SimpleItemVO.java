package com.pacee1.pojo.vo;

import java.util.Date;

/**
 * @Created by pace
 * @Date 2020/6/8 16:52
 * @Classname SimpleItemVO
 * 简单商品VO
 */
public class SimpleItemVO {

    private String itemId;
    private String itemName;
    private String itemUrl;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }
}
