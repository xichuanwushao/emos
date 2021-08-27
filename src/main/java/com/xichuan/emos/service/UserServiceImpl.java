package com.xichuan.emos.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xichuan.emos.exception.BusinessException;
import com.xichuan.emos.mapper.TB_UserMapperCust;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

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
}
