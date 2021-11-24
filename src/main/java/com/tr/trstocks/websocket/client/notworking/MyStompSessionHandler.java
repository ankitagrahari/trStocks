package com.tr.trstocks.websocket.client.notworking;

import com.tr.trstocks.beans.Stock;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders){
        session.subscribe("/instruments", this);
//        session.subscribe("/quotes", this);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Stock stock = (Stock) payload;
        System.out.println("Received : " + stock.getType());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("Got an exception:"+ exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Stock.class;
    }
}
