package com.tr.datakeeper.service;

import com.tr.datakeeper.dto.DataDto;
import com.tr.datakeeper.entity.Data;
import com.tr.datakeeper.dao.DataRepository;
import com.tr.datakeeper.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DataServiceImpl implements DataService{

    @Autowired
    DataRepository dataRepo;

    @Override
    public Mono<DataDto> createDataQuote(Mono<DataDto> dataDto) {
        System.out.println("Creating data");
        return dataDto.map(ConvertUtils::dtoToEntity).flatMap(dataRepo::insert).map(ConvertUtils::entityToDto);
    }

    @Override
    public Flux<Data> findByIsin(String isin) {
        return dataRepo.findByIsin(isin);
    }

    @Override
    public Mono<Void> deleteByIsin(String isin) {
        return dataRepo.deleteByIsin(isin);
    }

    @Override
    public Flux<Data> findAll(){
        return dataRepo.findAll();
    }

    @Override
    public Mono<Void> deleteAll(){
        return dataRepo.deleteAll();
    }
}
