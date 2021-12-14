package com.tr.datakeeper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tr.datakeeper.dto.DataDto;
import com.tr.datakeeper.entity.Stock;
import com.tr.datakeeper.service.DataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class DataKeeperController {

    @Autowired
    private DataServiceImpl dataService;

    static Map<String, List<DataDto>> IN_MEMORY = new ConcurrentHashMap<>();

    @GetMapping("/greet")
    public String greet(){
        return "Hello";
    }

    @PostMapping("/store")
    public void storeData(@RequestBody String payload ){
        try {
            if (null != payload) {
                Stock stock = new ObjectMapper().readValue(payload, Stock.class);
                String isin = stock.getDataDto().getIsin();
                stock.getDataDto().setTimeMillis(System.currentTimeMillis());
                //if found Quote, add the data to the list which will be on the next index
                if (stock.getType().equalsIgnoreCase("QUOTE")) {
                    if (IN_MEMORY.containsKey(isin)) {
                        List<DataDto> temp = IN_MEMORY.get(isin);
                        temp.add(stock.getDataDto());
                        IN_MEMORY.put(isin, temp);
                    }
                } else {
                    //if found Instruments, then based on ADD or DELETE, it will take action
                    if (stock.getType().equalsIgnoreCase("ADD")) {
                        if (!IN_MEMORY.containsKey(isin)) {
                            IN_MEMORY.put(isin, new ArrayList<DataDto>());
                        }
                    } else if (stock.getType().equalsIgnoreCase("DELETE")) {
                        if (!IN_MEMORY.containsKey(isin)) {
                            IN_MEMORY.remove(isin);
                        }
                    }
                }
            }
            System.out.println("Memory Size:"+IN_MEMORY.size());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/store/db")
    public void storeInDB(@RequestBody Stock stock){
        if(null!=stock) {
            if (stock.getType().equalsIgnoreCase("QUOTE")) {
                dataService.saveDataQuote(stock.getDataDto());
            } else if (stock.getType().equalsIgnoreCase("DELETE")) {
                dataService.deleteByIsin(stock.getDataDto().getIsin());
            }
        } else {
            System.out.println("Store:Not having stock");
        }
    }

    @GetMapping("/data")
    @ResponseBody
    public List<DataDto> getHistoryFromDB(@RequestParam String isin){
        return dataService.findByIsin(isin);
    }

    @GetMapping("/db/all")
    @ResponseBody
    public List<DataDto> getEntireHistory(){
        return dataService.findAll();
    }

}
