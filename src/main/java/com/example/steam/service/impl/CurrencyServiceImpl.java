package com.example.steam.service.impl;

import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpMethods;
import com.arronlong.httpclientutil.common.HttpResult;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.example.steam.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private static String url = "https://c.runoob.com/currencyconverter/CNY/TRY/";

    @Override
    public Double getCNY2TRYRate() {
        HttpConfig config = HttpConfig.custom().method(HttpMethods.GET).url(url).encoding("utf-8");
        HttpResult httpResult ;
        try {
            httpResult = HttpClientUtil.sendAndGetResp(config);
        } catch (HttpProcessException e) {
            throw new RuntimeException(e);
        }
        log.info("调用查询汇率接口/n{} {}" , httpResult.getResult().substring(0 , 200) , "剩余部分忽略");
        Document parse = Jsoup.parse(httpResult.getResult());
        Element selects = parse.getElementById("selectfromtime1");
        Elements TRY = selects.getElementsMatchingOwnText("土耳其里拉 TRY");
        if(TRY.size() != 1){
            log.error("获取土耳其人民币汇率失败，获取到的参数为{}" , TRY);
            return 0d;
        }
        Element element = TRY.get(0);
        String val = element.val();
        log.info("获取到土耳其里拉 TRY汇率{}" , Double.valueOf(val));
        return Double.valueOf(val);
    }
}
