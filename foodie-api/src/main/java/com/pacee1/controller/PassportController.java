package com.pacee1.controller;

import com.pacee1.pojo.Users;
import com.pacee1.pojo.bo.UserBO;
import com.pacee1.service.UserService;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author pace
 * @version v1.0
 * @Type PassportController.java
 * @Desc
 * @date 2020/5/17 15:20
 */
@RestController
@RequestMapping("passport")
@Api(value = "登录注册接口",tags = "用户登录注册的接口")
public class PassportController {

    @Autowired
    private UserService userService;

    @GetMapping("/usernameIsExist")
    @ApiOperation(value = "用户名校验是否存在",notes = "用户名校验是否存在")
    public ResponseResult usernameIsExist(@RequestParam String username){
        if(StringUtils.isBlank(username)){
            return ResponseResult.errorMsg("用户名不能为空");
        }

        boolean exist = userService.queryUsernameIsExist(username);
        if(exist){
            return ResponseResult.errorMsg("用户名已存在");
        }

        return ResponseResult.ok();
    }

    @PostMapping("/regist")
    @ApiOperation(value = "用户注册",notes = "用户注册")
    public ResponseResult regist(@RequestBody UserBO userBO){
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        // 1.校验
        if(StringUtils.isBlank(username) ||
        StringUtils.isBlank(password) ||
        StringUtils.isBlank(confirmPassword)){
            return ResponseResult.errorMsg("用户名或密码不能为空");
        }

        boolean exist = userService.queryUsernameIsExist(username);
        if(exist){
            return ResponseResult.errorMsg("用户名已存在");
        }

        if(password.length() < 6){
            return ResponseResult.errorMsg("密码不能小于6位");
        }

        if(!StringUtils.equals(password,confirmPassword)){
            return ResponseResult.errorMsg("两次密码不一致");
        }

        // 2.保存
        Users user = userService.createUser(userBO);

        return ResponseResult.ok(user);
    }
}
