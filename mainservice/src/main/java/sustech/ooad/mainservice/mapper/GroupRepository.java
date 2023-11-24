package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {

}