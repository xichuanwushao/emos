package com.xichuan.emos.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.xichuan.emos.exception.BusinessException;
import com.xichuan.emos.req.CheckinReq;
import com.xichuan.emos.resp.CommonResp;
import com.xichuan.emos.service.CheckinService;
import com.xichuan.emos.shiro.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

@RequestMapping("/checkin")
@RestController
@Api("签到模块Web接口")
@Slf4j
public class CheckinController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CheckinService checkinService;

    @Value("${emos.image-folder}")
    private String imageFolder;

    @GetMapping("/validCanCheckIn")
    @ApiOperation("查看用户今天是否可以签到")
    public CommonResp validCanCheckIn(@RequestHeader("token") String token){
        int userId=jwtUtil.getUserId(token);
        String result=checkinService.validCanCheckIn(userId, DateUtil.today());
        return CommonResp.success(result);
    }

    @PostMapping("/checkin")
    @ApiOperation("签到")
    public CommonResp checkin(@Valid CheckinReq form, @RequestParam("photo") MultipartFile file, @RequestHeader("token") String token){
        if(file==null){
            return CommonResp.error("没有上传文件");
        }
        int userId=jwtUtil.getUserId(token);
        String fileName=file.getOriginalFilename().toLowerCase();
        if(!fileName.endsWith(".jpg")){
            return CommonResp.error("必须提交JPG格式图片");
        }
        else{
            String path=imageFolder+"/"+fileName;
            try{
                file.transferTo(Paths.get(path));
                HashMap param=new HashMap();
                param.put("userId",userId);
                param.put("path",path);
                param.put("city",form.getCity());
                param.put("district",form.getDistrict());
                param.put("address",form.getAddress());
                param.put("country",form.getCountry());
                param.put("province",form.getProvince());
                checkinService.checkin(param);
                return CommonResp.success("签到成功");
            }catch ( IOException e){
                log.error(e.getMessage(),e);
                throw new BusinessException("图片保存错误");
            }
            finally {
                FileUtil.del(path);
            }

        }
    }
    @PostMapping("/createFaceModel")
    @ApiOperation("创建人脸模型")
    public CommonResp createFaceModel(@RequestParam("photo") MultipartFile file,@RequestHeader("token") String token){
        if(file==null){
            return CommonResp.error("没有上传文件");
        }
        int userId=jwtUtil.getUserId(token);
        String fileName=file.getOriginalFilename().toLowerCase();
        if(!fileName.endsWith(".jpg")){
            return CommonResp.error("必须提交JPG格式图片");
        }
        else{
            String path=imageFolder+"/"+fileName;
            try{
                file.transferTo(Paths.get(path));
                checkinService.createFaceModel(userId,path);
                return CommonResp.success("人脸建模成功");
            }catch (IOException e){
                log.error(e.getMessage(),e);
                throw new BusinessException("图片保存错误");
            }
            finally {
                FileUtil.del(path);
            }

        }
    }
}
