package com.xichuan.emos.mapper;


import java.util.ArrayList;
import java.util.HashMap;

public interface TbHolidaysMapperCust {
    public Integer searchTodayIsHolidays();
    public ArrayList<String> searchHolidaysInRange(HashMap param);

}