package sustech.ooad.mainservice.service;

import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import sustech.ooad.mainservice.mapper.CourseAuthorityMapper;
import sustech.ooad.mainservice.mapper.CourseRepository;
import sustech.ooad.mainservice.mapper.GroupRepository;
import sustech.ooad.mainservice.mapper.HomeworkRepository;
import sustech.ooad.mainservice.mapper.submitRepository;
import sustech.ooad.mainservice.mapper.ProjectRepository;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.submit;
import sustech.ooad.mainservice.model.Project;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;

import static sustech.ooad.mainservice.util.ConstantField.USER_COURSES;

@Service
public class CourseService {

    @Resource
    CourseAuthorityMapper courseAuthorityMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    AuthFunctionality authFunctionality;

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    submitRepository submitRepository;
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    HomeworkRepository homeworkRepository;

    private void deleteCache(long uid) {
        // 清除缓存
        String key = USER_COURSES + uid;
        stringRedisTemplate.delete(key);
    }

    public void addCourseAuthority(long courseId, long sid, String authority) {
        // TODO 筛选 authority
        courseAuthorityMapper.addRecord(courseId, sid, authority);
        deleteCache(sid);
    }

    public void quitCourse(long courseId, long uid) {
        courseAuthorityMapper.deleteRecord(courseId, uid);
        deleteCache(uid);
    }

    public void updateCourseAuthority(long courseId, long sid, String authority) {
        courseAuthorityMapper.updateRecord(courseId, sid, authority);
        deleteCache(sid);
    }

    public Course getCourseInfo(Integer id) {
        return courseRepository.findCourseById(id);
    }

    public List<Project> getProjectInfo(Integer courseId) {
        return projectRepository.findProjectsByCourse(courseRepository.findCourseById(courseId));
    }

    public List<submit> getHomeworkTable(Integer courseId) {
        return submitRepository.findAllByCourse(courseRepository.findCourseById(courseId));
    }

    public void addProject(String projectName, String ddl, String description, String attachment,
        String[] group, Integer courseId, String state) {
        courseRepository.addProject(projectName, courseId, ddl, state, description, attachment);
        Project p = projectRepository.findByCourseAndName(courseRepository.findCourseById(courseId),
            projectName);
        for (String s : group) {
            groupRepository.addGroup(s, courseId, p.getId());
        }
    }

    public void addHomework(String name, String ddl, String description, String attachment,
        Integer courseId, Integer isGroup, Long userId) {
        homeworkRepository.addHomework(name, attachment, description, ddl, courseId, isGroup,
            userId);
    }

    public void modifyProject(String projectName, String ddl, String description, String attachment,
        Integer courseId, String state, Integer projectId) {
        courseRepository.modifyProject(projectName, courseId, ddl, state, description, attachment,
            projectId);
    }
}
