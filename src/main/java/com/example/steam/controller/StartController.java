package com.example.steam.controller;
import com.example.steam.service.CHCService;
import com.example.steam.service.CurrencyService;
import com.example.steam.service.impl.CHCServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

@RestController
@Slf4j
public class StartController {


    public static Semaphore semaphore = new Semaphore(10 ,true);

    @Autowired
    private CHCService chcService;

    @RequestMapping(value = "start" , method = RequestMethod.GET)
    public String startProject(@RequestBody String date){
        while (true){
            if(CHCServiceImpl.hourIdSet.size() == 0){
                log.info("#######################################################################################################\n##############################\n");
                gogogo(date);
            }
        }
    }

    private void gogogo(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        String dateStr = "2024/03/22";

        Date startDate;
        try {
            startDate = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            log.error("转化时间出错，转化的时间原始输入为{}" , dateStr);
            throw new RuntimeException(e);
        }
        long time = startDate.getTime();
        //获取某个医生下面的空闲时间
        while(CHCServiceImpl.rankingId == null){

            chcService.getDoctorFreeDayTime(time);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for(int i =0; i<5 ; i++){
            chcService.getDoctorFreeHourTime(CHCServiceImpl.bookSettingId);
        }



        while (true){
            if(CHCServiceImpl.hourIdSet.size() > 0){
                for(String settingId : CHCServiceImpl.hourIdSet){
                    //修改这里的配置
                    chcService.yuYUE("2996" , "林小碧(儿保)" ,CHCServiceImpl.rankingId, settingId , dateStr);
//                    chcService.yuYUE("3037" , "黄杰(儿内)" ,CHCServiceImpl.rankingId, settingId , dateStr);
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
