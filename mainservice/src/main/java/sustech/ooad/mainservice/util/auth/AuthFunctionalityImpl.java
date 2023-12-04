package sustech.ooad.mainservice.util.auth;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sustech.ooad.mainservice.mapper.CourseAuthorityMapper;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.model.CourseAuthority;
import sustech.ooad.mainservice.util.Result;

import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static sustech.ooad.mainservice.util.ConstantField.*;

@Component
public class AuthFunctionalityImpl implements AuthFunctionality {

    @Resource
    CourseAuthorityMapper courseAuthorityMapper;

    @Resource
    RedisTemplate<String,Map<Long,String>> redisTemplate;

    @Override
    public Authentication getAuthentication() {
        // 获取当前的安全上下文
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // 通过安全上下文获取认证对象
        return securityContext.getAuthentication();
    }

    @Override
    public AuthUser getUser() {
        var auth = getAuthentication();
        if (auth.getPrincipal() instanceof AuthUser user){
            return user;
        }else if(auth.getDetails() instanceof AuthUser user){
            return user;
        }
        return (AuthUser) getAuthentication().getPrincipal();
    }

    @Override
    public boolean hasRole(String role) {
        return getUser().getRole().equals(role);
    }

    @Override
    public Map<Long,String> getUserCourses(){
        long uid = getUser().getId().longValue();
        String key = USER_COURSES + uid;
        var data = redisTemplate.opsForValue().get(key);
        if (data != null) {
            return data;
        }
        var res = courseAuthorityMapper.findUserCourses(uid)
                .stream()
                .collect(Collectors.toMap(
                        CourseAuthority::getCourseId,
                        CourseAuthority::getAuthority)
                );
        redisTemplate.opsForValue().set(key, res);
        redisTemplate.expire(key,ONE_DAY);
        return res;
    }

    @Override
    public boolean inCourse(long course) {
        return getUserCourses().containsKey(course);
    }

    @Override
    public String getCourseAuthority(long course) {
        return getUserCourses().get(course);
    }

    @Override
    public boolean hasCourseAuthority(long course, String ...authority) {
        return Arrays.stream(authority).anyMatch(a -> getUserCourses().get(course).equals(a));
    }


}
