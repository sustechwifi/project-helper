package sustech.ooad.mainservice.util.ws;


public record ChatListRecord(
        long withUser,
        String name,
        String avatar,
        long messageCnt,
        String lastMessage
) {
}
