package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.CourseAnnouncement;

public interface CourseAnnouncementRepository extends JpaRepository<CourseAnnouncement, Integer> {

}