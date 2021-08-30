package com.xichuan.emos.mapper;


import java.util.ArrayList;
import java.util.HashMap;

public interface TbWorkdayMapperCust {
    public Integer searchTodayIsWorkday();
    public ArrayList<String> searchWorkdayInRange(HashMap param);

}