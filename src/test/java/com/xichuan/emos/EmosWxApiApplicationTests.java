package com.xichuan.emos;

import cn.hutool.core.util.IdUtil;
import com.xichuan.emos.config.EmosWxApiApplication;
import com.xichuan.emos.domain.MessageEntity;
import com.xichuan.emos.domain.MessageRefEntity;
import com.xichuan.emos.service.MessageService;
import javafx.application.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest(classes = EmosWxApiApplication.class)
class EmosWxApiApplicationTests {

    @Resource
    private MessageService messageService;
    @Test
    void contextLoads() {
        for (int i = 1; i <= 10; i++) {
            MessageEntity message = new MessageEntity();
            message.setUuid(IdUtil.simpleUUID());
            message.setSenderId(0);
            message.setSenderName("系统消息");
            message.setMsg("这是第" + i + "条测试消息");
            message.setSendTime(new Date());
            String id=messageService.insertMessage(message);

            MessageRefEntity ref=new MessageRefEntity();
            ref.setMessageId(id);
            ref.setReceiverId(10); //接收人ID
            ref.setLastFlag(true);
            ref.setReadFlag(false);
            messageService.insertRef(ref);
        }
    }



}
