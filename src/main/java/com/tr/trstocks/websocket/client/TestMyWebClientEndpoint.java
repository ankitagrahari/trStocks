package com.tr.trstocks.websocket.client;

import javax.websocket.MessageHandler;
import java.net.URI;
import java.net.URISyntaxException;

public class TestMyWebClientEndpoint {

    public static void main(String[] args) {
        try {
//            MyWebSocketClientEndpoint instEndpoint = new MyWebSocketClientEndpoint(new URI("ws://localhost:8032/instruments"));
//            instEndpoint.onClose();
            MyWebSocketClientEndpoint quoteEndpoint = new MyWebSocketClientEndpoint(new URI("ws://localhost:8032/quotes"));
            Thread.sleep(2000);
//            quoteEndpoint.onClose();

//            endpoint.addMessageHandler(new MessageHandler(){});

//            endpoint.sendMessage("{'event':'add', 'name':'SendHello'}");
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
