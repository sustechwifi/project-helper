package sustech.ooad.mainservice.mapper;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.Homework;

public interface HomeworkRepository extends JpaRepository<Homework, Integer> {

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into homework (name,attachment,description,ddl,courseid,isgroup,userid) values (?1,?2,?3,?4,?5,?6,?7);", nativeQuery = true)
    int addHomework(String name, String attachment, String description, String ddl,
        Integer courseId, Integer isGroup, Long userId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update homework set name=?1,ddl=?4,description=?3,attachment=?2 where id=?5 ", nativeQuery = true)
    int modifyHomework(String name, String attachment, String description, String ddl,
        Integer homeworkId);

    List<Homework> findAllByCourseid(Course course);

    Homework findHomeworkById(Integer id);

    Homework findHomeworkByName(String name);
}