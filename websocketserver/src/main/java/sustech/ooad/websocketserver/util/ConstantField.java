package sustech.ooad.websocketserver.util;


import java.time.Duration;

public class ConstantField {

    // redis 键前缀
    public static final String EMAIL_LOGIN = "email:login:";
    public static final String EMAIL_AUTH_ROLE = "email:auth:role:";
    public static final String EMAIL_BIND_USER = "email:bind:user:";

    public static final String USER_COURSES = "course:courses:user";

    public static final String PREFIX_CHAT_USER_RECORD = "chat:userList:record";
    public static final String PREFIX_CHAT_OLD_MESSAGE = "chat:message:old";
    public static final String PREFIX_CHAT_NEW_MESSAGE = "chat:message:new";
    public static final String PREFIX_CHAT_MAPPING = "chat:pair";



    // 其他常数
    public static final Duration ONE_MINUTES = Duration.ofMinutes(1);
    public static final Duration FIVE_MINUTES = Duration.ofMinutes(5);
    public static final Duration FIFTEEN_MINUTES = Duration.ofMinutes(15);
    public static final Duration ONE_DAY = Duration.ofDays(1);


}
