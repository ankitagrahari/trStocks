package com.tr.datakeeper.entity;

import com.tr.datakeeper.dto.DataDto;
import lombok.Data;

@Data
public class Stock {

    private String type;
    private DataDto dataDto;
}
