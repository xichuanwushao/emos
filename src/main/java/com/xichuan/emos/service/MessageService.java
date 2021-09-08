package com.xichuan.emos.service;

import com.xichuan.emos.domain.MessageEntity;
import com.xichuan.emos.domain.MessageRefEntity;

import java.util.HashMap;
import java.util.List;

public interface MessageService {
    public String insertMessage(MessageEntity entity);
    public List<HashMap> searchMessageByPage(int userId, long start, int length);
    public HashMap searchMessageById(String id);
    public String insertRef(MessageRefEntity entity);
    public long searchUnreadCount(int userId);
    public long searchLastCount(int userId);
    public long updateUnreadMessage(String id);
    public long deleteMessageRefById(String id);
    public long deleteUserMessageRef(int userId);
}
