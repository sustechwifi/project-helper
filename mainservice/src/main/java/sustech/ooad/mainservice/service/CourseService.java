package sustech.ooad.mainservice.service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import sustech.ooad.mainservice.mapper.AuthUserMapper;
import sustech.ooad.mainservice.mapper.AuthUserRepository;
import sustech.ooad.mainservice.mapper.CourseAnnouncementRepository;
import sustech.ooad.mainservice.mapper.CourseAuthorityMapper;
import sustech.ooad.mainservice.mapper.CourseAuthorityRepository;
import sustech.ooad.mainservice.mapper.CourseRepository;
import sustech.ooad.mainservice.mapper.GroupMemberListRepository;
import sustech.ooad.mainservice.mapper.GroupProjectRepository;
import sustech.ooad.mainservice.mapper.GroupRepository;
import sustech.ooad.mainservice.mapper.HomeworkRepository;
import sustech.ooad.mainservice.mapper.ShareRepository;
import sustech.ooad.mainservice.mapper.TaskMemRepository;
import sustech.ooad.mainservice.mapper.TaskRepository;
import sustech.ooad.mainservice.mapper.UserprojectRepository;
import sustech.ooad.mainservice.mapper.submitRepository;
import sustech.ooad.mainservice.mapper.ProjectRepository;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.CourseAnnouncement;
import sustech.ooad.mainservice.model.CourseAuthority;
import sustech.ooad.mainservice.model.Group;
import sustech.ooad.mainservice.model.GroupMemberList;
import sustech.ooad.mainservice.model.GroupProject;
import sustech.ooad.mainservice.model.Homework;
import sustech.ooad.mainservice.model.Share;
import sustech.ooad.mainservice.model.Submit;
import sustech.ooad.mainservice.model.Task;
import sustech.ooad.mainservice.model.TaskMem;
import sustech.ooad.mainservice.model.Userproject;
import sustech.ooad.mainservice.model.dto.CourseInfoDto;
import sustech.ooad.mainservice.model.dto.GroupDto;
import sustech.ooad.mainservice.model.dto.HomeworkDto;
import sustech.ooad.mainservice.model.dto.ProjectDto;
import sustech.ooad.mainservice.model.dto.attachment;
import sustech.ooad.mainservice.model.dto.noticeDto;
import sustech.ooad.mainservice.model.dto.submitDto;
import sustech.ooad.mainservice.model.dto.taskDto;
import sustech.ooad.mainservice.model.Project;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;

import static sustech.ooad.mainservice.util.ConstantField.AUTHORITY_SA;
import static sustech.ooad.mainservice.util.ConstantField.AUTHORITY_TEACHER;
import static sustech.ooad.mainservice.util.ConstantField.USER_COURSES;

@Service
public class CourseService {

    @Resource
    CourseAuthorityMapper courseAuthorityMapper;
    @Autowired
    AuthUserMapper authUserMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    AuthFunctionality authFunctionality;
    @Autowired
    TaskRepository taskRepository;
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
    @Autowired
    TaskMemRepository taskMemRepository;
    @Autowired
    ShareRepository shareRepository;
    @Autowired
    GroupProjectRepository groupProjectRepository;
    @Autowired
    private AuthUserRepository authUserRepository;
    @Autowired
    private CourseAnnouncementRepository courseAnnouncementRepository;
    @Autowired
    private UserprojectRepository userprojectRepository;

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

    public CourseInfoDto getCourseInfo(Integer id) {
        CourseInfoDto courseInfoDto = new CourseInfoDto();
        courseInfoDto.setCourse(courseRepository.findCourseById(id).getName());
        courseInfoDto.setId(id);
        List<String> ta = courseAuthorityRepository.findCourseAuthoritiesByCourseIdAndCourseAuthority(
                id, AUTHORITY_SA).stream().map(CourseAuthority::getUserId)
            .map(a -> authUserRepository.findAuthUserById(new BigDecimal(a)).getName())
            .collect(Collectors.toList());
        courseInfoDto.setTa(ta);
        long teacher = courseAuthorityRepository.findCourseAuthoritiesByCourseIdAndCourseAuthority(
            id, AUTHORITY_TEACHER).get(0).getUserId();
        String teacher_name = authUserRepository.findAuthUserById(new BigDecimal(teacher))
            .getName();
        courseInfoDto.setTeacher(teacher_name);
        return courseInfoDto;
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
        Integer courseId, String state, long uuid) {
        courseRepository.addProject(projectName, courseId, ddl, state, description, attachment);
        homeworkRepository.addHomework(projectName + " final submit", ";",
            "This is " + projectName + " final submit", null, courseId, 1, uuid);
        Integer homeworkId = homeworkRepository.findHomeworkByName(projectName + " final submit")
            .getId();
        Integer projectId = projectRepository.findByCourseAndName(
            courseRepository.findCourseById(courseId), projectName).getId();
        courseRepository.modifyProject(projectName, courseId, ddl, state, description, attachment,
            projectId, homeworkId);
    }

    public void addHomework(String name, String ddl, String description, String attachment,
        Integer courseId, Integer isGroup, Long userId) {
        homeworkRepository.addHomework(name, attachment, description, ddl, courseId, isGroup,
            userId);
    }

