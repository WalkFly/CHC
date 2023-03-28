package com.example.steam.dto;

import lombok.Data;

import java.util.List;

@Data
public class HttpResultDto<T> {
    private String msg;
    private String sysDate;
    private String code;
    private List<T> data;
}
