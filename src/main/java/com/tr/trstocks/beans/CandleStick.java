package com.tr.trstocks.beans;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CandleStick {

    private String isin;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private double openPrice;
    private double closePrice;
    private double highPrice;
    private double lowPrice;
}
