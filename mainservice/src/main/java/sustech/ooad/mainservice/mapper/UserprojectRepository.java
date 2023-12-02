package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.model.Userproject;
import sustech.ooad.mainservice.model.UserprojectId;

public interface UserprojectRepository extends JpaRepository<Userproject, UserprojectId> {

    List<Userproject> findUserprojectsByUser(AuthUser user);
}