package sustech.ooad.mainservice.controller;

import jakarta.annotation.Resource;
import java.lang.annotation.Retention;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sustech.ooad.mainservice.mapper.AuthUserRepository;
import sustech.ooad.mainservice.mapper.CourseAnnouncementRepository;
import sustech.ooad.mainservice.mapper.submitRepository;
import sustech.ooad.mainservice.model.dto.CourseInfoDto;
import sustech.ooad.mainservice.service.CourseService;
import sustech.ooad.mainservice.util.Result;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;

import static sustech.ooad.mainservice.util.ConstantField.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    AuthFunctionality authFunctionality;

    @Resource
    CourseService courseService;
    @Autowired
    CourseAnnouncementRepository courseAnnouncementRepository;
    @Autowired
    submitRepository submitRepository;
    @Autowired
    private AuthUserRepository authUserRepository;

    public String merge(String[] array) {
        StringBuilder str = new StringBuilder();
        for (String s : array) {
            str.append(s);
            str.append(";");
        }
        return str.toString();
    }

    //获取当前用户在某个作业的提交
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/assignment/{assignmentId}/submit")
    public Result<?> getOwnHomeworkSubmit(@PathVariable("assignmentId") Integer homeworkId) {
        try {
            return Result.ok(courseService.getOwnHomeworkSubmit(homeworkId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获取小组在某个作业的提交
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/assignment/{assignmentId}/group/{groupId}/submit")
    public Result<?> getGroupHomeworkSubmit(@PathVariable("assignmentId") Integer homeworkId,
        @PathVariable("groupId") Integer groupId) {
        try {
            boolean valid = courseService.inGroup(authFunctionality.getUser().getId().longValue(),
                groupId);
            if (!valid) {
                return Result.err(ACCESS_DENIED, "你不是该小组的成员");
            }
            return Result.ok(courseService.getGroupSubmit(homeworkId, groupId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获取课程所有老师
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/teacher")
    public Result<?> getCourseTeacher(@PathVariable("courseId") Integer courseId) {
        try {
            return Result.ok(courseService.getCourseTeacher(courseId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获取某个用户的信息
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/user/{uuid}/info")
    public Result<?> getUser(@PathVariable("uuid") long uuid) {
        try {
            return Result.ok(authUserRepository.findAuthUserById(new BigDecimal(uuid)));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获取课程所有学生和sa
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/member")
    public Result<?> getCourseUser(@PathVariable("courseId") Integer id) {
        try {
            return Result.ok(courseService.getCourseUser(id));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //修改通知
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/announcement/{id}/edit")
    public Result<?> getGroup(@PathVariable("id") Integer id,
        @RequestParam("description") String description) {
        courseService.modifyAnnouncement(description, id);
        try {
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获取某个小组
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/group/{groupId}")
    public Result<?> getGroup(@PathVariable("groupId") Integer groupId) {
        try {
            return Result.ok(courseService.getGroup(groupId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获取学生的提交
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/user/submit")
    public Result<?> getOwnSubmit() {
        return Result.ok(courseService.getOwnSubmit());
    }

    //添加成绩册
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/assignment/{assignmentId}/grade/add")
    public Result<?> addGrade(@PathVariable("courseId") Integer courseId,
        @PathVariable("assignmentId") Integer homeworkId, @RequestParam("url") String[] url) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法进入课程");
            }
            courseService.addGrade(merge(url), homeworkId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获得某个作业的成绩册
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @GetMapping("/{courseId}/assignment/{assignmentId}/grade/get")
    public Result<?> getGrade(@PathVariable("courseId") Integer courseId,
        @PathVariable("assignmentId") Integer homeworkId) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法进入课程");
            }
            return Result.ok(courseService.getGrade(homeworkId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //删除课程作业
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/assignment/{assignmentId}/delete")
    public Result<?> deleteHomework(@PathVariable("courseId") Integer courseId,
        @PathVariable("assignmentId") Integer homeworkId) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法进入课程");
            }
            courseService.deleteHomework(homeworkId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获得用户某个课程的小组
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/user/group")
    public Result<?> getUserGroup(@PathVariable("courseId") Integer courseId) {
        try {
            long uuid = authFunctionality.getUser().getId().longValue();
            return Result.ok(courseService.userCourseGroup(courseId, uuid));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //删除提交
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/submit/{submitId}")
    public Result<?> deleteSubmit(@PathVariable("submitId") Integer submitId) {
        try {
            submitRepository.deleteSubmitById(submitId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

//    //删除项目
//    @PreAuthorize(ROLE_CHECK_TEACHER)
//    @PostMapping("/{courseId}/project/{projectId}")
//    public Result<?> deleteProject(@PathVariable("projectId") Integer projectId,
//        @PathVariable("courseId") Integer courseId) {
//        boolean inCourse = authFunctionality.inCourse(courseId);
//        if (!inCourse) {
//            return Result.err(ACCESS_COURSE_DENIED, "无法进入课程");
//        }
//        courseService.deleteProject(projectId);
//        return Result.ok("");
//    }

    //获得课程通知
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/announcement/get")
    public Result<?> getAnnouncement(@PathVariable("courseId") Integer courseId) {
        try {
            boolean inCourse = authFunctionality.inCourse(courseId);
            if (!inCourse) {
                return Result.err(ACCESS_COURSE_DENIED, "无法进入课程");
            }
            return Result.ok(courseService.getAnnouncement(courseId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //新增课程通知
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/{courseId}/announcement/post")
    public Result<?> addAnnouncement(@PathVariable("courseId") Integer courseId,
        @RequestParam("content") String description, @RequestParam("user") Long[] user) {
        try {
            boolean inCourse = authFunctionality.inCourse(courseId);
            if (!inCourse) {
                return Result.err(ACCESS_COURSE_DENIED, "无法进入课程");
            }
            long uuid = authFunctionality.getUser().getId().longValue();
            courseService.addAnnouncement(courseId, uuid, description, user);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //查询作业所有提交
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @GetMapping("/{courseId}/homework/{homeworkId}/allSubmit")
    public Result<?> getSubmit(@PathVariable("courseId") long courseId,
        @PathVariable("homeworkId") Integer homeworkId) {
        try {
            boolean inCourse = authFunctionality.inCourse(courseId);
            if (!inCourse) {
                return Result.err(ACCESS_COURSE_DENIED, "无法进入课程");
            }
            return Result.ok(courseService.getSubmit(homeworkId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //上传小组共享资源
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/project/{projectId}/group/{groupId}/sharedsource/post")
    public Result<?> addShare(@PathVariable("projectId") Integer projectId,
        @PathVariable("groupId") Integer groupId, @RequestParam("url") String[] url) {
        try {
            long uuid = authFunctionality.getUser().getId().longValue();
            boolean inGroup = courseService.inGroup(uuid, groupId);
            if (!inGroup) {
                return Result.err(ACCESS_DENIED, "你不是该小组的成员");
            }
            courseService.addShare(groupId, merge(url), projectId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //删除小组共享资源
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/group/{groupId}/sharedsource/{shareId}/delete")
    public Result<?> deleteShare(@PathVariable("groupId") Integer groupId,
        @PathVariable("shareId") Integer shareId) {
        try {
            long uuid = authFunctionality.getUser().getId().longValue();
            boolean inGroup = courseService.inGroup(uuid, groupId);
            if (!inGroup) {
                return Result.err(ACCESS_DENIED, "你不是该小组的成员");
            }
            courseService.deleteShare(shareId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //批改作业
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("{courseId}/submit/{submitId}/marking")
    public Result<?> markSubmit(@PathVariable("submitId") Integer submitId,
        @PathVariable("courseId") long courseId, @RequestParam("score") Integer score,
        @RequestParam("comment") String feedback) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法进入当前课程");
            } else {
                String role = authFunctionality.getCourseAuthority(courseId);
                if (role.equals(AUTHORITY_TEACHER) || role.equals(AUTHORITY_SA)) {
                    courseService.markSubmit(feedback, score, submitId);
                    return Result.ok("");
                }
                return Result.err(ACCESS_COURSE_DENIED, "助教或老师才能批改作业");
            }
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //上交个人作业
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/{courseId}/assignment/{assignmentId}/submit")
    public Result<?> addUserSubmit(@PathVariable("courseId") Long courseId,
        @PathVariable("assignmentId") Integer assignmentId,
        @RequestParam("url") String[] attachment, @RequestParam("description") String description) {
        try {
            long uuid = authFunctionality.getUser().getId().longValue();
            boolean add = courseService.addUserSubmit(uuid, courseId, merge(attachment), description,
                assignmentId);
            if (!add) {
                return Result.err(ACCESS_DENIED, "你不能提交其他课程的作业");
            }
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //上交小组作业
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/{courseId}/assignment/{assignmentId}/group/{groupId}/submit")
    public Result<?> addGroupSubmit(@PathVariable("courseId") Long courseId,
        @PathVariable("assignmentId") Integer assignmentId,
        @RequestParam("url") String[] attachment, @RequestParam("description") String description,
        @PathVariable("groupId") Integer groupId) {
        try {
            long uuid = authFunctionality.getUser().getId().longValue();
            boolean valid = courseService.inGroup(uuid, groupId);
            if (!valid) {
                return Result.err(ACCESS_DENIED, "你不是当前小组的成员");
            }
            courseService.addGroupSubmit(courseId, merge(attachment), description, groupId,
                assignmentId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //学生退出小组
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/group/{groupId}/exit")
    public Result<?> exitGroup(@PathVariable("groupId") Integer groupId) {
        try {
            long uuid = authFunctionality.getUser().getId().longValue();
            courseService.exitGroup(uuid, groupId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //学生加入小组
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/group/{groupId}/join")
    public Result<?> joinGroup(@PathVariable("groupId") Integer groupId) {
        try {
            long uuid = authFunctionality.getUser().getId().longValue();
            int join = courseService.joinGroup(uuid, groupId);
            if (join == 0) {
                return Result.ok("");
            } else if (join == 1) {
                return Result.err(ACCESS_DENIED, "小组容量已满");
            } else {
                return Result.err(ACCESS_DENIED, "你在同一课程只能加入一个小组");
            }
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获得共享资源
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/project/{projectId}/sharedsource/get")
    public Result<?> getShare(@PathVariable("projectId") Integer projectId) {
        try {
            long uid = authFunctionality.getUser().getId().longValue();
            Integer groupId = courseService.getOwnGroup(projectId, uid);
            boolean valid = courseService.inGroup(uid, groupId);
            if (!valid) {
                return Result.err(ACCESS_DENIED, "无法查看此小组共享资源");
            }
            return Result.ok(courseService.getShare(projectId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //修改小组任务
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/project/{projectId}/group/{groupId}/task/edit/{taskId}")
    public Result<?> Task(@PathVariable("groupId") Integer groupId,
        @PathVariable("projectId") Integer projectId, @PathVariable("taskId") Integer taskId,
        @RequestParam("name") String name,
        @RequestParam("member") List<Long> member, @RequestParam("deadline") String ddl,
        @RequestParam("attachmentURL") String[] attachment,
        @RequestParam("description") String description) {
        try {
            long uid = authFunctionality.getUser().getId().longValue();
            boolean valid = courseService.inGroup(uid, groupId);
            if (!valid) {
                return Result.err(ACCESS_DENIED, "无法修改此小组任务");
            }
            courseService.modifyTask(name, ddl, merge(attachment), description, taskId, member);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //添加小组任务
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/project/{projectId}/group/{groupId}/task/add")
    public Result<?> addTask(@PathVariable("groupId") Integer groupId,
        @PathVariable("projectId") Integer projectId, @RequestParam("name") String name,
        @RequestParam("member") List<Long> member, @RequestParam("deadline") String ddl,
        @RequestParam("attachmentURL") String[] attachment,
        @RequestParam("description") String description) {
        try {
            long uid = authFunctionality.getUser().getId().longValue();
            boolean valid = courseService.inGroup(uid, groupId);
            if (!valid) {
                return Result.err(ACCESS_DENIED, "无法添加此小组任务");
            }
            courseService.addTask(name, ddl, merge(attachment), description, projectId, groupId,
                member);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获得小组任务
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/group/{groupId}/task/get")
    public Result<?> getTask(@PathVariable("groupId") Integer groupId) {
        try {
            long uid = authFunctionality.getUser().getId().longValue();
            boolean valid = courseService.inGroup(uid, groupId);
            if (!valid) {
                return Result.err(ACCESS_DENIED, "无法访问此小组任务");
            }
            return Result.ok(courseService.getTasks(groupId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //修改课程信息
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/edit")
    public Result<?> modifyCourse(@PathVariable("courseId") Integer courseId,
        @RequestParam("courseName") String name,
        @RequestParam("teacherID") Long teacherId,
        @RequestParam("TAID") Long[] Ta) {
        try {
            courseService.modifyCourse(name, courseId, teacherId, Ta);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //添加课程
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/add")
    public Result<?> addCourse(@RequestParam("courseName") String name,
        @RequestParam("teacherID") Long teacherId) {
        try {
            courseService.addCourse(name, teacherId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //删除课程小组
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/group/{groupId}/delete")
    public Result<?> deleteCourseGroup(@PathVariable("courseId") long courseId,
        @PathVariable("groupId") Integer groupId) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            courseService.deleteGroup(groupId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //修改课程小组
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/group/{groupId}/edit")
    public Result<?> addCourseGroup(@PathVariable("courseId") long courseId,
        @PathVariable("groupId") Integer groupId, @RequestParam("groupName") String name,
        @RequestParam("groupMemberID") Long[] member,
        @RequestParam("inspectorID") Long teacherId, @RequestParam("preTime") String preTime,
        @RequestParam("groupSize") Integer capacity, @RequestParam("ddl") String ddl,
        @RequestParam("introduction") String introduction) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            courseService.modifyGroup(name, groupId, member, teacherId, preTime, capacity, ddl,
                introduction);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获取课程小组
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/group/get")
    public Result<?> getCourseGroup(@PathVariable("courseId") long courseId) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            return Result.ok(courseService.getCourseGroup((int) courseId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //添加课程小组
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/project/{projectId}/group/add")
    public Result<?> addCourseGroup(@PathVariable("courseId") long courseId,
        @PathVariable("projectId") Integer projectId, @RequestParam("groupName") String name,
        @RequestParam("inspectorID") Long teacherId, @RequestParam("preTime") String preTime,
        @RequestParam("groupSize") Integer capacity,
        @RequestParam("introduction") String introduction) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            courseService.addCourseGroup((int) courseId, name, projectId, teacherId, preTime, capacity,
                introduction);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获取某个作业
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/assignment/{assignmentId}")
    public Result<?> getHomework(@PathVariable("courseId") long courseId,
        @PathVariable("assignmentId") Integer assignmentId) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            return Result.ok(courseService.getHomework(assignmentId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获取某个项目
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/project/{projectId}")
    public Result<?> getProject(@PathVariable("courseId") long courseId,
        @PathVariable("projectId") Integer projectId) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            return Result.ok(courseService.getProject(projectId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //修改课程作业信息
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/assignment/{assignmentId}/table/edit")
    public Result<?> modifyHomework(@PathVariable("courseId") long courseId,
        @PathVariable("assignmentId") Integer assignmentId,
        @RequestParam("assignmentName") String assignmentName, @RequestParam("deadline") String ddl,
        @RequestParam("attachment") String[] attachments,
        @RequestParam("description") String description) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            courseService.modifyHomework(assignmentName, ddl, description, merge(attachments),
                assignmentId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //修改课程项目信息
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/project/{projectId}/table/edit")
    public Result<?> modifyProject(@PathVariable("courseId") long courseId,
        @PathVariable("projectId") Integer projectId,
        @RequestParam("projectName") String projectName, @RequestParam("deadline") String ddl,
        @RequestParam("attachment") String[] attachments,
        @RequestParam("description") String description, @RequestParam("state") String state) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            courseService.modifyProject(projectName, ddl, description, merge(attachments),
                (int) courseId, state, projectId);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }

    }

    //添加课程作业
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/assignment/table/add")
    public Result<?> addHomework(@PathVariable("courseId") long courseId,
        @RequestParam("assignmentName") String assignmentName, @RequestParam("deadline") String ddl,
        @RequestParam("attachment") String[] attachments,
        @RequestParam("description") String description, @RequestParam("isGroup") Integer isGroup) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            long uid = authFunctionality.getUser().getId().longValue();
            courseService.addHomework(assignmentName, ddl, description, merge(attachments),
                (int) courseId,
                isGroup, uid);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //添加课程项目
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/project/table/add")
    public Result<?> addProject(@PathVariable("courseId") long courseId,
        @RequestParam("projectName") String projectName, @RequestParam("deadline") String ddl,
        @RequestParam("attachment") String[] attachments,
        @RequestParam("description") String description, @RequestParam("state") String state) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            long uuid = authFunctionality.getUser().getId().longValue();
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            courseService.addProject(projectName, ddl, description, merge(attachments),
                (int) courseId,
                state, uuid);
            return Result.ok("");
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }

    }

    //获得课程所有作业
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/assignment/table")
    public Result<?> getCourseHomeworkTable(@PathVariable("courseId") long courseId) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            return Result.ok(courseService.getHomeworkTable((int) courseId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获得课程所有项目
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/project/table")
    public Result<?> getCourseProjectInfo(@PathVariable("courseId") long courseId) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            return Result.ok(courseService.getProjectInfo((int) courseId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    //获取我的课程
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/user/class")
    public Result<?> getAllCourse() {
        try {
            long uid = authFunctionality.getUser().getId().longValue();  //获取自身用户
            List<CourseInfoDto> courseInfoDtoList = courseService.getUserCourse(uid);
            return Result.ok(courseInfoDtoList);
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }


    @PreAuthorize(ROLE_CHECK)
    @GetMapping("{cid}/info")
    public Result<?> getCourseProfile(@PathVariable("cid") long courseId) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            return Result.ok(courseService.getCourseInfo((int) courseId));
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    @PreAuthorize(ROLE_CHECK)
    @DeleteMapping("{cid}/quit")
    public Result<?> quit(@PathVariable("cid") long courseId
    ) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            long uid = authFunctionality.getUser().getId().longValue();  //获取自身用户
            courseService.quitCourse(courseId, uid);
            return Result.ok(null);
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("{cid}/invite")
    public Result<?> addMember(@PathVariable("cid") long courseId,
        @RequestParam("sid") long sid,
        @RequestParam("authority") String authority
    ) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            boolean isCourseTeacher = authFunctionality.hasCourseAuthority(courseId, AUTHORITY_TEACHER);
            if (!isCourseTeacher) {
                return Result.err(COURSE_INVALID_AUTHORITY, "课程权限不足");
            }
            courseService.addCourseAuthority(courseId, sid, authority);
            return Result.ok(null);
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }

    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("{cid}/update/member/authority")
    public Result<?> changeAuthority(@PathVariable("cid") long courseId,
        @RequestParam("sid") long sid,
        @RequestParam("authority") String authority
    ) {
        try {
            boolean valid = authFunctionality.inCourse(courseId);
            if (!valid) {
                return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
            }
            boolean isCourseTeacher = authFunctionality.hasCourseAuthority(courseId, AUTHORITY_TEACHER);
            if (!isCourseTeacher) {
                return Result.err(COURSE_INVALID_AUTHORITY, "课程权限不足");
            }
            courseService.updateCourseAuthority(courseId, sid, authority);
            return Result.ok(null);
        } catch (Exception e) {
            return Result.err(500, e.getMessage());
        }
    }
}
