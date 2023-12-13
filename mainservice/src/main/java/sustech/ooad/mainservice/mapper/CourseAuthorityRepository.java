package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.CourseAuthority;

public interface CourseAuthorityRepository extends JpaRepository<CourseAuthority, Long> {

    List<CourseAuthority> findCourseAuthoritiesByUserId(long uuid);

    List<CourseAuthority> findCourseAuthoritiesByCourseIdAndCourseAuthority(long courseId,
        String auth);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into course_authority(course_id, auth_user_id, authority) values(?1,?2,?3) ", nativeQuery = true)
    int addCourseMember(Integer courseId, Long userId, String auth);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update course_authority set auth_user_id=?1 where authority='course teacher' and course_id=?2 ", nativeQuery = true)
    int modifyCourseTeacher(Long userId, Integer courseId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    void deleteCourseAuthoritiesByCourseIdAndCourseAuthority(long courseId, String auth);

}