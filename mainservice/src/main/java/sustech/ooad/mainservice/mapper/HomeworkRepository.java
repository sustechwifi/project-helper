package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Homework;

public interface HomeworkRepository extends JpaRepository<Homework, Integer> {

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into homework (name,attachment,description,ddl,courseid,isgroup,userid) values (?1,?2,?3,?4,?5,?6,?7);", nativeQuery = true)
    int addHomework(String name, String attachment, String description, String ddl,
        Integer courseId, Integer isGroup, Long userId);
}