package com.tr.datakeeper.service;

import com.tr.datakeeper.entity.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class DataServiceImplTest {

    DataServiceImpl serviceImpl;

    DataServiceImplTest(){
        this.serviceImpl = new DataServiceImpl();
    }

    @Test
    void findAll() {
        Flux<Data> dataSet = serviceImpl.findAll();
        StepVerifier.create(dataSet)
//                .assertNext(r -> r.getIsin().equalsIgnoreCase("WX7880702802"))
//                .consumeNextWith(r -> { assertEquals("WX7880702802", r.getIsin()); })
                .expectNextCount(4)
                .expectComplete()
                .verify();
    }

    @Test
    void findByIsin() {
        Flux<Data> quotes = serviceImpl.findByIsin("WX7880702802");
        StepVerifier.create(quotes)
                .consumeNextWith(r -> { assertEquals("WX7880702802", r.getIsin()); })
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    void deleteByIsin() {
        Mono<Void> quotes = serviceImpl.deleteByIsin("LD7232024125");
        StepVerifier.create(quotes)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    @BeforeEach
    void setUp() {
        Data data = new Data();
        data.setIsin("WX7880702802");
        data.setDescription("ABCD");
        data.setPrice(9876.1123);
        data.setTimeMillis(System.currentTimeMillis());
        serviceImpl.createDataQuote(data);

        Data data1 = new Data();
        data1.setIsin("WX7880702802");
        data1.setDescription("ABCD");
        data1.setPrice(9776.1123);
        data1.setTimeMillis(System.currentTimeMillis());
        serviceImpl.createDataQuote(data1);

        Data data2 = new Data();
        data2.setIsin("LD7232024125");
        data2.setDescription("QWER");
        data2.setPrice(96.123);
        data2.setTimeMillis(System.currentTimeMillis());
        serviceImpl.createDataQuote(data2);
    }

    @AfterEach
    void tearDown(){
        serviceImpl.deleteAll();
    }
}
