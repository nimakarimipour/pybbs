package co.yiiu.pybbs.config.websocket;

import co.yiiu.pybbs.model.vo.UserWithWebSocketVO;
import co.yiiu.pybbs.service.INotificationService;
import co.yiiu.pybbs.service.impl.NotificationService;
import co.yiiu.pybbs.util.Message;
import co.yiiu.pybbs.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@ServerEndpoint(value = "/websocket", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
@Component
public class MyWebSocket {

    private final static Logger log = LoggerFactory.getLogger(MyWebSocket.class);

    //aaaa
    private final static AtomicInteger online = new AtomicInteger(0);
    //aaaaa，aaaa
    public static Map<Session, UserWithWebSocketVO> webSockets = new ConcurrentHashMap<>();

    //aaaa
    @OnOpen
    public void onOpen(Session session) {
        online.incrementAndGet();
        webSockets.put(session, new UserWithWebSocketVO());
    }

    //aaaa
    @OnClose
    public void onClose(Session session) {
        online.decrementAndGet();
        webSockets.remove(session);
    }

    //aaaaaaaa
    @OnMessage
    public void onMessage(Message message, Session session) {
        if (message != null) {
            switch (message.getType()) {
                case "bind":
                    bind(message, session);
                    break;
                case "notReadCount":
                    fetchNotReadCount(message, session);
                    break;
                default:
                    break;
            }
        }
    }

    // aaaaaawebsocketaa
    private void bind(Message message, Session session) {
        try {
            Integer userId = Integer.parseInt(((Map) (message.getPayload())).get("userId").toString());
            String username = ((Map) (message.getPayload())).get("username").toString();
            UserWithWebSocketVO userWithWebSocketVO = webSockets.get(session);
            userWithWebSocketVO.setUserId(userId);
            userWithWebSocketVO.setUsername(username);
            webSockets.put(session, userWithWebSocketVO);
            session.getBasicRemote().sendObject(new Message("bind", null));
        } catch (IOException | EncodeException e) {
            log.error("aawsaaaa, aaaa: {}", e.getMessage());
        }
    }

    // aaaaaaaaaa
    private static void fetchNotReadCount(Message message, Session session) {
        try {
            INotificationService notificationService = SpringContextUtil.getBean(NotificationService.class);
            long countNotRead = notificationService.countNotRead(MyWebSocket.webSockets.get(session).getUserId());
            session.getBasicRemote().sendObject(new Message("notification_notread", countNotRead));
        } catch (IOException | EncodeException e) {
            log.error("aawsaaaa, aaaa: {}", e.getMessage());
        }
    }

    // aaaaaaaaaaaaidaasession
    private static Session selectSessionByUserId(Integer userId) {
        return webSockets.entrySet().stream().filter(x -> x.getValue().getUserId().equals(userId)).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    public static void emit(Integer userId, Message message) {
        try {
            Session session = selectSessionByUserId(userId);
            if (session != null) session.getBasicRemote().sendObject(message);
        } catch (IOException | EncodeException e) {
            log.error("aawsaaaa, aaaa：{}", e.getMessage());
        }
    }

}
