package com.tr.datakeeper.service;

import com.tr.datakeeper.dto.DataDto;
import com.tr.datakeeper.entity.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataService {

    Mono<DataDto> createDataQuote(Mono<DataDto> data);

    Flux<Data> findByIsin(String isin);

    Mono<Void> deleteByIsin(String isin);

    Flux<Data> findAll();

    Mono<Void> deleteAll();
}
