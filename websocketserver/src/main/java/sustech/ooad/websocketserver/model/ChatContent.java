package sustech.ooad.websocketserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatContent{
    /**
     * type | 类型解释 | content内容 | 字段情况
     * -2：发送者自己发的信息      与发出的消息相同，仅用于发送方确认是否发送成功
     * -1：聊天列表信息           content = null; from = server; figure = null
     * 0: 系统消息,系统消息内容,   from = null; figure = null; records = null
     * 1: 正文，聊天文本,         figure = null;   records = null
     * 2: 正文，图片,            content = null;  records = null
     */
    int type;
    long to;
    ChatUserDto from;
    String content;
    String figure;
    List<ChatListRecord> records;
    long time;
}
