package sustech.ooad.websocketserver.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import sustech.ooad.websocketserver.model.ChatContent;
import sustech.ooad.websocketserver.model.ChatListRecord;
import sustech.ooad.websocketserver.model.ChatUserDto;
import sustech.ooad.websocketserver.service.ChatService;
import sustech.ooad.websocketserver.util.Result;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/ws/chat")
@CrossOrigin
public class ChatController {

    @Resource
    ChatService chatService;


    @GetMapping("/users")
    public Result<?> getUserList(@RequestParam("uid") long uid) {
        List<ChatListRecord> records = chatService.findChatRecords(uid);
        return Result.ok(records);

    }

    @GetMapping("/user/list")
    public Result<?> getUserList(@RequestParam("name") String patten) {
        boolean availablePatten = chatService.checkPatten(patten);
        if (availablePatten) {
            List<ChatUserDto> chatUserDtos = chatService.findChatUser(patten);
            return Result.ok(chatUserDtos);
        } else {
            return Result.err(-1, "bad patten " + patten);
        }
    }


    @PostMapping("/with")
    public Result<?> chatWith(@RequestParam("uid") long uid,@RequestParam("with") long with) {
        Map<String, Object> initData = new HashMap<>();
        List<ChatContent> history = chatService.beginChat(uid, with);
        initData.put("history", history);
        return Result.ok(initData);
    }

    @PostMapping("/invite")
    public Result<?> inviteUser(@RequestParam("uid") long uid,@RequestParam("with") long with) {
        boolean valid = chatService.addChatUser(uid, with);
        if (valid) {
            return Result.ok(null);
        } else {
            return Result.err(-1, "bad uid " + uid);
        }

    }
}
