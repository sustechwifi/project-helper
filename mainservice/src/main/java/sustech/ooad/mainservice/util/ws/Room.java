package sustech.ooad.mainservice.util.ws;


import sustech.ooad.mainservice.model.AuthUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Room {
    private AuthUser requester;
    private AuthUser accepter;
    private final List<ChatContent> messages = new ArrayList<>();

    public List<ChatContent> getMessages() {
        return messages;
    }

    public void addMessage(ChatContent message){
        messages.add(message);
    }


}
