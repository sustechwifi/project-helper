package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Group;
import sustech.ooad.mainservice.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findTasksByGroupid(Group group);

    Task findTaskById(Integer id);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into task (name, ddl, attachment, description, projectid, groupid) values (?1,?2,?3,?4,?5,?6);", nativeQuery = true)
    int addTask(String name, String ddl, String attachment, String description, Integer projectId,
        Integer groupId);

    Task findTaskByGroupidAndName(Group group, String name);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update task set name=?1,ddl=?2,attachment=?3,description=?4 where id=?5", nativeQuery = true)
    int modifyTask(String name, String ddl, String attachment, String description, Integer id);
}