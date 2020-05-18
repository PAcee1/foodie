package com.pacee1.controller;

import com.pacee1.pojo.Users;
import com.pacee1.pojo.bo.UserBO;
import com.pacee1.service.UserService;
import com.pacee1.utils.CookieUtils;
import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ResponseResult regist(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
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

        // 3.设置cookie
        // 首先清空用户敏感信息
        user = setUserNull(user);
        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(user),true);

        return ResponseResult.ok(user);
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录",notes = "用户登录")
    public ResponseResult login(@RequestBody UserBO userBO,
                                HttpServletRequest request,
                                HttpServletResponse response){
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 1.校验
        if(StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)){
            return ResponseResult.errorMsg("用户名或密码不能为空");
        }

        // 2.登录
        Users user = userService.queryUserForLogin(username,password);
        if(user == null){
            return  ResponseResult.errorMsg("用户名或密码错误");
        }

        // 3.设置cookie
        // 首先清空用户敏感信息
        user = setUserNull(user);
        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(user),true);

        return ResponseResult.ok(user);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出登录",notes = "用户退出登录")
    public ResponseResult logout(@RequestParam String userId,
                                HttpServletRequest request,
                                HttpServletResponse response){

        // 1.清除cookie
        CookieUtils.deleteCookie(request,response,"user");

        // TODO 清除购物车，清除分布式会话

        return ResponseResult.ok();
    }

    private Users setUserNull(Users user) {
        user.setPassword(null);
        user.setBirthday(null);
        user.setCreatedTime(null);
        user.setEmail(null);
        user.setUpdatedTime(null);
        return user;
    }
}
