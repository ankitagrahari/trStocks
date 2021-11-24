package com.tr.datakeeper.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@lombok.Data
@Document(value = "Data")
public class Data {

    @Id
    private String id;
    private String isin;
    private String description;
    private double price;
    private long timeMillis;
}
