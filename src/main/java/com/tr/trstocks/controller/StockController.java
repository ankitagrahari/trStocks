package com.tr.trstocks.controller;

import com.tr.trstocks.beans.CandleStick;
import com.tr.trstocks.service.TrService;
import com.tr.trstocks.websocket.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class StockController {

    @Autowired
    TrService trService;

    /**
     * Start websockets on destination and receive data across instruments, which is stored
     * -In Memory
     * -In MongoDb
     */
    @GetMapping("/startInstruments")
    public void startWebSocketForInstruments(){
        try {
            Client client = new Client();
            client.startClient("ws://localhost:8032/instruments");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Start websockets on destination and receive data across quotes, which is stored
     * -In Memory
     * -In MongoDb
     */
    @GetMapping("/startQuotes")
    public void startWebSocketForQuotes(){
        try {
            Client client = new Client();
            client.startClient("ws://localhost:8032/quotes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the list of recent 30 candles from Memory
     * @param isin
     * @return
     */
    @GetMapping("/candlesticks")
    public List<CandleStick> getHistoryFromMemory(@RequestParam String isin){
        try {
            return trService.getCandleSticksFromMemory(isin);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the list of recent 30 candles from MongoDB
     * @param isin
     * @return
     */
    @GetMapping("/db/candlesticks")
    public List<CandleStick> getHistoryFromDB(@RequestParam String isin){
        try {
            return trService.getCandleSticksInDB(isin);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
