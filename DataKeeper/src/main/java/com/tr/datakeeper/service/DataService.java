package com.tr.datakeeper.service;

import com.tr.datakeeper.dto.DataDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DataService {

    DataDto saveDataQuote(DataDto data);

    List<DataDto> findByIsin(String isin);

    void deleteByIsin(String isin);

    Mono<Void> deleteById(String id);

    List<DataDto> findAll();

    void deleteAll();
}
