package sustech.ooad.mainservice.util.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sustech.ooad.mainservice.model.dto.ChatUserDto;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatContent{
    /**
     * type | 类型解释 | content内容 | 字段情况
     * 0: 系统消息,系统消息内容,   from = null; figure = null; records = null
     * 1: 正文，聊天文本,         figure = null;   records = null
     * 2: 正文，图片,            content = null;  records = null
     * 3：聊天列表, 列表信息       content = null; from = null; figure = null
     */
    int type;
    long to;
    ChatUserDto from;
    String content;
    String figure;
    List<ChatListRecord> records;
    long time;

}
