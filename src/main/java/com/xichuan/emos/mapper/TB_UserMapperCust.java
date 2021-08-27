package com.xichuan.emos.mapper;


import java.util.HashMap;

public interface TB_UserMapperCust {
    public boolean haveRootUser();

    public int insert(HashMap param);

    public Integer searchIdByOpenId(String openId);

}