package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.Project;
import sustech.ooad.mainservice.model.Share;

public interface ShareRepository extends JpaRepository<Share, Integer> {

    List<Share> findSharesByProject(Project project);
}