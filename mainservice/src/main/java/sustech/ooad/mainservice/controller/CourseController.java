package sustech.ooad.mainservice.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/assignment/table/add")
    public Result<?> addProject(@PathVariable("courseId") long courseId,
        @RequestParam("assignmentName") String assignmentName, @RequestParam("deadline") String ddl,
        @RequestParam("attachment") String[] attachments,
        @RequestParam("description") String description, @RequestParam("isGroup") Integer isGroup) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        long uid = authFunctionality.getUser().getId();
        courseService.addHomework(assignmentName, ddl, description, merge(attachments),
            (int) courseId,
            isGroup, uid);
        return Result.ok("");
    }

    @PreAuthorize(ROLE_CHECK_TEACHER)
    @PostMapping("/{courseId}/project/table/add")
    public Result<?> addProject(@PathVariable("courseId") long courseId,
        @RequestParam("projectName") String projectName, @RequestParam("deadline") String ddl,
        @RequestParam("attachment") String[] attachments, @RequestParam("group") String[] group,
        @RequestParam("description") String description, @RequestParam("state") String state) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        courseService.addProject(projectName, ddl, description, merge(attachments), group,
            (int) courseId,
            state);
        return Result.ok("");

    }

    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/assignment/table")
    public Result<?> getCourseHomeworkTable(@PathVariable("courseId") long courseId) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        return Result.ok(courseService.getHomeworkTable((int) courseId));
    }

    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/{courseId}/project/table")
    public Result<?> getCourseProjectInfo(@PathVariable("courseId") long courseId) {
        boolean valid = authFunctionality.inCourse(courseId);
        if (!valid) {
            return Result.err(ACCESS_COURSE_DENIED, "无法访问此课程");
        }
        return Result.ok(courseService.getProjectInfo((int) courseId));
    }

    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/all")
    public Result<?> getAllCourse() {
        // TODO 根据<课程id,权限> 获取当前用户的所有课程信息
        Map<Long, String> userCourses = authFunctionality.getUserCourses();
        throw new UnsupportedOperationException();
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
        long uid = authFunctionality.getUser().getId();  //获取自身用户
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
