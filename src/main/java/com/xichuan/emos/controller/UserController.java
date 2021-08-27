package com.xichuan.emos.controller;

import com.xichuan.emos.req.RegisterReq;
import com.xichuan.emos.resp.CommonResp;
import com.xichuan.emos.service.UserService;
import com.xichuan.emos.shiro.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Api("用户模块Web接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${emos.jwt.cache-expire}")
    private int cacheExpire;

    @PostMapping("/register")
    @ApiOperation("注册用户")
    public CommonResp register(@Valid @RequestBody RegisterReq form){
        int id=userService.registerUser(form.getRegisterCode(),form.getCode(),form.getNickname(),form.getPhoto());
        String token=jwtUtil.createToken(id);
        Set<String> permsSet=userService.searchUserPermissions(id);
        saveCacheToken(token,id);
        return CommonResp.success("用户注册成功").put("token",token).put("permission",permsSet);
    }
    private void saveCacheToken(String token,int userId){
        redisTemplate.opsForValue().set(token,userId+"",cacheExpire, TimeUnit.DAYS);
    }
}
