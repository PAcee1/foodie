package com.pacee1.service.impl;

import com.pacee1.enums.Sex;
import com.pacee1.mapper.UsersMapper;
import com.pacee1.pojo.Users;
import com.pacee1.pojo.bo.UserBO;
import com.pacee1.service.UserService;
import com.pacee1.utils.DateUtil;
import com.pacee1.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author pace
 * @version v1.0
 * @Type UserServiceImpl.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private Sid sid;
    @Autowired
    private UsersMapper usersMapper;

    // 默认头像地址
    private static final String USER_FACE = "http://www.pacee1.com//img/avatar.png";

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUsernameIsExist(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users result = usersMapper.selectOne(users);
        return result == null ? false : true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users createUser(UserBO userBO) {
        Users users = new Users();
        // 设置各种属性

        // 使用SID生成id
        users.setId(sid.nextShort());

        users.setUsername(userBO.getUsername());
        // 密码md5加密
        try {
            users.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        users.setNickname(userBO.getUsername());
        users.setFace(USER_FACE);
        users.setBirthday(DateUtil.stringToDate("1970-01-01"));
        users.setSex(Sex.SECRET.type);

        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());

        // 保存
        usersMapper.insert(users);
        return users;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(String username, String password) {
        Users users = new Users();
        users.setUsername(username);
        try {
            users.setPassword(MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Users result = usersMapper.selectOne(users);
        return result;
    }

}
