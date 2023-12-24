package sustech.ooad.websocketserver.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
    BigDecimal id;
    String login;
    String name;
    String password;
    String email;
    String role;
    String platform;
    String avatar;
    String college;
    String sex;
    String grade;
    String major;
    String introduction;
    boolean enabled;
    LocalDate createTime;
}
