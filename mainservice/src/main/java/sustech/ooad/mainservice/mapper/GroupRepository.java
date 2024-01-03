package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into \"group\" (name,course_id,teacher_uuid,pre_time,capacity,introduction) values (?1,?2,?3,?4,?5,?6);", nativeQuery = true)
    int addGroup(String name, Integer courseId, Long uuid, String preTime,
        Integer capacity, String introduction);

    List<Group> findGroupsByCourse(Course course);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update \"group\" set name=?1,teacher_uuid=?2,pre_time=?3,capacity=?4,ddl=?6,introduction=?7 where id=?5", nativeQuery = true)
    int modifyGroup(String name, Long uuid, String preTime, Integer capacity, Integer groupId,
        String ddl, String introduction);

    Group findGroupById(Integer id);

    Group findGroupByNameAndCourse(String name, Course course);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    void deleteGroupById(Integer id);
}