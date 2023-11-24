package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.CourseAuthority;

public interface CourseAuthorityRepository extends JpaRepository<CourseAuthority, Long> {

}