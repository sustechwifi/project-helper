package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.Homework;

public interface HomeworkRepository extends JpaRepository<Homework, Integer> {

    List<Homework> findAllByCourse(Course course);
}