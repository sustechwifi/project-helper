package sustech.ooad.mainservice.util.ws;

import sustech.ooad.mainservice.model.AuthUser;

public interface ChatServer {

    ChattingRoom newChatting(AuthUser requester, AuthUser accepter);



}
