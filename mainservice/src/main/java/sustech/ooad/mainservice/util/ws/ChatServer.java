package sustech.ooad.mainservice.util.ws;

import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.util.ChattingRoom;


public interface ChatServer {

    ChattingRoom newChatting(AuthUser requester, AuthUser accepter);



}
