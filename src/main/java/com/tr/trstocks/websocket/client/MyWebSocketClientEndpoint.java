package com.tr.trstocks.websocket.client;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class MyWebSocketClientEndpoint {

    Session session = null;
    private MessageHandler messageHandler;

    public MyWebSocketClientEndpoint(URI uri){
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, uri);
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session){
        System.out.println("On Open");
        this.session = session;
    }

    @OnClose
    public void onClose(){
        System.out.println("On Close");
        this.session = null;
    }

    @OnMessage
    public void onMessage(String message){
        System.out.println("On Message:"+ message);
    }

    public void addMessageHandler(MessageHandler handler){
        this.messageHandler = handler;
    }

    public void sendMessage(String message){
        this.session.getAsyncRemote().sendText(message);
    }
}
