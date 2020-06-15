package com.pacee1.controller.center;

import com.pacee1.pojo.Users;
import com.pacee1.pojo.bo.center.CenterUserBO;
import com.pacee1.service.center.CenterUserService;
import com.pacee1.utils.CookieUtils;
import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Created by pace
 * @Date 2020/6/15 15:36
 * @Classname CenterController
 */
@RestController
@RequestMapping("userInfo")
@Api(value = "用户相关接口",tags = "用户相关接口")
public class CenterUserController {

    @Autowired
    private CenterUserService centerUserService;

    @PostMapping("/update")
    @ApiOperation(value = "修改用户信息",notes = "修改用户信息接口")
    public ResponseResult update(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response){
        if(userId == null){
            return ResponseResult.errorMsg("用户不存在");
        }

        // 校验传入的用户信息是否正确，使用Hibernate的Validate
        if(bindingResult.hasErrors()){
            Map<String,Object> errorMap = getErrorMap(bindingResult);
            return ResponseResult.errorMap(errorMap);
        }

        Users users = centerUserService.updateUserInfo(userId,centerUserBO);

        // 设置cookie 清空用户敏感信息
        users = setUserNull(users);
        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(users),true);

        return ResponseResult.ok();
    }

    /**
     * 获取校验出的错误信息封装
     * @param bindingResult
     * @return
     */
    private Map<String, Object> getErrorMap(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String,Object> errorMap = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            // 错误字段
            String field = fieldError.getField();
            // 错误信息
            String defaultMessage = fieldError.getDefaultMessage();
            errorMap.put(field,defaultMessage);
        }
        return errorMap;
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
