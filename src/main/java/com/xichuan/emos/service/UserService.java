package com.xichuan.emos.service;


import java.util.Set;

public interface UserService {
    public int registerUser(String registerCode, String code, String nickname, String photo) ;

    public Set<String> searchUserPermissions(int userId);
}
