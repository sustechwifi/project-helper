package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Group;
import sustech.ooad.mainservice.model.Task;
import sustech.ooad.mainservice.model.TaskMem;
import sustech.ooad.mainservice.model.TaskMemId;

public interface TaskMemRepository extends JpaRepository<TaskMem, TaskMemId> {

    List<TaskMem> findTaskMemsByTaskid(Task task);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into task_mem (taskid, uuid) values (?1,?2);", nativeQuery = true)
    int addTaskMem(Integer taskId, Long uuid);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    void deleteTaskMemsByTaskid(Task task);


}