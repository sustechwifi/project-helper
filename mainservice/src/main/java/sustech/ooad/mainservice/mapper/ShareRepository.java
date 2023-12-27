package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Project;
import sustech.ooad.mainservice.model.Share;

public interface ShareRepository extends JpaRepository<Share, Integer> {

    List<Share> findSharesByProject(Project project);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into Share(group_id, attachment, project_id) values (?1,?2,?3)", nativeQuery = true)
    void addShare(Integer groupId, String attachment, Integer projectId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "delete from Share where id=?1", nativeQuery = true)
    void deleteShare(Integer shareId);

}