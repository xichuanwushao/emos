package com.xichuan.emos.controller;

import com.xichuan.emos.mail.MessageTask;
import com.xichuan.emos.req.DeleteMessageRefByIdForm;
import com.xichuan.emos.req.SearchMessageByIdForm;
import com.xichuan.emos.req.SearchMessageByPageForm;
import com.xichuan.emos.req.UpdateUnreadMessageForm;
import com.xichuan.emos.resp.CommonResp;
import com.xichuan.emos.service.MessageService;
import com.xichuan.emos.shiro.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/message")
@Api("消息模块网络接口")
public class MessageController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageTask messageTask;
    @PostMapping("/searchMessageByPage")
    @ApiOperation("获取分页消息列表")
    public CommonResp searchMessageByPage(@Valid @RequestBody SearchMessageByPageForm form, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        int page = form.getPage();
        int length = form.getLength();
        long start = (page - 1) * length;
        List<HashMap> list = messageService.searchMessageByPage(userId, start, length);
        return CommonResp.success().put("result", list);
    }

    @PostMapping("/searchMessageById")
    @ApiOperation("根据ID查询消息")
    public CommonResp searchMessageById(@Valid @RequestBody SearchMessageByIdForm form) {
        HashMap map = messageService.searchMessageById(form.getId());
        return CommonResp.success().put("result", map);
    }

    @PostMapping("/updateUnreadMessage")
    @ApiOperation("未读消息更新成已读消息")
    public CommonResp updateUnreadMessage(@Valid @RequestBody UpdateUnreadMessageForm form) {
        long rows = messageService.updateUnreadMessage(form.getId());
        return CommonResp.success().put("result", rows == 1 ? true : false);
    }

    @PostMapping("/deleteMessageRefById")
    @ApiOperation("删除消息")
    public CommonResp deleteMessageRefById(@Valid @RequestBody DeleteMessageRefByIdForm form) {
        long rows = messageService.deleteMessageRefById(form.getId());
        return CommonResp.success().put("result", rows == 1 ? true : false);
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
