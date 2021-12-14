package com.tr.datakeeper.service;

import com.tr.datakeeper.dto.DataDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataServiceImplTest {

    DataServiceImpl serviceImpl;

    DataServiceImplTest(){
        this.serviceImpl = new DataServiceImpl();
    }

    @Test
    void findAll() {
        List<DataDto> dataSet = serviceImpl.findAll();
        StepVerifier.create(Flux.just(dataSet))
//                .assertNext(r -> r.getIsin().equalsIgnoreCase("WX7880702802"))
//                .consumeNextWith(r -> { assertEquals("WX7880702802", r.getIsin()); })
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @Test
    void findByIsin() {
        List<DataDto> quotes = serviceImpl.findByIsin("WX7880702802");
        StepVerifier.create(Flux.just(quotes))
                .consumeNextWith(r -> { assertEquals("WX7880702802", r.get(0).getIsin()); })
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Disabled
    @Test
    void deleteByIsin() {
        serviceImpl.deleteById("LD7232024125");
        List<DataDto> quotes = serviceImpl.findByIsin("LD7232024125");
        StepVerifier.create(Flux.just(quotes))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

//    @BeforeEach
//    void setUp() {
//        DataDto data = new DataDto();
//        data.setIsin("WX7880702802");
//        data.setDescription("ABCD");
//        data.setPrice(9876.1123);
//        data.setTimeMillis(System.currentTimeMillis());
//        serviceImpl.saveDataQuote(data);
//
//        DataDto data1 = new DataDto();
//        data1.setIsin("WX7880702802");
//        data1.setDescription("ABCD");
//        data1.setPrice(9776.1123);
//        data1.setTimeMillis(System.currentTimeMillis());
//        serviceImpl.saveDataQuote(data1);
//
//        DataDto data2 = new DataDto();
//        data2.setIsin("LD7232024125");
//        data2.setDescription("QWER");
//        data2.setPrice(96.123);
//        data2.setTimeMillis(System.currentTimeMillis());
//        serviceImpl.saveDataQuote(data2);
//    }
//
//    @AfterEach
//    void tearDown(){
//        serviceImpl.deleteAll();
//    }
}
