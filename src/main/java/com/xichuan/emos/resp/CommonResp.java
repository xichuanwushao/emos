package com.xichuan.emos.resp;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class CommonResp extends HashMap<String,Object> {
    public CommonResp(){
        put("code", HttpStatus.SC_OK);
        put("msg","success");
    }
    public CommonResp put(String key, Object value){
        super.put(key,value);
        return this;
    }
    public static CommonResp success(){
        return new CommonResp();
    }
    public static CommonResp success(String msg){
        CommonResp r=new CommonResp();
        r.put("msg",msg);
        return r;
    }
    public static CommonResp success(Map<String,Object> map){
        CommonResp r=new CommonResp();
        r.putAll(map);
        return r;
    }

    public static CommonResp error(int code, String msg){
        CommonResp r=new CommonResp();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }
    public static CommonResp error(String msg){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,msg);
    }
    public static CommonResp error(){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,"未知异常，请联系管理员");
    }
}
