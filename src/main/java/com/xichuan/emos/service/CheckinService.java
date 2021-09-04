package com.xichuan.emos.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface CheckinService {
    public String validCanCheckIn(int userId,String date);
    public void checkin(HashMap param);
    public void createFaceModel(int userId,String path);

    /***
     *
     * @param userId
     * @return
     */
    public HashMap searchTodayCheckin(int userId);

    /***
     *
     * @param userId
     * @return
     */
    public long searchCheckinDays(int userId);

    /***
     *
     * @param param
     * @return
     */
    public ArrayList<HashMap> searchWeekCheckin(HashMap param);
}
