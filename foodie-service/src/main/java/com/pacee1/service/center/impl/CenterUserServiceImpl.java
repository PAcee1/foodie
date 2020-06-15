package com.pacee1.service.center.impl;

import com.pacee1.mapper.UserAddressMapper;
import com.pacee1.mapper.UsersMapper;
import com.pacee1.pojo.UserAddress;
import com.pacee1.pojo.Users;
import com.pacee1.pojo.bo.center.CenterUserBO;
import com.pacee1.service.center.CenterUserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type UserServiceImpl.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserInfo(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users updateUserInfo(String userId, CenterUserBO userBO) {
        Users users = new Users();
        BeanUtils.copyProperties(userBO,users);
        users.setUpdatedTime(new Date());
        users.setId(userId);
        usersMapper.updateByPrimaryKeySelective(users);
        return users;
    }
}
