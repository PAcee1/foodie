package com.pacee1.controller;

import com.pacee1.pojo.Users;
import com.pacee1.pojo.vo.UsersVO;
import com.pacee1.resource.FileResource;
import com.pacee1.service.FdfsService;
import com.pacee1.service.center.CenterUserService;
import com.pacee1.utils.CookieUtils;
import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.RedisOperator;
import com.pacee1.utils.ResponseResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-08-14 16:20
 **/
@RestController
@RequestMapping("fdfs")
public class FdfsController {

    @Autowired
    private FileResource fileResource;

    @Autowired
    private CenterUserService centerUserService;
    @Autowired
    private FdfsService fdfsService;
    @Autowired
    private RedisOperator redisOperator;

    @PostMapping("/uploadFace")
    @ApiOperation(value = "修改用户头像",notes = "修改用户头像接口")
    public ResponseResult uploadFace(
            @RequestParam String userId, MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) throws Exception{
        String path = null;

        // 开始文件上传
        if(file != null){
            // 文件名称
            String filename = file.getOriginalFilename();
            if(StringUtils.isNotBlank(filename)){
                // 文件重命名，face-userId.jpg
                String[] strings = filename.split("\\.");
                String suffix = strings[strings.length - 1];

                // 判断文件格式是否正确
                if(!suffix.toLowerCase().equals("png") &&
                        !suffix.toLowerCase().equals("jpg") &&
                        !suffix.toLowerCase().equals("jpeg")){
                    return ResponseResult.errorMsg("文件格式不正确");
                }

                // 上传图片到fdfs
                path = fdfsService.upload(file, suffix);
                System.out.println(path);
            }
        }else {
            return ResponseResult.errorMsg("头像不存在");
        }

        if(StringUtils.isNotBlank(path)){
            // 更新用户信息到数据库
            // 组装图片网络路径
            String faceUrl = fileResource.getHost() + path;
            Users users = centerUserService.updateUserFace(userId, faceUrl);

            // 清空用户敏感信息
            users = setUserNull(users);

            // 保存Session到Redis
            String userToken = UUID.randomUUID().toString().trim();
            redisOperator.set("redis_user_token:" + users.getId(),userToken);
            UsersVO usersVO = new UsersVO();
            BeanUtils.copyProperties(users,usersVO);
            usersVO.setUserUniqueToken(userToken);

            // 设置cookie
            CookieUtils.setCookie(request,response,"user",
                    JsonUtils.objectToJson(usersVO),true);
        }else {
            return ResponseResult.errorMsg("上传头像失败");
        }

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
