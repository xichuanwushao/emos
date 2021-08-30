package com.xichuan.emos.controller;

import cn.hutool.core.date.DateUtil;
import com.xichuan.emos.resp.CommonResp;
import com.xichuan.emos.service.CheckinService;
import com.xichuan.emos.shiro.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/checkin")
@RestController
@Api("签到模块Web接口")
@Slf4j
public class CheckinController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CheckinService checkinService;

    @GetMapping("/validCanCheckIn")
    @ApiOperation("查看用户今天是否可以签到")
    public CommonResp validCanCheckIn(@RequestHeader("token") String token){
        int userId=jwtUtil.getUserId(token);
        String result=checkinService.validCanCheckIn(userId, DateUtil.today());
        return CommonResp.success(result);
    }
}
