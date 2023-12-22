package sustech.ooad.mainservice.controller;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sustech.ooad.mainservice.model.dto.CourseInfoDto;
import sustech.ooad.mainservice.service.CourseService;
import sustech.ooad.mainservice.util.Result;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;

import java.util.Map;

import static sustech.ooad.mainservice.util.ConstantField.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    AuthFunctionality authFunctionality;

    @Resource
    CourseService courseService;

    public String merge(String[] array) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            str.append(array[i]);
            str.append(";");
        }
        return str.toString();
    }

    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/project/{projectId}/group/{groupId}/task/edit/{taskId}")
    public Result<?> modifyTask(@PathVariable("groupId") Integer groupId,
        @PathVariable("projectId") Integer projectId, @PathVariable("taskId") Integer taskId,
        @RequestParam("name") String name,
        @RequestParam("member") List<Long> member, @RequestParam("deadline") String ddl,
        @RequestParam("attachmentURL") String[] attachment,
        @RequestParam("description") String description) {
        long uid = authFunctionality.getUser().getId().longValue();
        boolean valid = courseService.inGroup(uid, groupId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法修改此小组任务");
        }
        courseService.modifyTask(name, ddl, merge(attachment), description, taskId, member);
        return Result.ok("");
    }

    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/project/{projectId}/group/{groupId}/task/add")
    public Result<?> addTask(@PathVariable("groupId") Integer groupId,
        @PathVariable("projectId") Integer projectId, @RequestParam("name") String name,
        @RequestParam("member") List<Long> member, @RequestParam("deadline") String ddl,
        @RequestParam("attachmentURL") String[] attachment,
        @RequestParam("description") String description) {
        long uid = authFunctionality.getUser().getId().longValue();
        boolean valid = courseService.inGroup(uid, groupId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法添加此小组任务");
        }
        courseService.addTask(name, ddl, merge(attachment), description, projectId, groupId,
            member);
        return Result.ok("");
    }

    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/group/{groupId}/task/get")
    public Result<?> getTask(@PathVariable("groupId") Integer groupId) {
        long uid = authFunctionality.getUser().getId().longValue();
        boolean valid = courseService.inGroup(uid, groupId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此小组任务");
        }
        return Result.ok(courseService.getTasks(groupId));
    }

    //修改课程信息
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/edit")
    public Result<?> modifyCourse(@PathVariable("courseId") Integer courseId,
        @RequestParam("courseName") String name,
        @RequestParam("teacherID") Long teacherId,
        @RequestParam("TAID") Long[] Ta) {
        courseService.modifyCourse(name, courseId, teacherId, Ta);
        return Result.ok("");
    }

    //添加课程
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/add")
    public Result<?> addCourse(@RequestParam("courseName") String name,
        @RequestParam("teacherID") Long teacherId) {
        courseService.addCourse(name, teacherId);
        return Result.ok("");
    }

    //修改课程小组
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/group/{groupId}/edit")
    public Result<?> addCourseGroup(@PathVariable("courseId") long courseId,
        @PathVariable("groupId") Integer groupId, @RequestParam("groupName") String name,
        @RequestParam("groupMemberID") Long[] member) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        courseService.modifyGroup(name, groupId, member);
        return Result.ok("");
    }

    //获取课程小组
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/group/get")
    public Result<?> getCourseGroup(@PathVariable("courseId") long courseId) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        return Result.ok(courseService.getCourseGroup((int) courseId));
    }

    //添加课程小组
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @GetMapping("/{courseId}/project/{projectId}/group/add")
    public Result<?> addCourseGroup(@PathVariable("courseId") long courseId,
        @PathVariable("projectId") Integer projectId, @RequestParam("groupName") String name) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        courseService.addCourseGroup((int) courseId, name, projectId);
        return Result.ok("");
    }

    //获取某个作业
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/assignment/{assignmentId}")
    public Result<?> getHomework(@PathVariable("courseId") long courseId,
        @PathVariable("assignmentId") Integer assignmentId) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        return Result.ok(courseService.getHomework(assignmentId));
    }

    //获取某个项目
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/project/{projectId}")
    public Result<?> getProject(@PathVariable("courseId") long courseId,
        @PathVariable("projectId") Integer projectId) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        return Result.ok(courseService.getProject(projectId));
    }

    //修改课程作业信息
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/assignment/{assignmentId}/table/edit")
    public Result<?> modifyHomework(@PathVariable("courseId") long courseId,
        @PathVariable("assignmentId") Integer assignmentId,
        @RequestParam("assignmentName") String assignmentName, @RequestParam("deadline") String ddl,
        @RequestParam("attachment") String[] attachments,
        @RequestParam("description") String description) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        courseService.modifyHomework(assignmentName, ddl, description, merge(attachments),
            assignmentId);
        return Result.ok("");
    }

    //修改课程项目信息
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/project/{projectId}/table/edit")
    public Result<?> modifyProject(@PathVariable("courseId") long courseId,
        @PathVariable("projectId") Integer projectId,
        @RequestParam("projectName") String projectName, @RequestParam("deadline") String ddl,
        @RequestParam("attachment") String[] attachments,
        @RequestParam("description") String description, @RequestParam("state") String state) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        courseService.modifyProject(projectName, ddl, description, merge(attachments),
            (int) courseId, state, projectId);
        return Result.ok("");

    }

    //添加课程作业
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/assignment/table/add")
    public Result<?> addHomework(@PathVariable("courseId") long courseId,
        @RequestParam("assignmentName") String assignmentName, @RequestParam("deadline") String ddl,
        @RequestParam("attachment") String[] attachments,
        @RequestParam("description") String description, @RequestParam("isGroup") Integer isGroup) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        long uid = authFunctionality.getUser().getId().longValue();
        courseService.addHomework(assignmentName, ddl, description, merge(attachments),
            (int) courseId,
            isGroup, uid);
        return Result.ok("");
    }

    //添加课程项目
    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/project/table/add")
    public Result<?> addProject(@PathVariable("courseId") long courseId,
        @RequestParam("projectName") String projectName, @RequestParam("deadline") String ddl,
        @RequestParam("attachment") String[] attachments,
        @RequestParam("description") String description, @RequestParam("state") String state) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        courseService.addProject(projectName, ddl, description, merge(attachments),
            (int) courseId,
            state);
        return Result.ok("");

    }

    //获得课程所有作业
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/assignment/table")
    public Result<?> getCourseHomeworkTable(@PathVariable("courseId") long courseId) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        return Result.ok(courseService.getHomeworkTable((int) courseId));
    }

    //获得课程所有项目
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/project/table")
    public Result<?> getCourseProjectInfo(@PathVariable("courseId") long courseId) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        return Result.ok(courseService.getProjectInfo((int) courseId));
    }

    //获取我的课程
    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/user/class")
    public Result<?> getAllCourse() {
        long uid = authFunctionality.getUser().getId().longValue();  //获取自身用户
        List<CourseInfoDto> courseInfoDtoList = courseService.getUserCourse(uid);
        return Result.ok(courseInfoDtoList);
    }


    @PreAuthorize(ROLE_CHECK)
    @GetMapping("{cid}/info")
    public Result<?> getCourseProfile(@PathVariable("cid") long courseId) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        // TODO 获取此课程的详细信息
        return Result.ok(courseService.getCourseInfo((int) courseId));
    }

    @PreAuthorize(ROLE_CHECK)
    @DeleteMapping("{cid}/quit")
    public Result<?> quit(@PathVariable("cid") long courseId
    ) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        long uid = authFunctionality.getUser().getId().longValue();  //获取自身用户
        courseService.quitCourse(courseId, uid);
        return Result.ok(null);
    }

    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("{cid}/invite")
    public Result<?> addMember(@PathVariable("cid") long courseId,
        @RequestParam("sid") long sid,
        @RequestParam("authority") String authority
    ) {
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
    }

    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("{cid}/update/member/authority")
    public Result<?> changeAuthority(@PathVariable("cid") long courseId,
        @RequestParam("sid") long sid,
        @RequestParam("authority") String authority
    ) {
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
    }
}
