package com.tr.datakeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDto {

    private String id;
    private String isin;
    private String description;
    private double price;
    private long timeMillis;

}
