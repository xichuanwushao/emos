package com.xichuan.emos.mapper;


import com.xichuan.emos.domain.TbCheckin;

import java.util.ArrayList;
import java.util.HashMap;

public interface TbCheckinMapperCust {
    public Integer haveCheckin(HashMap param);
    public void insert(TbCheckin checkin);

    /***
     * 查询今天签到的结果 既可以查询出来员工的基本信息 也可以查询出来员工当天的考情结果
     * 使用左外连接
     * @param userId
     * @return
     */
    public HashMap searchTodayCheckin(int userId);

    /***
     * 统计用户总的签到天数 使用汇总函数查询
     * @param userId
     * @return
     */
    public long searchCheckinDays(int userId);

    /***
     * 查询本周员工的考勤情况 比如员工周三周四周五签到 那么默认周一周二旷工
     * 排除周一周二是特殊节假日 如周六周日上班 周一周二放假
     * 需要到节假日表查询 排定是不是特殊节假日
     * @param param
     * @return
     */
    public ArrayList<HashMap> searchWeekCheckin(HashMap param);

}