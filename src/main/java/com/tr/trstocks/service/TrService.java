package com.tr.trstocks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tr.trstocks.beans.CandleStick;
import com.tr.trstocks.beans.Data;
import com.tr.trstocks.beans.Stock;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TrService {

    //This will act as thread safe CACHE for this program to store all ISIN and price history.
    public Map<String, List<Data>> map = new ConcurrentHashMap<>();
    private final int LAST_30 = 30;


    public Map<String, List<Data>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<Data>> map) {
        this.map = map;
    }

    /**
     * It will call another webservice which will store the Quote data in a MongoDB based on type.
     * If type is Quote: It will add the record in database
     * if type is DELETE: it will remove the entries for that specific isin
     * @param text
     */
    public void sendPayloadToStore(String text){
        try {
            String POST_URL = "http://localhost:9001/store/db";
            HttpClient client = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost(POST_URL);
            postRequest.addHeader("content-type", "application/json");
            postRequest.setEntity(new StringEntity(text, ContentType.APPLICATION_JSON));

            HttpResponse response = client.execute(postRequest);
            if(response.getCode() == 200){
                System.out.println("Messaged Stored");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the List of Data objects for specific isin from the Database MongoDb
     * @param isin
     * @return
     */
    public List<Data> getStockFromDB(String isin){
        List<Data> dataList = new ArrayList<>();
        try {
            String GET_URL = "http://localhost:9001";
            WebClient client = WebClient.create(GET_URL);
            Flux<Data> dataFlux = client.get().uri("/data?isin="+isin).retrieve().bodyToFlux(Data.class);

            dataFlux.subscribe(a -> dataList.add(a));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * For Quote: I will get following text as output from the web socket
     * "data": {
     *     "price": 352.4568,
     *     "isin": "KV8363L66204"
     *   },
     *   "type": "QUOTE"
     *
     * For Instruments, I will get following text as output from the web socket
     * "data": {
     *     "description": "dicta deseruisse vel suas porro ex",
     *     "isin": "KV8363L66204"
     *   },
     *   "type": "ADD"/"DELETE"
     *
     * Based on type, the MAP is populated.
     * @param textData
     * @return Stock
     */
    public Stock getStockFromMemory(String textData){
        try {
            //Converting text to object
            Stock stock = new ObjectMapper().readValue(textData, Stock.class);
            if(null!=stock){
                String isin = stock.getData().getIsin();
                stock.getData().setTimeMillis(System.currentTimeMillis());
                //if found Quote, add the data to the list which will be on the next index
                if(stock.getType().equalsIgnoreCase("QUOTE")){
                    if(map.containsKey(isin)){
                        List<Data> temp = map.get(isin);
                        temp.add(stock.getData());
                        map.put(isin, temp);
                    }
                } else {
                    //if found Instruments, then based on ADD or DELETE, it will take action
                    if(stock.getType().equalsIgnoreCase("ADD")){
                        if(!map.containsKey(isin)){
                            map.put(isin, new ArrayList<>());
                        }
                    } else if(stock.getType().equalsIgnoreCase("DELETE")){
                        if(map.containsKey(isin)){
                            map.remove(isin);
                        }
                    }
                }
                return stock;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * From the list of data got from the history of given ISIN, it will prepare
     * the candlestick of 1 min and add it to the aggregated list.
     *
     * It will return the last 30 records from the list of Candlestick.
     * @param isin
     * @return List
     */
    public List<CandleStick> getCandleSticksFromMemory(String isin){
        //Step1: Prepare CandleStick for given isin
        //Step2: Collaborate these CandleSticks together for 30 min or 30 objects

        System.out.println("MAP.size:"+ map.size());
        List<Data> history = map.get(isin);
        List<CandleStick> candleStickList = getCandlesFromDataSet(history, isin);

        return candleStickList;
    }

    /**
     * Get the candlesticks objects from a new service which will store and fetch data from MonoDB
     * @param isin
     * @return
     */
    public List<CandleStick> getCandleSticksInDB(String isin){
        //Step1: Prepare CandleStick for given isin
        //Step2: Collaborate these CandleSticks together for 30 min or 30 objects

        List<Data> history = getStockFromDB(isin);
        List<CandleStick> candleStickList = getCandlesFromDataSet(history, isin);

        return candleStickList;
    }

    public List<CandleStick> getCandlesFromDataSet(List<Data> history, String isin){

        List<CandleStick> candleStickList = new ArrayList<>();
        long time = 59999l;

        //In case where the history of the instrument is less than 1 min, then it had to iterate over the entire history
        boolean found = false;
        Data first = history.get(0);
        Data tempForOpenPrice = new Data();
        double high=first.getPrice(), low=first.getPrice();
        for(int i=1; i<history.size(); i++){
            Data current = history.get(i);
            Data previous = history.get(i-1);
            if(time == 59999)
                tempForOpenPrice = current;

            time = time - (current.getTimeMillis() - previous.getTimeMillis());
            high = high<current.getPrice() ? current.getPrice() : high;
            low = low>current.getPrice() ? current.getPrice() : low;
            if(time<=0){
                time = 59999;
                found = true;
                CandleStick cs = new CandleStick();
                cs.setIsin(isin);
                cs.setOpenPrice(tempForOpenPrice.getPrice());
                cs.setClosePrice(current.getPrice());
                cs.setHighPrice(high);
                cs.setLowPrice(low);
                cs.setOpenTime(Instant.ofEpochMilli(tempForOpenPrice.getTimeMillis())
                        .atZone(ZoneId.systemDefault()).toLocalDateTime());
                cs.setCloseTime(Instant.ofEpochMilli(current.getTimeMillis())
                        .atZone(ZoneId.systemDefault()).toLocalDateTime());

                candleStickList.add(cs);
            }
        }

        if(!found){
            CandleStick cs = new CandleStick();
            cs.setIsin(isin);
            cs.setOpenPrice(first.getPrice());
            cs.setOpenTime(Instant.ofEpochMilli(first.getTimeMillis())
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
            cs.setClosePrice(history.get(history.size()-1).getPrice());
            cs.setCloseTime(Instant.ofEpochMilli(history.get(history.size()-1).getTimeMillis())
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
            cs.setHighPrice(high);
            cs.setLowPrice(low);
            candleStickList.add(cs);
        }

        if(candleStickList.size()>LAST_30){
            return candleStickList.subList(candleStickList.size()-LAST_30, candleStickList.size());
        } else {
            return candleStickList;
        }
    }
}
