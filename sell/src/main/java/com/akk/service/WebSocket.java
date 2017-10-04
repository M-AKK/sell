package com.akk.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Akk_Mac
 * Date: 2017/8/30 下午3:09
 */
@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session;

    //建立一个websocket的容器来存储这些session
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void opOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("【websocket消息】有新的连接，总数:{}", webSocketSet.size());
    }

    @OnClose
    public void onClose(Session session) {
        webSocketSet.remove(this);
        log.info("【websocket消息】连接断开，总数:{}", webSocketSet.size());
    }

    //是的，这个是收到消息，客户端发过来的，似乎没有
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
    }

    public void sendMessage(String message) {
        for(WebSocket webSocket : webSocketSet) {
            log.info("【websocket消息】广播消息， message={}", message);
            try {
                //向前端发送消息
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
