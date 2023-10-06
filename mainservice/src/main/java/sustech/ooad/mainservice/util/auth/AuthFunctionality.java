package sustech.ooad.mainservice.util.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import sustech.ooad.mainservice.model.AuthUser;

import java.util.Map;


public interface AuthFunctionality {

    /**
     * 获取当前会话认证信息
     */
    Authentication getAuthentication();

    AuthUser getUser();

    /**
     * 检查当前认证用户是否有此权限
     * @param role 权限值
     * @return 判定结果
     */
    boolean hasRole(String role);


    Map<Long,String> getUserCourses();

    boolean inCourse(long course);

    String getCourseAuthority(long course);

    boolean hasCourseAuthority(long course,String ... authority);


}
