package com.xichuan.emos.controller;

import com.xichuan.emos.resp.CommonResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Api
public class TestController {
    @GetMapping("/sayHello")
    @ApiOperation("sayHello测试方法 http://192.168.1.118:8080/emos-wx-app/test/sayHello")
    public CommonResp sayHello(){
        return CommonResp.success().put("message","hello world");
    }
}