    public void modifyProject(String projectName, String ddl, String description, String attachment,
        Integer courseId, String state, Integer projectId) {
        Integer homeworkId = homeworkRepository.findHomeworkByName(projectName + " final submit")
            .getId();
        courseRepository.modifyProject(projectName, courseId, ddl, state, description, attachment,
            projectId, homeworkId);
        homeworkRepository.modifyddl(ddl, homeworkId);
    }

    public void modifyHomework(String name, String ddl, String description, String attachment,
        Integer homeworkId) {
        homeworkRepository.modifyHomework(name, attachment, description, ddl, homeworkId);
        Project project = projectRepository.findProjectByHomeworkid(
            homeworkRepository.findHomeworkById(homeworkId));
        projectRepository.modifyddl(ddl, project.getId());
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
            temp.setId((int) c.getCourseId());
            temp.setCourse(courseRepository.findCourseById((int) c.getCourseId()).getName());
            temp.setAuth(c.getCourseAuthority());
            List<String> ta = courseAuthorityRepository.findCourseAuthoritiesByCourseIdAndCourseAuthority(
                    c.getCourseId(), AUTHORITY_SA).stream().map(CourseAuthority::getUserId)
                .map(a -> authUserRepository.findAuthUserById(new BigDecimal(a)).getName())
                .collect(Collectors.toList());
            temp.setTa(ta);
            Long teacher = courseAuthorityRepository.findCourseAuthoritiesByCourseIdAndCourseAuthority(
                c.getCourseId(), AUTHORITY_TEACHER).get(0).getUserId();
            String teacher_name = authUserRepository.findAuthUserById(new BigDecimal(teacher))
                .getName();
            temp.setTeacher(teacher_name);
            courseInfoDto.add(temp);
        }
        return courseInfoDto;
    }

    public void addCourseGroup(Integer courseId, String name, Integer projectId, Long teacherId,
        String preTime, Integer capacity) {
        groupRepository.addGroup(name, courseId, teacherId, preTime, capacity);
        Integer groupId = groupRepository.findGroupByNameAndCourse(name,
            courseRepository.findCourseById(courseId)).getId();
        groupProjectRepository.add(groupId, projectId);
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

    public void modifyGroup(String name, Integer groupId, Long[] member, Long teacherId,
        String preTime, Integer capacity) {
        groupRepository.modifyGroup(name, teacherId, preTime, capacity, groupId);
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

    public List<taskDto> getTasks(Integer groupId) {
        List<taskDto> taskDtoList = new ArrayList<>();
        List<Task> taskList = taskRepository.findTasksByGroupid(
            groupRepository.findGroupById(groupId));
        taskList.forEach(a -> {
            Integer id = a.getId();
            List<TaskMem> taskMemList = taskMemRepository.findTaskMemsByTaskid(
                taskRepository.findTaskById(id));
            List<Long> memberList = new ArrayList<>();
            taskMemList.forEach(b -> {
                memberList.add(b.getUuid().getId().longValue());
            });
            taskDtoList.add(new taskDto(a, memberList));
        });
        return taskDtoList;
    }

    public boolean inGroup(Long uuid, Integer groupId) {
        return Objects.equals(groupId, groupMemberListRepository.findGroupMemberListByUserUuid(
                authUserMapper.selectOneById(uuid)).getGroup()
            .getId());
    }

    public void addTask(String name, String ddl, String attachment, String description,
        Integer projectId,
        Integer groupId, List<Long> member) {
        taskRepository.addTask(name, ddl, attachment, description, projectId, groupId);
        Task task = taskRepository.findTaskByGroupidAndName(groupRepository.findGroupById(groupId),
            name);
        member.forEach(a -> {
            taskMemRepository.addTaskMem(task.getId(), a);
        });
    }

    public void modifyTask(String name, String ddl, String attachment, String description,
        Integer taskId, List<Long> member) {
        taskRepository.modifyTask(name, ddl, attachment, description, taskId);
        taskMemRepository.deleteTaskMemsByTaskid(taskRepository.findTaskById(taskId));
        member.forEach(a -> {
            taskMemRepository.addTaskMem(taskId, a);
        });
    }

    public List<Share> getShare(Integer projectId) {
        return shareRepository.findSharesByProject(projectRepository.findProjectById(projectId));
    }

    public Integer getOwnGroup(Integer projectId, Long uuid) {
        Integer courseId = projectRepository.findProjectById(projectId).getCourse().getId();
        List<Group> groupList = groupRepository.findGroupsByCourse(
            courseRepository.findCourseById(courseId));
        List<GroupMemberList> OwnGroupList = groupMemberListRepository.findGroupMemberListsByUserUuid(
            authUserRepository.findAuthUserById(new BigDecimal(uuid)));
        List<Integer> groupId1 = OwnGroupList.stream().map(a -> a.getGroup().getId()).collect(
            Collectors.toList());
        List<Integer> groupId2 = groupList.stream().map(Group::getId).toList();
        groupId1.retainAll(groupId2);
        return groupId1.get(0);
    }

    public int joinGroup(Long uuid, Integer groupId) {
        Group group = groupRepository.findGroupById(groupId);
        Integer courseId = group.getCourse().getId();
        Integer capacity = group.getCapacity();
        List<GroupProject> groupProjectList = groupProjectRepository.findGroupProjectsByGroupid(
            group);
        List<GroupMemberList> groupMemberListList = groupMemberListRepository.findGroupMemberListsByGroup(
            group);
        List<Group> userGroupList = groupMemberListRepository.findGroupMemberListsByUserUuid(
                authUserRepository.findAuthUserById(new BigDecimal(uuid))).stream()
            .map(a -> groupRepository.findGroupById(a.getGroup().getId()))
            .toList();
        List<Integer> courseId2 = userGroupList.stream().map(a -> a.getCourse().getId()).toList();
        if (!courseId2.contains(courseId)) {
            int now = groupMemberListList.size();
            if (now < capacity) {
                groupMemberListRepository.addGroupMember(groupId, uuid);
                groupProjectList.forEach(
                    a -> userprojectRepository.addUserProject(uuid, a.getProjectid().getId()));
                return 0;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }

    public Group userCourseGroup(Integer courseId, long uuid) {
        Group group = new Group();
        List<Group> userGroupList = groupMemberListRepository.findGroupMemberListsByUserUuid(
                authUserRepository.findAuthUserById(new BigDecimal(uuid))).stream()
            .map(a -> groupRepository.findGroupById(a.getGroup().getId()))
            .toList();
        for (Group g : userGroupList) {
            if (Objects.equals(g.getCourse().getId(), courseId)) {
                group = g;
            }
        }
        return group;
    }

    public void exitGroup(Long uuid, Integer groupId) {
        Group group = groupRepository.findGroupById(groupId);
        AuthUser user = authUserRepository.findAuthUserById(new BigDecimal(uuid));
        groupMemberListRepository.deleteGroupMemberListByGroupAndUserUuid(group, user);
        List<GroupProject> groupProjectList = groupProjectRepository.findGroupProjectsByGroupid(
            group);
        groupProjectList.forEach(a -> {
            userprojectRepository.deleteUserprojectsByUserAndProject(user, a.getProjectid());
        });
    }

    public void deleteGroup(Integer groupId) {
        Group group = groupRepository.findGroupById(groupId);
        groupProjectRepository.deleteGroupProjectByGroupid(group);
        groupMemberListRepository.deleteGroupMemberListsByGroup(group);
        List<Task> taskList = taskRepository.findTasksByGroupid(group);
        taskList.forEach(a -> taskMemRepository.deleteTaskMemsByTaskid(a));
        taskRepository.deleteTasksByGroupid(group);
        shareRepository.deleteSharesByGroup(group);
        groupRepository.deleteGroupById(groupId);
    }

    public boolean addUserSubmit(Long uuid, Long courseId, String attachment, String description,
        Integer homeworkId) {
        boolean inCourse = authFunctionality.inCourse(courseId);
        if (!inCourse) {
            return false;
        }
        submitRepository.addUserSubmit(uuid, courseId, attachment, description, homeworkId);
        return true;
    }

    public void addGroupSubmit(Long courseId, String attachment, String description,
        Integer groupId, Integer homeworkId) {
        submitRepository.addGroupSubmit(courseId, attachment, description, groupId, homeworkId);
    }

    public void markSubmit(String feedback, Integer score, Integer homeworkId) {
        submitRepository.modifySubmit(feedback, score, homeworkId);
    }

    public void addShare(Integer groupId, String attachment, Integer projectId) {
        shareRepository.addShare(groupId, attachment, projectId);
    }

    public void deleteShare(Integer shareId) {
        shareRepository.deleteShare(shareId);
    }

    public List<submitDto> getSubmit(Integer homeworkId) {
        List<Submit> submitList = submitRepository.findSubmitsByHomework(
            homeworkRepository.findHomeworkById(homeworkId));
        List<submitDto> submitDtoList = new ArrayList<>();
        submitList.forEach(a -> {
            submitDtoList.add(new submitDto(a, attachment.divide(a.getAttachment())));
        });
        return submitDtoList;
    }

    public List<noticeDto> getAnnouncement(Integer courseId) {
        List<CourseAnnouncement> courseAnnouncementList = courseAnnouncementRepository.findCourseAnnouncementsByCourse(
            courseRepository.findCourseById(courseId));
        List<noticeDto> noticeDtoList = new ArrayList<>();
        courseAnnouncementList.forEach(a -> {
            noticeDtoList.add(new noticeDto(a.getDescription(), a.getId(), a.getCourse(),
                a.getUserUuid().getId().longValue(), a.getUserUuid().getName()));
        });
        return noticeDtoList;
    }

    public void addAnnouncement(Integer courseId, Long uuid, String description) {
        courseAnnouncementRepository.addAnnouncement(courseId, uuid, description);
    }

    public void deleteHomework(Integer id) {
        homeworkRepository.deleteHomeworkById(id);
    }

    public void deleteProject(Integer id) {
        projectRepository.deleteProjectById(id);
    }
}
