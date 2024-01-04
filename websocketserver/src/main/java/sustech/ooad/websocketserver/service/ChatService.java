package sustech.ooad.websocketserver.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import sustech.ooad.websocketserver.mapper.AuthUserMapper;
import sustech.ooad.websocketserver.model.ChatContent;
import sustech.ooad.websocketserver.model.ChatListRecord;
import sustech.ooad.websocketserver.model.ChatUserDto;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static sustech.ooad.websocketserver.util.ConstantField.*;


@Slf4j
@Service
public class ChatService {


    @Autowired
    RedisTemplate<String, ChatContent> chatRedisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource
    AuthUserMapper authUserMapper;

    private final static int DEFAULT_SIZE = 10;

    // 获取已读信息和未读信息中最近的一条消息
    private void getLatestMessage(List<ChatContent> buff, long uid1, long uid2, String prefix) {
        String key = String.format("%s:%d->%d", prefix, uid1, uid2);
        var t = chatRedisTemplate.opsForList().index(key,0);
        if (t != null) {
            buff.add(t);
        }
    }


    // 获取未读信息数目
    private long getNewContentCnt(long uid, long fromUser) {
        String key = String.format("%s:%d->%d", PREFIX_CHAT_NEW_MESSAGE, fromUser, uid);
        var res = chatRedisTemplate.opsForList().size(key);
        if (res == null || res == 0) {
            return 0;
        } else {
            return res;
        }
    }

    // 将未读信息转为以读信息
    private void age(long uid,long fromUser){
        var newMessageCnt = getNewContentCnt(uid, fromUser);
        if (newMessageCnt == 0){
            return;
        }
        String key1 = String.format("%s:%d->%d", PREFIX_CHAT_NEW_MESSAGE, fromUser, uid);
        var res = chatRedisTemplate.opsForList().leftPop(key1,newMessageCnt);
        if (res != null){
            String key2 = String.format("%s:%d->%d", PREFIX_CHAT_OLD_MESSAGE, fromUser, uid);
            chatRedisTemplate.opsForList().leftPushAll(key2,res);
        }
    }

    // 将历史消息添加到 List<ChatContent> result 中
    private void addHistory(List<ChatContent> result, long from, long to, String prefix) {
        var key = String.format("%s:%d->%d", prefix, from, to);
        var size = chatRedisTemplate.opsForList().size(key);
        if (size != null && size > 0) {
            var t = chatRedisTemplate.opsForList().range(key, 0, size < DEFAULT_SIZE ? size : DEFAULT_SIZE);
            if (t != null) {
                result.addAll(t);
            }
        }
    }

