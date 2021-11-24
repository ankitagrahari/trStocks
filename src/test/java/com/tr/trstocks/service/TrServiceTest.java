package com.tr.trstocks.service;

import com.tr.trstocks.beans.CandleStick;
import com.tr.trstocks.beans.Data;
import com.tr.trstocks.beans.Stock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TrServiceInMemoryTest {

    TrService service;

    TrServiceInMemoryTest(){
        this.service = new TrService();
    }

    @Test
    void getStockAdd() {
        String textData = "{\n" +
                "  \"data\": {\n" +
                "    \"description\": \"alienum nibh ignota\",\n" +
                "    \"isin\": \"LD7232024125\"\n" +
                "  },\n" +
                "  \"type\": \"ADD\"\n" +
                "}";

        service.getStockFromMemory(textData);
        assertEquals(service.getMap().get("LD7232024125").size(), 0);
    }

    @Test
    void getStockQuote(){
        //Adding the Instrument
        String textData = "{\n" +
                "  \"data\": {\n" +
                "    \"description\": \"alienum nibh ignota\",\n" +
                "    \"isin\": \"LD7232024125\"\n" +
                "  },\n" +
                "  \"type\": \"ADD\"\n" +
                "}";

        service.getStockFromMemory(textData);

        //Adding Quote for that Instrument
        String textData1 = "{\n" +
                "  \"data\": {\n" +
                "    \"price\": 118.987,\n" +
                "    \"isin\": \"LD7232024125\"\n" +
                "  },\n" +
                "  \"type\": \"QUOTE\"\n" +
                "}";
        service.getStockFromMemory(textData1);
        assertEquals(service.getMap().get("LD7232024125").size(), 1);
    }

    @Test
    void getStockDelete(){
        //Adding the Instrument
        String textData = "{\n" +
                "  \"data\": {\n" +
                "    \"description\": \"alienum nibh ignota\",\n" +
                "    \"isin\": \"LD7232024125\"\n" +
                "  },\n" +
                "  \"type\": \"ADD\"\n" +
                "}";

        service.getStockFromMemory(textData);

        //Deleting the Instrument
        String textData1 = "{\n" +
                "  \"data\": {\n" +
                "    \"description\": \"alienum nibh ignota\",\n" +
                "    \"isin\": \"LD7232024125\"\n" +
                "  },\n" +
                "  \"type\": \"DELETE\"\n" +
                "}";

        service.getStockFromMemory(textData1);
        assertEquals(service.getMap().containsKey("LD7232024125"), false);
    }


    @Test
    void getCandleSticks() {
        Map<String, List<Data>> map = new ConcurrentHashMap<>();
        service.setMap(map);
        List<Data> dataSet = new CopyOnWriteArrayList<>();
        System.out.println(System.currentTimeMillis());
        dataSet.add(new Data("FH0283513764", "ABCD", 118.765, 1637476092183l));
        dataSet.add(new Data("FH0283513764", "ABCD", 113.465, 1637476102183l));
        dataSet.add(new Data("FH0283513764", "ABCD", 110.700, 1637476112183l));
        dataSet.add(new Data("FH0283513764", "ABCD", 108.645, 1637476212183l));
        dataSet.add(new Data("FH0283513764", "ABCD", 112.465, 1637476392183l));
        dataSet.add(new Data("FH0283513764", "ABCD", 115.365, 1637476703183l));
        dataSet.add(new Data("FH0283513764", "ABCD", 117.795, 1637476792183l));
        dataSet.add(new Data("FH0283513764", "ABCD", 122.769, 1637476892183l));
        dataSet.add(new Data("FH0283513764", "ABCD", 120.739, 1637476992183l));

        map.put("FH0283513764", dataSet);
        List<CandleStick> candles = service.getCandleSticksFromMemory("FH0283513764");
        candles.stream().forEach(c -> System.out.println(c.toString()));

        //Because we are only giving 9 candles info which will generate around 6 1min candles.
        assertEquals(candles.size(), 1);
    }

    @BeforeEach
    void setUp() {
        Map<String, List<Data>> map = new ConcurrentHashMap<>();
        service.setMap(map);
    }

    @AfterEach
    void tearDown() {
    }
}
