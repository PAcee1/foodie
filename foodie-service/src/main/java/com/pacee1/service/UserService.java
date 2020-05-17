package com.pacee1.service;

import com.pacee1.pojo.Users;
import com.pacee1.pojo.bo.UserBO;

/**
 * @author pace
 * @version v1.0
 * @Type UserService.java
 * @Desc
 * @date 2020/5/17 15:16
 */
public interface UserService {

    // 判断用户名是否存在
    boolean queryUsernameIsExist(String username);

    // 创建用户
    Users createUser(UserBO userBO);
}
