package com.xichuan.emos.mapper;


import java.util.HashMap;
import java.util.Set;

public interface TB_UserMapperCust {
    public boolean haveRootUser();

    public int insert(HashMap param);

    public Integer searchIdByOpenId(String openId);

    public Set<String> searchUserPermissions(int userId);

}