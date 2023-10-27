package sustech.ooad.mainservice.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import sustech.ooad.mainservice.model.dto.ChatUserDto;
import sustech.ooad.mainservice.service.ChatService;
import sustech.ooad.mainservice.util.Result;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;
import sustech.ooad.mainservice.util.ws.ChatContent;
import sustech.ooad.mainservice.util.ws.ChatListRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static sustech.ooad.mainservice.util.ConstantField.BAD_PARAMETER;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    ChatService chatService;

    @Resource
    AuthFunctionality authFunctionality;

    @GetMapping("/users")
    public Result<?> getUserList() {
        var curr = authFunctionality.getUser().getId();
        List<ChatListRecord> records = chatService.findChatRecords(curr);
        return Result.ok(records);

    }

    @GetMapping("/user/list")
    public Result<?> getUserList(@RequestParam("name") String patten) {
        boolean availablePatten = chatService.checkPatten(patten);
        if (availablePatten) {
            List<ChatUserDto> chatUserDtos = chatService.findChatUser(patten);
            return Result.ok(chatUserDtos);
        } else {
            return Result.err(BAD_PARAMETER, "bad patten " + patten);
        }
    }


    @PostMapping("/with")
    public Result<?> chatWith(@RequestParam("uid") long uid) {
        Map<String, Object> initData = new HashMap<>();
        var curr = authFunctionality.getUser().getId();
        var validPartner = chatService.beforeBegin(curr,uid);
        if (validPartner) {
            List<ChatContent> history = chatService.beginChat(curr, uid);
            initData.put("history", history);
            return Result.ok(initData);
        }else {
            return Result.err(BAD_PARAMETER, "the user is not in your chat list");
        }
    }

    @PostMapping("/invite")
    public Result<?> inviteUser(@RequestParam("uid") long uid) {
        var curr = authFunctionality.getUser().getId();
        boolean valid = chatService.addChatUser(curr, uid);
        if (valid) {
            return Result.ok(null);
        } else {
            return Result.err(BAD_PARAMETER, "bad uid " + uid);
        }

    }
}
