package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into \"group\" (name,course_id,project_id) values (?1,?2,?3);", nativeQuery = true)
    int addGroup(String name, Integer courseId, Integer projectId);

}