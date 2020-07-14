package com.pacee1.controller.interceptor;

import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.RedisOperator;
import com.pacee1.utils.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>用户登录拦截器</p>
 *
 * @author : Pace
 * @date : 2020-07-14 16:49
 **/
public class UserTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 请求前拦截方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");

        if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)){
            // 通过Redis获取
            String userUniqueToken = redisOperator.get("redis_user_token:" + userId);
            if(StringUtils.isNotBlank(userUniqueToken)){
                if(!userUniqueToken.equals(userToken)){
                    System.out.println("用户异地登录");
                    returnError(response,ResponseResult.errorMsg("用户异地登录"));
                    return false;
                }
            }else {
                System.out.println("用户未登录");
                returnError(response,ResponseResult.errorMsg("用户未登录"));
                return false;
            }
        }else {
            System.out.println("用户未登录");
            returnError(response,ResponseResult.errorMsg("用户未登录"));
            return false;
        }
        return true;
    }

    private void returnError(HttpServletResponse response,
                             ResponseResult responseResult){
        OutputStream os = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            os = response.getOutputStream();
            os.write(JsonUtils.objectToJson(responseResult).getBytes("utf-8"));
            os.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
