package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sustech.ooad.mainservice.model.Grade;
import sustech.ooad.mainservice.model.GradeId;
import sustech.ooad.mainservice.model.Homework;

public interface GradeRepository extends JpaRepository<Grade, GradeId> {

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "insert into grade(homeworkid, url) values (?1,?2)", nativeQuery = true)
    void addGrade(Integer homeworkId, String url);

    Grade findGradeByHomeworkid(Homework homework);

}