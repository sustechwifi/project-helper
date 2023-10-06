package sustech.ooad.mainservice.model;

import com.example.mock.model.ChatContent;
import com.example.mock.model.RoomConfig;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Room {
    private final RoomConfig config;
    private final Map<String,String> users = new ConcurrentHashMap<>();
    private final List<ChatContent> messages = new ArrayList<>();

    private Map<String, WebSocketSession> sshMap = new ConcurrentHashMap<>();



    public Room(RoomConfig config){
        this.config = config;
    }

    public Set<String> getUsers() {
        return users.keySet();
    }

    public List<String> getUUIDs(){
        return new ArrayList<>(users.values());
    }

    public List<ChatContent> getMessages() {
        return messages;
    }

    public boolean judgePass(String user, String password) {
        return  (!this.config.isNeedPassword() || password.equals(this.config.getPassword()));
    }

    public void doRegister(String user,String uuid){
        users.put(user, uuid);
        config.setCurrentSize(config.getCurrentSize()+1);
    }

    public void addMessage(ChatContent message){
        messages.add(message);
    }

    public void removeUser(String user){
        users.remove(user);
    }



    public RoomConfig getConfig() {
        return config;
    }
}
