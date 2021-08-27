package com.xichuan.emos.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xichuan.emos.req.TestSayHelloReq;
import com.xichuan.emos.resp.CommonResp;
import com.xichuan.emos.service.UserService;
import com.xichuan.emos.service.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/test")
@Api
public class TestController {
    @Autowired
    public UserServiceImpl userService;

    @GetMapping("/sayHello")
    @ApiOperation("sayHello测试方法")
    public CommonResp sayHello(){
        return CommonResp.success().put("message","hello world 5 ");
    }

    @PostMapping("/sayHello2")
    @ApiOperation("sayHello测试方法")
    public CommonResp sayHello2(@Valid @RequestBody TestSayHelloReq req){
        return CommonResp.success().put("message", "Hello, "+req.getName());
    }

    @GetMapping("/registerUser")
    public CommonResp sayHello3(){
        userService.getOpenId("20");
        return CommonResp.success().put("message","hello world 5 ");
    }



}
