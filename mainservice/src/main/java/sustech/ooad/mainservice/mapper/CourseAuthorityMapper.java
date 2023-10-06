package sustech.ooad.mainservice.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import sustech.ooad.mainservice.model.CourseAuthority;

import java.util.List;

@Repository
public interface CourseAuthorityMapper {

    List<CourseAuthority> findUserCourses(@Param("uid") long uid);

    @Insert("insert into ooad_project_helper.public.course_authority (course_id, auth_user_id, authority) " +
            "values (#{cid},#{uid},#{authority})")
    void addRecord(@Param("cid") long cid,
                   @Param("uid") long uid,
                   @Param("authority") String authority);

    @Delete("delete from ooad_project_helper.public.course_authority u where " +
            "u.course_id = #{cid} and u.auth_user_id = #{uid}")
    void deleteRecord(@Param("cid") long cid, @Param("uid") long uid);

    @Update("update ooad_project_helper.public.course_authority  set authority = #{authority} " +
            "where auth_user_id = #{uid} and course_id = #{cid}")
    void updateRecord(@Param("cid") long cid,
                      @Param("uid") long uid,
                      @Param("authority") String authority);
}
