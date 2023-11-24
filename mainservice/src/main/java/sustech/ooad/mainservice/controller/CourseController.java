package sustech.ooad.mainservice.controller;

import jakarta.annotation.Resource;
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

    @PreAuthorize(ROLE_CHECK)
    @GetMapping("/all")
    public Result<?> getAllCourse() {
        // TODO 根据<课程id,权限> 获取当前用户的所有课程信息
        Map<Long, String> userCourses = authFunctionality.getUserCourses();
        throw new UnsupportedOperationException();
    }


    @PreAuthorize(ROLE_CHECK)
    @GetMapping("{cid}/home")
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
        long uid = authFunctionality.getUser().getId();
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