    // 获取历史记录，最多获取 limit 条
    private List<ChatContent> getHistory(long uid, long withUser, int limit) {
        List<ChatContent> res = new ArrayList<>();
        addHistory(res, withUser, uid, PREFIX_CHAT_OLD_MESSAGE);
        addHistory(res, withUser, uid, PREFIX_CHAT_NEW_MESSAGE);
        addHistory(res, uid, withUser, PREFIX_CHAT_OLD_MESSAGE);
        addHistory(res, uid, withUser, PREFIX_CHAT_NEW_MESSAGE);
        return res.stream()
                .sorted(Comparator.comparingLong(ChatContent::getTime).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // 添加消息到 已读/未读消息中
    private void addMessage(ChatContent content, String prefix) {
        String key = String.format("%s:%d->%d", prefix, content.getFrom().getId(), content.getTo());
        chatRedisTemplate.opsForList().leftPush(key, content);
        stringRedisTemplate.expire(key,Duration.ofHours(1));

        // 检查收信人的聊天列表中是否有发信人，若没有则添加
        var key2 = String.format("%s:%d", PREFIX_CHAT_USER_RECORD, content.getTo());
        var t = stringRedisTemplate.opsForList().indexOf(key2, String.valueOf(content.getFrom().getId()));
        stringRedisTemplate.expire(key2,Duration.ofHours(1));
        if (t == null) {
            addChatUser(content.getTo(), content.getFrom().getId());
        }
    }

    public void addOnlineMessage(ChatContent content) {
        addMessage(content, PREFIX_CHAT_OLD_MESSAGE);
    }

    public void addOfflineMessage(ChatContent content) {
        addMessage(content, PREFIX_CHAT_NEW_MESSAGE);
    }

    // 获取最新的一条消息
    private ChatContent latestMessage(long uid, long withUser) {
        List<ChatContent> tmp = new ArrayList<>();
        getLatestMessage(tmp, uid, withUser, PREFIX_CHAT_NEW_MESSAGE);
        getLatestMessage(tmp, uid, withUser, PREFIX_CHAT_OLD_MESSAGE);
        getLatestMessage(tmp, withUser, uid, PREFIX_CHAT_NEW_MESSAGE);
        getLatestMessage(tmp, withUser, uid, PREFIX_CHAT_OLD_MESSAGE);
        var res = tmp.stream().max(Comparator.comparingLong(ChatContent::getTime));
        return res.orElse(null);
    }

    // 获取聊天列表
    public List<ChatListRecord> findChatRecords(long uid) {
        var key = String.format("%s:%d", PREFIX_CHAT_USER_RECORD, uid);
        var size = chatRedisTemplate.opsForList().size(key);
        var res = new ArrayList<ChatListRecord>();
        if (size == null) {
            return res;
        }

        // 返回用户 id
        List<String> range = stringRedisTemplate.opsForList().range(key, 0, size);
        if (range == null) {
            return res;
        }
        for (String o : range) {
            try {
                long withUid = Long.parseLong(o);
                var withUser = findChatUserById(withUid);
                if (withUser == null) {
                    System.out.println("查不到此好友 uid = "+withUid);
                    continue;
                }
                System.out.printf("查找 uid = %d 的聊天好友 %s\n",uid, withUser);
                var msg = latestMessage(uid, withUid);
                if (msg == null){
                    System.out.printf("找不到 %d -> %d 的聊天信息\n",uid, withUid);
                    continue;
                }
                var cnt = getNewContentCnt(uid, withUid);
                System.out.printf("%d -> %d 的聊天信息数目为 %d\n",uid, withUid,cnt);
                res.add(new ChatListRecord(
                        withUser.getId(),
                        withUser.getName(),
                        withUser.getAvatar(),
                        cnt,
                        msg.getContent(),
                        msg.getTime())
                );

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        System.out.println(res);
        return res;
    }

    public boolean checkPatten(String patten) {
        String p = "%" + patten + "%";
        return authUserMapper.checkPatten(p);
    }

    public List<ChatUserDto> findChatUser(String patten) {
        String p = "%" + patten + "%";
        return authUserMapper.getChatUserByPatten(p);
    }

    public ChatUserDto findChatUserById(long uid) {
        ChatUserDto res = authUserMapper.getChatUserDtoById(uid);
        if (res == null) {
            log.error("no found uid:" + uid);
        }
        return res;
    }


    public List<ChatContent> beginChat(long uid, long withUser) {
        String key = String.format("%s:%d", PREFIX_CHAT_MAPPING, uid);
        stringRedisTemplate.opsForValue().set(key, String.valueOf(withUser), Duration.ofHours(1));
        var res = getHistory(uid, withUser, DEFAULT_SIZE);
        age(uid,withUser);
        return res;
    }

    public boolean addChatUser(long uid, long with) {
        var key = String.format("%s:%d", PREFIX_CHAT_USER_RECORD, uid);
        var t = stringRedisTemplate.hasKey(key);
        if (t == null || !t){
            stringRedisTemplate.expire(key,Duration.ofHours(1));
            stringRedisTemplate.opsForList().leftPush(key, String.valueOf(with));
            return true;
        } else if (stringRedisTemplate.opsForList().indexOf(key, String.valueOf(with)) == null) {
            stringRedisTemplate.expire(key,Duration.ofHours(1));
            stringRedisTemplate.opsForList().leftPush(key, String.valueOf(with));
            return true;
        } else {
            return false;
        }
    }
}
