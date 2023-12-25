package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.submit;

public interface submitRepository extends JpaRepository<submit, Integer> {

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into submit (user_uuid,course_id, attachment, isgroup, description, homework) values (?1,?2,?3,false,?4,?5);", nativeQuery = true)
    int addUserSubmit(long uuid, long courseId, String attachment, String description,
        Integer homeworkId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into submit (course_id, attachment, isgroup, description, groupid, homework) values (?1,?2,true,?3,?4,?5);", nativeQuery = true)
    int addGroupSubmit(long courseId, String attachment, String description, Integer groupId,
        Integer homework);
}