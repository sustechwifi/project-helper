package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.GroupMemberList;
import sustech.ooad.mainservice.model.GroupMemberListId;

public interface GroupMemberListRepository extends
    JpaRepository<GroupMemberList, GroupMemberListId> {

}