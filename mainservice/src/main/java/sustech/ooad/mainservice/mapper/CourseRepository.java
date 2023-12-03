package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.CourseAnnouncement;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    Course findCourseById(Integer id);

    Course findCourseByName(String name);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into project(name, course_id, time, time_state, description, attachment) values (?1,?2,?3,?4,?5,?6);", nativeQuery = true)
    int addProject(String name, Integer courseId, String ddl, String state, String description,
        String attachment);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update project set name=?1,course_id=?2,time=?3,time_state=?4,description=?5,attachment=?6 where id=?7", nativeQuery = true)
    int modifyProject(String name, Integer courseId, String ddl, String state, String description,
        String attachment, Integer projectId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into course(name) VALUES (?1)", nativeQuery = true)
    int addCourse(String name);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update course set name=?1 where id=?2", nativeQuery = true)
    int modifyCourse(String name, Integer id);

}