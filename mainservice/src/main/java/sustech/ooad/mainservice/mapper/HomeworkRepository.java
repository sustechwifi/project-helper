package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.Homework;

public interface HomeworkRepository extends JpaRepository<Homework, Integer> {

}