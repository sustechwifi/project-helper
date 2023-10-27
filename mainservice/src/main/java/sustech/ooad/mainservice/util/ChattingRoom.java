package sustech.ooad.mainservice.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sustech.ooad.mainservice.service.ChatService;
import sustech.ooad.mainservice.util.ws.ChatContent;
import sustech.ooad.mainservice.util.ws.ChatListRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static sustech.ooad.mainservice.util.ConstantField.PREFIX_CHAT_MAPPING;

@Slf4j
@Component
public class ChattingRoom extends TextWebSocketHandler {

    @Resource
    ChatService chatService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static Map<Long, WebSocketSession> sshMap = new ConcurrentHashMap<>();


    private static ChatContent publicContent(String message, long to){
        return new ChatContent(0,to,null,message,null, null, System.currentTimeMillis());
    }


    private static ChatContent updateChatList(List<ChatListRecord> records, long to){
        return new ChatContent(0,to,null,null,null, records,System.currentTimeMillis());
    }

    private void sendFailMessage(@NotNull WebSocketSession session,long to) throws Exception{
        var failMessage = publicContent("send fail",to);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(failMessage)));
    }

    private void sendFailMessage(@NotNull WebSocketSession session,String error) throws Exception{
        var failMessage = publicContent(error,-1);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(failMessage)));
    }


    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        var tmp = session.getHandshakeHeaders().getFirst(HttpHeaders.COOKIE);
        if (tmp == null){
            session.close();
            return;
        }
        var tmps = tmp.split("=");
        if (!tmps[0].equals("uid")){
            session.close();
            return;
        }
        long uid = Long.parseLong(tmps[1]);
        log.info(String.format("用户 %d 连接WebSocket",uid));
        sshMap.put(uid, session);
        try {
            List<ChatListRecord> chatRecords = chatService.findChatRecords(uid);
            var content = updateChatList(chatRecords,uid);
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(content)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage instanceof TextMessage){
            ChatContent content = objectMapper.readValue(webSocketMessage.getPayload().toString(), ChatContent.class);
            if (content.getType() == 1 || content.getType() == 2){
                if (content.getFrom() == null){
                    sendFailMessage(session,content.getTo());
                    return;
                }
                var from = content.getFrom().getId();
                String key = String.format("%s:%d",PREFIX_CHAT_MAPPING,from);
                String toUser = stringRedisTemplate.opsForValue().get(key);
                if (!Objects.equals(toUser, String.valueOf(content.getTo()))){
                    sendFailMessage(session,content.getTo());
                }else {
                    WebSocketSession webSocketSession = sshMap.get(content.getTo());
                    if (webSocketSession != null){
                        // 判断对面是否也在同一个窗口
                        String key2 = String.format("%s:%d",PREFIX_CHAT_MAPPING,content.getTo());
                        String toUser2 = stringRedisTemplate.opsForValue().get(key2);
                        if (Objects.equals(toUser2, String.valueOf(from))){
                            // 对面也在一起聊天
                            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(content)));
                        }
                        // 更新聊天列表
                        chatService.addOnlineMessage(content);
                        List<ChatListRecord> chatRecords = chatService.findChatRecords(content.getTo());
                        var newList = updateChatList(chatRecords,from);
                        webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(newList)));
                    }else {
                        chatService.addOfflineMessage(content);
                    }
                }
            }
        }else {
            sendFailMessage(session,"invalid message type");
            log.error("invalid message type: "+ webSocketMessage.getClass().getName());
        }
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) throws Exception {
        handleMessage(session,message);
    }

    @Override
    protected void handlePongMessage(@NotNull WebSocketSession session, @NotNull PongMessage message) throws Exception {
        sendFailMessage(session,"invalid message type");
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, Throwable exception) throws Exception {
        sendFailMessage(session,exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        var tmp = sshMap.entrySet().stream().filter(i -> i.getValue().equals(session)).findFirst();
        if (tmp.isPresent()){
            var uid = tmp.get().getKey();
            String key = String.format("%s:%d",PREFIX_CHAT_MAPPING,uid);
            stringRedisTemplate.delete(key);
            sshMap.remove(uid);
        }else {
            log.error("can not find the WebSocketSession");
        }
        super.afterConnectionClosed(session, status);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
