package sustech.ooad.mainservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ConstantField {
    //相应状态码
    public static final int ACCESS_DENIED = 101;
    public static final int AUTH_ROLE_DENIED = 102;
    public static final int AUTH_SESSION_DENIED = 103;
    public static final int ACCESS_COURSE_DENIED = 151;
    public static final int COURSE_INVALID_AUTHORITY = 152;
    public static final int CAPTCHA_ERR = 201;
    public static final int REPEATED_USERNAME = 202;
    public static final int REPEATED_EMAIL = 203;
    public static final int REGISTER_ERR = 203;
    public static final int UPLOAD_ERROR = 301;

    public static final int BAD_PARAMETER = 400;


    // 角色
    public static final String ROLE_DEFAULT = "UNKNOWN";
    public static final String ROLE_STUDENT = "ROLE_STUDENT";
    public static final String ROLE_TEACHER = "ROLE_TEACHER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_CHECK = "hasAnyRole('ROLE_ADMIN','ROLE_STUDENT','ROLE_TEACHER')";
    public static final String ROLE_CHECK_TEACHER = "hasAnyRole('ROLE_ADMIN','ROLE_TEACHER')";

    // 权限
    public static final String AUTHORITY_DEFAULT = "unknown";
    public static final String AUTHORITY_STUDENT = "student";
    public static final String AUTHORITY_SA = "student assistant";
    public static final String AUTHORITY_GROUP_LEADER = "group leader";
    public static final String AUTHORITY_TEACHER = "course teacher";
    public static final String AUTHORITY_ADMIN = "administration";


    // 第三方认证
    public static final String THIRD_GITEE = "gitee";
    public static final String THIRD_GITHUB = "github";


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

    // 工具方法
    public static String randomPassword(){
        return new BCryptPasswordEncoder().encode("123456");
    }

}
