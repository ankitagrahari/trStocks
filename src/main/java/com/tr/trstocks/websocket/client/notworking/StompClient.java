package com.tr.trstocks.websocket.client.notworking;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;

@Configuration
public class StompClient {

    private static final String URL = "ws://localhost:8032/";

    public void startStompClient(){

//        try {
//            WebSocketClient client = new StandardWebSocketClient();
//            List<Transport> transports = new ArrayList<>(1);
//            transports.add(new WebSocketTransport(client));
//            SockJsClient sockJsClient = new SockJsClient(transports);
//            WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
//            String url = "ws://localhost:9090/chat";
//            StompSessionHandler sessionHandler = new MyStompSessionHandler();
//
//            StompSession session = stompClient.connect(url, sessionHandler).get();
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//
//            while(true){
//                System.out.flush();
//                String line = in.readLine();
//                if ( line == null ) break;
//                if ( line.length() == 0 ) continue;
//                Stock stock = new Stock();
//                session.send("/app/chat/java", msg);
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect(URL, sessionHandler);
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("STOMP::"+sc.nextLine());
        }
    }
}
