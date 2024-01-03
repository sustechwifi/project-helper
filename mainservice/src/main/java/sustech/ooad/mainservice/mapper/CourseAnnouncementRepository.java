package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.AnnouncementUser;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.CourseAnnouncement;

public interface CourseAnnouncementRepository extends JpaRepository<CourseAnnouncement, Integer> {

    List<CourseAnnouncement> findCourseAnnouncementsByCourse(Course course);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into course_announcement(course_id, user_uuid, description) VALUES (?1,?2,?3)", nativeQuery = true)
    void addAnnouncement(long courseId, long uuid, String description);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update course_announcement set description=?1 where id=?2", nativeQuery = true)
    void modifyAnnouncement(String description, Integer id);

    @Query(value = "select * from course_announcement order by id desc limit 1", nativeQuery = true)
    CourseAnnouncement findLatestAnnouncement();

    List<CourseAnnouncement> findCourseAnnouncementsById(Integer id);
}