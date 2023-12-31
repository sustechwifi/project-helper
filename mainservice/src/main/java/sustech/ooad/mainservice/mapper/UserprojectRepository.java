package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.model.Userproject;
import sustech.ooad.mainservice.model.UserprojectId;

public interface UserprojectRepository extends JpaRepository<Userproject, UserprojectId> {

    List<Userproject> findUserprojectsByUser(AuthUser user);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into userproject(uuid, projectid, exhibit) values (?1,?2,'not exhibit')", nativeQuery = true)
    int addUserProject(long uuid, Integer projectId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(nativeQuery = true, value = "update userproject set exhibit='exhibit' where uuid=?1 and projectid=?2")
    int modifyUserProject(long uuid, Integer projectId);
}