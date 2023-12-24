package sustech.ooad.websocketserver.model;


public record ChatListRecord(
        long withUser,
        String name,
        String avatar,
        long messageCnt,
        String lastMessage,
        long lastMessageTime
) {
}
