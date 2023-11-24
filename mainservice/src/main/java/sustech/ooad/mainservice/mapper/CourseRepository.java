package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.CourseAnnouncement;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    Course findCourseById(Integer id);
}