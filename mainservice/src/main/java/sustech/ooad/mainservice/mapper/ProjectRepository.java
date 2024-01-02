package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.Homework;
import sustech.ooad.mainservice.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findProjectsByCourse(Course course);

    Project findByCourseAndName(Course course, String name);

    Project findProjectById(Integer id);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update project set time=?1 where id=?2 ", nativeQuery = true)
    int modifyddl(String time, Integer id);

    Project findProjectByHomeworkid(Homework homework);
}