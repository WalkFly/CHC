package com.example.steam.service;

import com.example.steam.dto.DocFreeDay;
import com.example.steam.dto.DocFreeHour;

import java.util.List;

public interface CHCService {

    /**
     * 主线流程
     */
    void gogogogo();

    /**
     * 获取医生下面空余的时间(那天有空)
     */
    List<DocFreeDay> getDoctorFreeDayTime(Long date);

    /**
     * 获取医生下面空余的时间(小时)
     */
    List<DocFreeHour> getDoctorFreeHourTime(String dayId);

    void yuYUE(String doctorId , String doctorName , String rankId , String bookingSettingTimeListId , String bookingDate);
}
