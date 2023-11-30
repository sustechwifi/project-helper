package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findProjectsByCourse(Course course);

    Project findByCourseAndName(Course course, String name);

}