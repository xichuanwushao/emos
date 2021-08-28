package com.xichuan.emos.req;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class LoginReq {
    @NotBlank(message = "临时授权不能为空")
    private String code;
}
