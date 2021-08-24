package com.xichuan.emos.controller;

import com.xichuan.emos.req.TestSayHelloReq;
import com.xichuan.emos.resp.CommonResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/test")
@Api
public class TestController {
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
}
