package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.model.Group;
import sustech.ooad.mainservice.model.GroupMemberList;
import sustech.ooad.mainservice.model.GroupMemberListId;

public interface GroupMemberListRepository extends
    JpaRepository<GroupMemberList, GroupMemberListId> {

    List<GroupMemberList> findGroupMemberListsByGroup(Group group);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    void deleteGroupMemberListsByGroup(Group group);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into group_member_list(group_id, user_uuid) VALUES (?1,?2)", nativeQuery = true)
    int addGroupMember(Integer groupId, Long uuid);

    GroupMemberList findGroupMemberListByUserUuid(AuthUser user);

    List<GroupMemberList> findGroupMemberListsByUserUuid(AuthUser user);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    void deleteGroupMemberListByGroupAndUserUuid(Group group, AuthUser user);
}