package sustech.ooad.mainservice.mapper;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.AnnouncementUser;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.model.CourseAnnouncement;

public interface AnnouncementUserRepository extends
    JpaRepository<AnnouncementUser, Integer> {

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into announcement_user(announcementid, uuid) VALUES (?1,?2)", nativeQuery = true)
    void addAnnouncementUser(Integer announcementId, long uuid);

    List<AnnouncementUser> findAnnouncementUsersByUuid(AuthUser user);

}