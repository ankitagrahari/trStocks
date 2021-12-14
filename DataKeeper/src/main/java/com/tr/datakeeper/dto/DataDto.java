package com.tr.datakeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "Data")
public class DataDto {

    @Id
    private Long id;
    private String isin;
    private String description;
    private double price;
    private long timeMillis;

}
