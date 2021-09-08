package com.xichuan.emos.controller;

import com.xichuan.emos.mail.MessageTask;
import com.xichuan.emos.req.LoginReq;
import com.xichuan.emos.req.RegisterReq;
import com.xichuan.emos.resp.CommonResp;
import com.xichuan.emos.service.MessageService;
import com.xichuan.emos.service.UserService;
import com.xichuan.emos.shiro.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
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

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageTask messageTask;

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

    @PostMapping("/login")
    @ApiOperation("登陆系统")
    public CommonResp login(@Valid @RequestBody LoginReq form){
        int id=userService.login(form.getCode());
        String token=jwtUtil.createToken(id);
        saveCacheToken(token,id);
        Set<String> permsSet = userService.searchUserPermissions(id);
        return CommonResp.success("登陆成功").put("token",token).put("permission",permsSet);
    }

    @GetMapping("/searchUserSummary")
    @ApiOperation("查询用户摘要信息")
    public CommonResp searchUserSummary(@RequestHeader("token") String token){
        int userId=jwtUtil.getUserId(token);
        HashMap map=userService.searchUserSummary(userId);
        return CommonResp.success().put("result",map);
    }

    @GetMapping("/refreshMessage")
    @ApiOperation("刷新用户消息")
    public CommonResp refreshMessage(@RequestHeader("token") String token){
        int userId=jwtUtil.getUserId(token);
        messageTask.receiveAsync(userId+"");
        long lastRows=messageService.searchLastCount(userId);
        long unreadRows=messageService.searchUnreadCount(userId);
        return CommonResp.success().put("lastRows",lastRows).put("unreadRows",unreadRows);
    }
}
