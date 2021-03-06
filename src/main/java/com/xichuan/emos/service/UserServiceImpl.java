package com.xichuan.emos.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xichuan.emos.domain.MessageEntity;
import com.xichuan.emos.domain.TbUser;
import com.xichuan.emos.exception.BusinessException;
import com.xichuan.emos.mail.MessageTask;
import com.xichuan.emos.mapper.TB_UserMapperCust;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@Service
@Slf4j
@Scope("prototype")
public class UserServiceImpl implements UserService{

    @Value("${wx.app-id}")
    private String appId;
    @Value("${wx.app-secret}")
    private String appSecret;

    @Resource
    private TB_UserMapperCust tb_userMapperCust;

    @Autowired
    private MessageTask messageTask;

    public String getOpenId(String code){
        String url="https://api.weixin.qq.com/sns/jscode2session";
        HashMap map=new HashMap();
        map.put("appid", appId);
        map.put("secret", appSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String response= HttpUtil.post(url,map);
        JSONObject json= JSONUtil.parseObj(response);
        String openId=json.getStr("openid");
        if(openId==null||openId.length()==0){
            throw new RuntimeException("临时登陆凭证错误");
        }
        return openId;
    }

    public int registerUser(String registerCode, String code, String nickname, String photo) {
        if(registerCode.equals("000000")){
            boolean bool=tb_userMapperCust.haveRootUser();
            if(!bool){
                String openId=getOpenId(code);
                HashMap param=new HashMap();
                param.put("openId", openId);
                param.put("nickname", nickname);
                param.put("photo", photo);
                param.put("role", "[0]");
                param.put("status", 1);
                param.put("createTime", new Date());
                param.put("root", true);
                tb_userMapperCust.insert(param);
                int id=tb_userMapperCust.searchIdByOpenId(openId);

                MessageEntity entity=new MessageEntity();
                entity.setSenderId(0);
                entity.setSenderName("系统消息");
                entity.setUuid(IdUtil.simpleUUID());
                entity.setMsg("欢迎您注册成为超级管理员，请及时更新你的员工个人信息。");
                entity.setSendTime(new Date());
                messageTask.sendAsync(id+"",entity);
                return id;
            }
            else{
                throw new BusinessException("无法绑定超级管理员账号");
            }
        }
        else{

        }
        return 0;
    }

    @Override
    public Set<String> searchUserPermissions(int userId) {
        Set<String> permissions=tb_userMapperCust.searchUserPermissions(userId);
        return permissions;
    }

    @Override
    public Integer login(String code) {
        String openId=getOpenId(code);
        Integer userId=tb_userMapperCust.searchIdByOpenId(openId);
        if(userId==null){
            throw new BusinessException("帐户不存在");
        }
        messageTask.receiveAsync(userId+"");
        return userId;
    }


    @Override
    public TbUser searchById(int userId) {
        TbUser user=tb_userMapperCust.searchById(userId);

        return user;
    }

    @Override
    public String searchUserHiredate(int userId) {
        String hiredate=tb_userMapperCust.searchUserHiredate(userId);
        return hiredate;
    }

    @Override
    public HashMap searchUserSummary(int userId) {
        HashMap map=tb_userMapperCust.searchUserSummary(userId);
        return map;
    }
}
