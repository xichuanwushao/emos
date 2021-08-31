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
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
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
    @ApiOperation("测试方法")
    public CommonResp registerUser(){
        userService.getOpenId("20");
        return CommonResp.success().put("message","hello world 5 ");
    }
    @GetMapping("/searchUserPermissions")
    @ApiOperation("测试方法")
    public CommonResp searchUserPermissions(int id){
        return CommonResp.success().put("message",userService.searchUserPermissions(id));
    }


    @PostMapping("/addUser")
    @ApiOperation("添加用户测试用户权限")
    @RequiresPermissions(value = {"ROOT","USER:ADD"},logical = Logical.OR)
    public CommonResp addUser(){
        return CommonResp.success("用户添加成功");
    }


}
