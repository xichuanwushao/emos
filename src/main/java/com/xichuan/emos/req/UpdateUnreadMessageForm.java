package com.xichuan.emos.req;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
//@Data
@ApiModel
public class UpdateUnreadMessageForm {
    @NotNull
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
