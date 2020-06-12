package com.pacee1.service;

import com.pacee1.pojo.UserAddress;
import com.pacee1.pojo.bo.UserAddressBO;

import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type UserService.java
 * @Desc
 * @date 2020/5/17 15:16
 */
public interface AddressService {

    /**
     * 查
     * @param userId
     * @return
     */
    List<UserAddress> queryUserAddress(String userId);

    void add(UserAddressBO userAddressBO);

    void edit(UserAddressBO userAddressBO);

    void delete(String userId,String addressId);

    void setDefaultAddress(String userId,String addressId);

    UserAddress queryById(String addressId);
}
