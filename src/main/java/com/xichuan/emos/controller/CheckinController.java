package com.xichuan.emos.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.xichuan.emos.config.SystemConstants;
import com.xichuan.emos.exception.BusinessException;
import com.xichuan.emos.req.CheckinReq;
import com.xichuan.emos.req.SearchMonthCheckinReq;
import com.xichuan.emos.resp.CommonResp;
import com.xichuan.emos.service.CheckinService;
import com.xichuan.emos.service.UserService;
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
import java.util.ArrayList;
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

    @Autowired
    private UserService userService;

    @Value("${emos.image-folder}")
    private String imageFolder;

    @Autowired
    private SystemConstants constants;

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

    @GetMapping("/searchTodayCheckin")
    @ApiOperation("查询用户当日签到数据")
    public CommonResp searchTodayCheckin(@RequestHeader("token") String token){
        int userId=jwtUtil.getUserId(token);
        HashMap map=checkinService.searchTodayCheckin(userId);
        map.put("attendanceTime",constants.attendanceTime);
        map.put("closingTime",constants.closingTime);
        long days=checkinService.searchCheckinDays(userId);
        map.put("checkinDays",days);

        DateTime hiredate=DateUtil.parse(userService.searchUserHiredate(userId));
        DateTime startDate=DateUtil.beginOfWeek(DateUtil.date());
        if(startDate.isBefore(hiredate)){
            startDate=hiredate;
        }
        DateTime endDate=DateUtil.endOfWeek(DateUtil.date());
        HashMap param=new HashMap();
        param.put("startDate",startDate.toString());
        param.put("endDate",endDate.toString());
        param.put("userId",userId);
        ArrayList<HashMap> list=checkinService.searchWeekCheckin(param);
        map.put("weekCheckin",list);
        return CommonResp.success().put("result",map);
    }
    @PostMapping("/searchMonthCheckin")
    @ApiOperation("查询用户某月签到数据")
    public CommonResp searchMonthCheckin(@Valid @RequestBody SearchMonthCheckinReq form, @RequestHeader("token") String token){
        int userId=jwtUtil.getUserId(token);
        DateTime hiredate=DateUtil.parse(userService.searchUserHiredate(userId));//查询员工入职日期 //入职日期是String转换成Date类型
        String month=form.getMonth()<10?"0"+form.getMonth():form.getMonth().toString();//把月份数字变成字符串 //变成2位
        DateTime startDate=DateUtil.parse(form.getYear()+"-"+month+"-01");//构建某年某月第一天
        if(startDate.isBefore(DateUtil.beginOfMonth(hiredate))){//和入职日期作比较 //是不是在入职日期之前
            throw new BusinessException("只能查询考勤之后日期的数据");
        }
        if(startDate.isBefore(hiredate)){//查询考勤月份和入职月份是同一个月
            startDate=hiredate;
        }
        DateTime endDate=DateUtil.endOfMonth(startDate);//当月最后一天
        HashMap param=new HashMap();
        param.put("userId",userId);
        param.put("startDate",startDate.toString());
        param.put("endDate",endDate.toString());
        ArrayList<HashMap> list=checkinService.searchMonthCheckin(param);//返回考勤结果
        int sum_1=0,sum_2=0,sum_3=0;
        for(HashMap<String,String> one:list){
            String type=one.get("type");
            String status=one.get("status");
            if("工作日".equals(type)){
                if("正常".equals(status)){
                    sum_1++;
                }
                else if("迟到".equals(status)){
                    sum_2++;
                }
                else if("缺勤".equals(status)){
                    sum_3++;
                }
            }
        }
        return CommonResp.success().put("list",list).put("sum_1",sum_1).put("sum_2",sum_2).put("sum_3",sum_3);
    }


}
