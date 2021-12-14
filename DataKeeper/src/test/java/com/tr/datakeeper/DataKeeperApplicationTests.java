package com.tr.datakeeper;

import com.tr.datakeeper.dto.DataDto;
import com.tr.datakeeper.service.DataServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DataKeeperApplicationTests {

    @Autowired
    DataServiceImpl dataService;
    List<DataDto> list = new ArrayList<>();

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp(){
        dataService.deleteAll();
        DataDto data = new DataDto();
        data.setIsin("WX7880702802");
        data.setDescription("WECD");
        data.setPrice(9876.1123);
        data.setTimeMillis(System.currentTimeMillis());

        DataDto data1 = new DataDto();
        data1.setIsin("WX7880702802");
        data1.setDescription("ABDD");
        data1.setPrice(9776.1123);
        data1.setTimeMillis(System.currentTimeMillis());

        DataDto data2 = new DataDto();
        data2.setIsin("LD7232024125");
        data2.setDescription("QWER");
        data2.setPrice(96.123);
        data2.setTimeMillis(System.currentTimeMillis());

        list.add(data);list.add(data1);list.add(data2);

        dataService.saveDataQuotes(list);
    }

    @Test
    void testStoreDB(){

        List<DataDto> all = dataService.findAll();
        System.out.println("all.size:"+all.size());

        Assertions.assertEquals(all.size(), list.size());
    }

    @Test
    void findByIsin() {
        String isin = "WX7880702802";
        List<DataDto> quotes = dataService.findByIsin(isin);
        long count = list.stream().filter(a -> a.getIsin().equalsIgnoreCase(isin)).count();

        Assertions.assertEquals(quotes.size(), count);
    }

    @Test
    void deleteByIsin() {
        String isin = "LD7232024125";
        List<DataDto> dataSet = dataService.findByIsin(isin);
//        dataService.deleteById(id);
        List<DataDto> quotes = dataService.findByIsin(isin);

        Assertions.assertEquals(quotes.size(),0);
    }
}
