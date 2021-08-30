package com.xichuan.emos.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.xichuan.emos.config.SystemConstants;
import com.xichuan.emos.mapper.TbCheckinMapperCust;
import com.xichuan.emos.mapper.TbHolidaysMapperCust;
import com.xichuan.emos.mapper.TbWorkdayMapperCust;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
@Scope("prototype")
@Slf4j
public class CheckinServiceImpl implements CheckinService{

    @Autowired
    private SystemConstants constants;

    @Resource
    private TbHolidaysMapperCust holidaysMapperCust;

    @Resource
    private TbWorkdayMapperCust workdayMapperCust;

    @Resource
    private TbCheckinMapperCust checkinMapperCust;

    @Override
    public String validCanCheckIn(int userId, String date) {
        boolean bool_1 = holidaysMapperCust.searchTodayIsHolidays() != null ? true : false;
        boolean bool_2 = workdayMapperCust.searchTodayIsWorkday() != null ? true : false;
        String type = "工作日";
        if (DateUtil.date().isWeekend()) {
            type = "节假日";
        }
        if (bool_1) {
            type = "节假日";
        } else if (bool_2) {
            type = "工作日";
        }

        if (type.equals("节假日")) {
            return "节假日不需要考勤";
        } else {
            DateTime now = DateUtil.date();
            String start = DateUtil.today() + " " + constants.attendanceStartTime;
            String end = DateUtil.today() + " " + constants.attendanceEndTime;
            DateTime attendanceStart = DateUtil.parse(start);
            DateTime attendanceEnd = DateUtil.parse(end);
            if(now.isBefore(attendanceStart)){
                return "没到上班考勤开始时间";
            }
            else if(now.isAfter(attendanceEnd)){
                return "超过了上班考勤结束时间";
            }else {
                HashMap map=new HashMap();
                map.put("userId",userId);
                map.put("date",date);
                map.put("start",start);
                map.put("end",end);
                boolean bool=checkinMapperCust.haveCheckin(map)!=null?true:false;
                return bool?"今日已经考勤，不用重复考勤" : "可以考勤";
            }
        }
    }
}

