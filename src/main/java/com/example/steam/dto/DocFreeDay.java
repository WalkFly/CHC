package com.example.steam.dto;

import lombok.Data;

@Data
public class DocFreeDay {
    private String bookingSettingId;
    private String hospitalId;
    private String hospitalClassId;
    private Long rankingDate;
    private String rankingId;
}
