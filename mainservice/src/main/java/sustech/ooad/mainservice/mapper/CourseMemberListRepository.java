package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.CourseMemberList;
import sustech.ooad.mainservice.model.CourseMemberListId;

public interface CourseMemberListRepository extends
    JpaRepository<CourseMemberList, CourseMemberListId> {

}