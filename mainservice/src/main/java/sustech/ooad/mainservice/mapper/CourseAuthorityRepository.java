package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.CourseAuthority;

public interface CourseAuthorityRepository extends JpaRepository<CourseAuthority, Long> {

    List<CourseAuthority> findCourseAuthoritiesByUserId(long uuid);

    List<CourseAuthority> findCourseAuthoritiesByCourseIdAndCourseAuthority(long courseId,String auth);
}