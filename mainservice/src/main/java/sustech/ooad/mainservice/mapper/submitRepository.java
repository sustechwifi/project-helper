package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Course;
import sustech.ooad.mainservice.model.submit;

public interface submitRepository extends JpaRepository<submit, Integer> {




}