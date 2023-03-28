package com.example.steam.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.common.HttpMethods;
import com.arronlong.httpclientutil.common.HttpResult;
import com.example.steam.dto.DocFreeDay;
import com.example.steam.dto.DocFreeHour;
import com.example.steam.dto.HttpResultDto;
import com.example.steam.service.CHCService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

@Slf4j
@Service
public class CHCServiceImpl implements CHCService {

    public static Semaphore semaphore = new Semaphore(1,true);

    //获取到排班日期数据存储位
    public static String rankingId = null;

    public static String bookSettingId = null;

    //获取到当天排班日期班次标志位
    public static Set<String> hourIdSet = new HashSet<>();

    public static boolean success = false;



    //固定的医生列表，可以不用动态获取
    private Integer[] doctorId = new Integer[]{2996 , 2998, 4265};

    //儿童保健
    private Integer ertongbaojian = 11653;

    //儿童心理
    private Integer xinli = 11654;



    private Header[] headers = HttpHeader.custom().cookie("brandId=10000225; ljhyToken=662e29bc-a05b-4840-a538-70cea20f7da9; " +
            "SERVERID=412ec2af2c17b9b7a9e56383f9b2c908|1679983699|1679983692").build();





    @Override
    public void gogogogo() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = "2023-03-31";
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
            List<DocFreeDay> doctorFreeDayTime = getDoctorFreeDayTime(time);
            log.info(JSON.toJSONString(doctorFreeDayTime));
        }

        List<DocFreeHour> doctorFreeHourTime = getDoctorFreeHourTime(CHCServiceImpl.rankingId);
        log.info(JSON.toJSONString(doctorFreeHourTime));

        //获取到id后进行预约
//        yuYUE();

    }

    @Override
    public  List<DocFreeDay> getDoctorFreeDayTime( Long date) {
        HttpConfig config = HttpConfig.custom().headers(headers).method(HttpMethods.GET).url(docFreeDayUrlByDocId(2996 , ertongbaojian)).encoding("utf-8");
        HttpResult httpResult ;
        try {
            if(CHCServiceImpl.rankingId == null){
                httpResult = HttpClientUtil.sendAndGetResp(config);
                log.info("查询到排班(日期){}" , httpResult.getResult());
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
           log.info("请求出错");
           return null;
        }

        HttpResultDto<DocFreeDay> resultDto = JSON.parseObject(httpResult.getResult() , new TypeReference<HttpResultDto<DocFreeDay>>(){});
        //这里获取时间如果获取到则修改
        DocFreeDay vaildDocFreeDay = getVaildDocFreeDay(resultDto.getData(), date);
        if(vaildDocFreeDay != null){
            log.info("获取到对应日期数据{}" , vaildDocFreeDay.getRankingId());
            CHCServiceImpl.rankingId = vaildDocFreeDay.getRankingId();
            CHCServiceImpl.bookSettingId = vaildDocFreeDay.getBookingSettingId();
        }
        return resultDto.getData();
    }

    @Override
    @Async("taskExecutorFR")
    public List<DocFreeHour> getDoctorFreeHourTime(String dayId) {
        String url = getHourTimeUrlByDayId(dayId);
        HttpConfig config = HttpConfig.custom().headers(headers).method(HttpMethods.GET).url(url).encoding("utf-8");
        HttpResult httpResult ;
        while(true){
            try {
                log.info("获取班次中");
                httpResult = HttpClientUtil.sendAndGetResp(config);
            } catch (Exception e) {
                log.info("请求出错");
                continue;
            }
            HttpResultDto<DocFreeHour> resultDto = JSON.parseObject(httpResult.getResult() , new TypeReference<HttpResultDto<DocFreeHour>>(){});
            List<DocFreeHour> data = resultDto.getData();
            getVaildDocFreeHour(data);
            log.info("获取到合法排班班次小时{}" , JSON.toJSONString(CHCServiceImpl.hourIdSet));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Async("taskExecutorSE")
    public void yuYUE(String doctorId , String doctorName , String rankId , String bookingSettingTimeListId , String bookingDate) {
        Map map = new HashMap();
        map.put("brandId" , 10000225);
        map.put("billId" , "15858513258");
        map.put("birthdayDate" , "2021/03/03");
        map.put("privCode" , "330000");
        map.put("cityCode" , "330300");
        map.put("countyCode" , "330302");
        map.put("address" , "1号");
        map.put("idCard" , "");
        map.put("userId" , 1216875);
        map.put("userName" , "蔡弋礼");
        map.put("userSex" , 1);
        map.put("hospitalId" , 10001244);
        map.put("source" , 1);
        map.put("registeredType" , 1);
        map.put("timeSlot" , 1);
        map.put("outpatientTypeId" , 452);
        map.put("outpatientTypeName" , "儿童健康门诊");
        map.put("registeredFee" , "0");
        map.put("describe" , "0");
        map.put("departId" , 11653);

        map.put("doctorId" , doctorId);
        map.put("doctorName" , doctorName);
        map.put("rankId" , rankId);
        map.put("bookingDate" , bookingDate);
        map.put("bookingSettingTimeListId" , bookingSettingTimeListId);

        HttpConfig config = HttpConfig.custom().headers(headers).method(HttpMethods.POST)
                .url("https://m.ruolinzs.com/wechat/order/regBooking").map(map)
                .encoding("utf-8");
        HttpResult httpResult ;
        try {
            httpResult = HttpClientUtil.sendAndGetResp(config);
            log.info("开始抢购{}{}" ,bookingSettingTimeListId , httpResult.getResult());
        } catch (Exception e) {
            log.info("请求出错{}" , e.getMessage());
        }
    }


    //获取医生下面时间段地址
    public static String docFreeDayUrlByDocId(Integer docId  , Integer itemId){
        return "https://m.ruolinzs.com/wechat/user/depart/10001244/"+itemId+"/" + docId + "?startDate=2023%2F03%2F24&endDate=2023%2F09%2F24";
    }

    //获取当天时间段
    public static String getHourTimeUrlByDayId(String dayId){
        return  "https://m.ruolinzs.com/wechat/user/depart/new/10001244/bookSettingId?bookSettingId="+dayId+"&startDate=2023%2F03%2F24&endDate=2023%2F09%2F24";
    }

    //获取合法的时间日期
    public static DocFreeDay getVaildDocFreeDay(List<DocFreeDay> docFreeDayList , Long date){
        if(null == docFreeDayList){
            return null;
        }
        for(DocFreeDay docFreeDay : docFreeDayList){
            if(docFreeDay.getRankingDate() >= date){
                return docFreeDay;
            }
        }
        return null;
    }

    //获取合法时间小时
    public static DocFreeDay getVaildDocFreeHour(List<DocFreeHour> docFreeHours){
        if(null == docFreeHours){
            return null;
        }
        for(DocFreeHour docFreeHour : docFreeHours){
            if(docFreeHour.getState() == 1){
                CHCServiceImpl.hourIdSet.add(docFreeHour.getBookingSettingTimeListId());
            }
        }
        return null;
    }
}
