package sustech.ooad.mainservice.util.ws;

import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.util.ChattingRoom;

public class WebsocketChatServer implements ChatServer{
    @Override
    public ChattingRoom newChatting(AuthUser requester, AuthUser accepter) {
        return null;
    }
}
