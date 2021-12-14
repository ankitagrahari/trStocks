package com.tr.datakeeper.service;

import com.tr.datakeeper.dao.DataRepository;
import com.tr.datakeeper.dto.DataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DataServiceImpl implements DataService{

    @Autowired
    DataRepository dataRepo;

    public DataDto saveDataQuote(DataDto data) {
        AtomicReference<DataDto> dtoSaved = new AtomicReference<>();
        dataRepo.save(data).log().subscribe(
                a -> dtoSaved.set(a),
                b -> System.out.println("On Error:"+b.getMessage()),
                () -> System.out.println(data.getIsin()+" saved onComplete")
        );
        return dtoSaved.get();
    }

    public String saveDataQuotes(List<DataDto> dataSet){
        AtomicReference<String> error = new AtomicReference<>();
        dataRepo.saveAll(dataSet).subscribe(
                a -> System.out.println("On Next:"+a.getIsin()),
                b -> error.set(b.getMessage()),
                () -> System.out.println("On Complete")
        );

        return error.get();
    }

    @Override
    public List<DataDto> findByIsin(String isin) {
        return dataRepo.findByIsin(isin).collectList().block();
    }

    @Override
    public void deleteByIsin(String isin) {
//        return dataRepo.
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return dataRepo.deleteById(id);
//                .subscribe(
//                next -> System.out.println("on next delete"),
//                error -> System.out.println(error.getMessage()),
//                () -> System.out.println("Delete Completed Successfully")
//        );
    }

    @Override
    public List<DataDto> findAll(){
        List<DataDto> allL = new ArrayList<>();
        dataRepo.findAll()
                .doOnNext(a -> allL.add(a))
                .then()
                .block();
        return allL;
    }

    @Override
    public void deleteAll(){
        dataRepo.deleteAll().subscribe(
                a -> System.out.println("On Next:"),
                b -> System.out.println("On Error:"+b.getMessage()),
                () -> System.out.println("On Complete")
        );
    }
}
