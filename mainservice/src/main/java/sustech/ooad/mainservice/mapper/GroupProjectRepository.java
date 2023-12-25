package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Group;
import sustech.ooad.mainservice.model.GroupProject;
import sustech.ooad.mainservice.model.GroupProjectId;

public interface GroupProjectRepository extends JpaRepository<GroupProject, GroupProjectId> {

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into group_project (groupid, projectid) values (?1,?2);", nativeQuery = true)
    int add(Integer groupId, Integer projectId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    void deleteGroupProjectByGroupid(Group group);
}