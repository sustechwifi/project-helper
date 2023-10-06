package sustech.ooad.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import sustech.ooad.mainservice.util.ConstantField;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements OAuth2User,UserDetails {

    long id;
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

    // 表单注册
    public AuthUser(String username,String password) {
        this.login = username;
        this.name = username;
        this.password = authUserPassEncoder.encode(password);
        this.avatar = defaultUser.avatar;
    }

    // 第三方免注册
    public AuthUser(String username ,String name, String platform,String avatar) {
        this.login = username;
        this.name = name;
        this.platform = platform;
        this.avatar = avatar;
        this.password = authUserPassEncoder.encode("123456");
    }

    public static PasswordEncoder authUserPassEncoder = new BCryptPasswordEncoder();

    public static AuthUser defaultUser = new AuthUser(
                -1,
                "test",
                "test_user",
                "123456",
                "123@qq.com",
            ConstantField.ROLE_DEFAULT,
                "local",
                "https://ts1.cn.mm.bing.net/th/id/R-C.2229eb8e5a576c3089b76f041b36077d?rik=d9LvVt%2bjoE8mAg&pid=ImgRaw&r=0",
                null,
                null,
                null,
                null,
                null,
                true,
                null);


    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return name;
    }
}
