package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Project findProjectByCourse(Course course);
}