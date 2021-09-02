package com.xichuan.emos.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@Scope("prototype")
public class EmailTask implements Serializable {
    @Resource
    private JavaMailSender javaMailSender;
    @Value("${emos.email.system}")
    private String mailbox;
    @Async
    public void sendAsync(SimpleMailMessage message){
        message.setFrom(mailbox);
        message.setCc(mailbox);
        javaMailSender.send(message);
    }
}
