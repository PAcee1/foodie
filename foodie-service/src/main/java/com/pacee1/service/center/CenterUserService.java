package com.pacee1.service.center;

import com.pacee1.pojo.UserAddress;
import com.pacee1.pojo.Users;
import com.pacee1.pojo.bo.UserAddressBO;
import com.pacee1.pojo.bo.center.CenterUserBO;

import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type UserService.java
 * @Desc
 * @date 2020/5/17 15:16
 */
public interface CenterUserService {

    Users queryUserInfo(String userId);

    Users updateUserInfo(String userId, CenterUserBO userBO);
}
