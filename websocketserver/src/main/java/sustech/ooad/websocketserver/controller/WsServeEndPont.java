package sustech.ooad.websocketserver.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import sustech.ooad.websocketserver.config.WebSocketConfig;
import sustech.ooad.websocketserver.model.ChatContent;
import sustech.ooad.websocketserver.model.ChatListRecord;
import sustech.ooad.websocketserver.model.ChatUserDto;
import sustech.ooad.websocketserver.service.ChatService;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static sustech.ooad.websocketserver.util.ConstantField.PREFIX_CHAT_MAPPING;


/**
 * 监听websocket地址
 */
@ServerEndpoint(value = "/ws/chat", subprotocols = {"protocol"}, configurator = WebSocketConfig.class)
@Component("chatServer")
@Slf4j
@CrossOrigin
public class WsServeEndPont {

    // 线程安全的Map,用来保存session, static用来确保是属于类的而不是对象的
    private static Map<Long, Session> sessionMap = new ConcurrentHashMap<>();

    private static StringRedisTemplate stringRedisTemplate;

    private static ChatService chatService;

    @Autowired
    void setService(ChatService chatService, StringRedisTemplate stringRedisTemplate) {
        WsServeEndPont.chatService = chatService;
        WsServeEndPont.stringRedisTemplate = stringRedisTemplate;
    }

    private static ChatContent publicContent(String message, long to) {
        return new ChatContent(0, to, null, message, null, null, System.currentTimeMillis());
    }

    ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    private static ChatContent updateChatList(List<ChatListRecord> records, long to) {
        return new ChatContent(-1, to, new ChatUserDto(0, "server", null), null, null, records, System.currentTimeMillis());
    }

    private void sendEchoMessage(Session session, ChatContent chatContent) throws Exception {
        var echo = new ChatContent(
                -1,
                chatContent.getTo(),
                chatContent.getFrom(),
                chatContent.getContent(),
                chatContent.getFigure(),
                chatContent.getRecords(),
                chatContent.getTime()
                );
        session.getBasicRemote().sendText(objectMapper.writeValueAsString(echo));
    }

    private void sendFailMessage(Session session, long to) throws Exception {
        var failMessage = publicContent("send fail", to);
        session.getBasicRemote().sendText(objectMapper.writeValueAsString(failMessage));
    }

    private void sendFailMessage(Session session, String error) throws Exception {
        var failMessage = publicContent(error, -1);
        session.getBasicRemote().sendText(objectMapper.writeValueAsString(failMessage));
    }

    // 连接建立时执行的操作
    @OnOpen
    public void OnOpen(Session session) {
        long uid = Long.parseLong(session.getRequestParameterMap().get("uid").get(0));
        log.info(String.format("用户 %d 连接WebSocket", uid));
        sessionMap.put(uid, session);
        try {
            List<ChatListRecord> chatRecords = chatService.findChatRecords(uid);
            var content = updateChatList(chatRecords, uid);
            session.getBasicRemote().sendText(objectMapper.writeValueAsString(content));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 收到客户端消息执行的操作
    @OnMessage
    public void onMessage(String text) throws Exception {
        ChatContent content = objectMapper.readValue(text, ChatContent.class);
        System.out.println(content);
        if (content.getType() == 1 || content.getType() == 2) {
            if (content.getFrom() == null) {
                return;
            }
            var fromSession = sessionMap.get(content.getFrom().getId());
            if (fromSession == null){
                return;
            }
            var from = content.getFrom().getId();
            String key = String.format("%s:%d", PREFIX_CHAT_MAPPING, from);
            String toUser = stringRedisTemplate.opsForValue().get(key);
            if (!Objects.equals(toUser, String.valueOf(content.getTo()))) {
                sendFailMessage(fromSession, content.getFrom().getId());
            } else {
                // 将此消息发回各发送者
                sendEchoMessage(fromSession,content);
                Session webSocketSession = sessionMap.get(content.getTo());
                if (webSocketSession != null) {
                    // 判断对面在綫
                    String key2 = String.format("%s:%d", PREFIX_CHAT_MAPPING, content.getTo());
                    String toUser2 = stringRedisTemplate.opsForValue().get(key2);
                    if (Objects.equals(toUser2, String.valueOf(from))) {
                        // 对面也在一起聊天
                        webSocketSession.getBasicRemote().sendText(objectMapper.writeValueAsString(content));
                        chatService.addOnlineMessage(content);
                    }else {
                        // 對面在綫，但是不是在同一個窗口
                        chatService.addOfflineMessage(content);
                    }
                    List<ChatListRecord> chatRecords = chatService.findChatRecords(content.getTo());
                    var newList = updateChatList(chatRecords, from);
                    webSocketSession.getBasicRemote().sendText(objectMapper.writeValueAsString(newList));
                } else {
                    chatService.addOfflineMessage(content);
                }
            }
        }
    }

    // 连接关闭时执行的操作
    @OnClose
    public void OnClose(Session session) {
        sessionMap.remove(session.getId());
        log.info("websocket is close");
    }


}