package sustech.ooad.mainservice.service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import sustech.ooad.mainservice.mapper.CourseAuthorityMapper;
import sustech.ooad.mainservice.mapper.CourseAuthorityRepository;
import sustech.ooad.mainservice.mapper.CourseRepository;
import sustech.ooad.mainservice.mapper.GroupMemberListRepository;
import sustech.ooad.mainservice.mapper.GroupRepository;
import sustech.ooad.mainservice.mapper.HomeworkRepository;
import sustech.ooad.mainservice.mapper.submitRepository;
import sustech.ooad.mainservice.mapper.ProjectRepository;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.CourseAuthority;
import sustech.ooad.mainservice.model.Group;
import sustech.ooad.mainservice.model.GroupMemberList;
import sustech.ooad.mainservice.model.Homework;
import sustech.ooad.mainservice.model.dto.CourseInfoDto;
import sustech.ooad.mainservice.model.dto.GroupDto;
import sustech.ooad.mainservice.model.dto.HomeworkDto;
import sustech.ooad.mainservice.model.dto.ProjectDto;
import sustech.ooad.mainservice.model.dto.attachment;
import sustech.ooad.mainservice.model.submit;
import sustech.ooad.mainservice.model.Project;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;

import static sustech.ooad.mainservice.util.ConstantField.AUTHORITY_SA;
import static sustech.ooad.mainservice.util.ConstantField.AUTHORITY_TEACHER;
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

    @Autowired
    CourseAuthorityRepository courseAuthorityRepository;
    @Autowired
    GroupMemberListRepository groupMemberListRepository;

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

    public List<ProjectDto> getProjectInfo(Integer courseId) {
        List<Project> projectList = projectRepository.findProjectsByCourse(
            courseRepository.findCourseById(courseId));
        List<ProjectDto> projectDtoList = new ArrayList<>();
        for (Project p : projectList) {
            projectDtoList.add(new ProjectDto(p, attachment.divide(p.getAttachment())));
        }
        return projectDtoList;
    }

    public List<HomeworkDto> getHomeworkTable(Integer courseId) {
        List<Homework> homeworkList = homeworkRepository.findAllByCourseid(
            courseRepository.findCourseById(courseId));
        List<HomeworkDto> homeworkDtoList = new ArrayList<>();
        for (Homework h : homeworkList) {
            homeworkDtoList.add(new HomeworkDto(h, attachment.divide(h.getAttachment())));
        }
        return homeworkDtoList;
    }

    public void addProject(String projectName, String ddl, String description, String attachment,
        Integer courseId, String state) {
        courseRepository.addProject(projectName, courseId, ddl, state, description, attachment);
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

    public void modifyHomework(String name, String ddl, String description, String attachment,
        Integer homeworkId) {
        homeworkRepository.modifyHomework(name, attachment, description, ddl, homeworkId);
    }

    public ProjectDto getProject(Integer id) {
        Project p = projectRepository.findProjectById(id);
        return new ProjectDto(p, attachment.divide(p.getAttachment()));
    }

    public HomeworkDto getHomework(Integer id) {
        Homework h = homeworkRepository.findHomeworkById(id);
        return new HomeworkDto(h, attachment.divide(h.getAttachment()));
    }

    public List<CourseInfoDto> getUserCourse(long uuid) {
        List<CourseAuthority> courseAuthorityList = courseAuthorityRepository.findCourseAuthoritiesByUserId(
            uuid);
        List<CourseInfoDto> courseInfoDto = new ArrayList<>();
        for (CourseAuthority c : courseAuthorityList) {
            CourseInfoDto temp = new CourseInfoDto();
            temp.setCourse(c.getCourseId());
            temp.setAuth(c.getCourseAuthority());
            List<Long> ta = courseAuthorityRepository.findCourseAuthoritiesByCourseIdAndCourseAuthority(
                    c.getCourseId(), AUTHORITY_SA).stream().map(CourseAuthority::getUserId)
                .collect(Collectors.toList());
            temp.setTa(ta);
            Long teacher = courseAuthorityRepository.findCourseAuthoritiesByCourseIdAndCourseAuthority(
                c.getCourseId(), AUTHORITY_TEACHER).get(0).getUserId();
            temp.setTeacher(teacher);
            courseInfoDto.add(temp);
        }
        return courseInfoDto;
    }

    public void addCourseGroup(Integer courseId, String name, Integer projectId) {
        groupRepository.addGroup(name, courseId, projectId);
    }

    public List<GroupDto> getCourseGroup(Integer courseId) {
        List<Group> groupList = groupRepository.findGroupsByCourse(
            courseRepository.findCourseById(courseId));
        List<GroupDto> dtoList = new ArrayList<>();
        for (Group g : groupList) {
            List<GroupMemberList> groupMemberList = groupMemberListRepository.findGroupMemberListsByGroup(
                g);
            List<Long> member = groupMemberList.stream()
                .map(a -> a.getUserUuid().getId().longValue()).toList();
            dtoList.add(new GroupDto(g, member));
        }
        return dtoList;
    }

    public void modifyGroup(String name, Integer groupId, Long[] member) {
        groupRepository.modifyGroup(name, groupId);
        groupMemberListRepository.deleteGroupMemberListsByGroup(
            groupRepository.findGroupById(groupId));
        for (Long i : member) {
            groupMemberListRepository.addGroupMember(groupId, i);
        }
    }

    public void addCourse(String name, Long uuid) {
        courseRepository.addCourse(name);
        Course c = courseRepository.findCourseByName(name);
        courseAuthorityRepository.addCourseMember(c.getId(), uuid, "course teacher");
    }

    public void modifyCourse(String name, Integer courseId, Long teacherId, Long[] ta) {
        courseRepository.modifyCourse(name, courseId);
        courseAuthorityRepository.modifyCourseTeacher(teacherId, courseId);
        courseAuthorityRepository.deleteCourseAuthoritiesByCourseIdAndCourseAuthority(courseId,
            "student assistant");
        for (Long i : ta) {
            courseAuthorityRepository.addCourseMember(courseId, i, "student assistant");
        }
    }
}
