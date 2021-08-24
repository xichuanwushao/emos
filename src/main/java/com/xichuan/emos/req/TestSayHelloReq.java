package com.xichuan.emos.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TestSayHelloReq {
    @NotBlank
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,15}$",message = "不符合正则表达式")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
