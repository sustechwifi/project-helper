package sustech.ooad.mainservice.service;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import sustech.ooad.mainservice.mapper.CourseAuthorityMapper;
import sustech.ooad.mainservice.model.CourseAuthority;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static sustech.ooad.mainservice.util.ConstantField.AUTHORITY_STUDENT;
import static sustech.ooad.mainservice.util.ConstantField.USER_COURSES;

@Service
public class CourseService {

    @Resource
    CourseAuthorityMapper courseAuthorityMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    AuthFunctionality authFunctionality;

    private void deleteCache(long uid){
        // 清除缓存
        String key = USER_COURSES + uid;
        stringRedisTemplate.delete(key);
    }

    public void addCourseAuthority(long courseId, long sid,String authority) {
        // TODO 筛选 authority
        courseAuthorityMapper.addRecord(courseId,sid,authority);
        deleteCache(sid);
    }

    public void quitCourse(long courseId, long uid) {
        courseAuthorityMapper.deleteRecord(courseId,uid);
        deleteCache(uid);
    }

    public void updateCourseAuthority(long courseId, long sid, String authority) {
        courseAuthorityMapper.updateRecord(courseId,sid,authority);
        deleteCache(sid);
    }
}
