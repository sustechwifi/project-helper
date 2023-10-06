package sustech.ooad.mainservice.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import sustech.ooad.mainservice.model.AuthUser;



@Repository
public interface AuthUserMapper {
    AuthUser selectThirdUser(@Param("platformId") String platformId, @Param("username") String username);
    AuthUser selectFormUser(@Param("username") String username);
    AuthUser selectOneById(@Param("id") Long id);

    AuthUser selectEmailUser(@Param("email") String email);

    AuthUser selectByEmail(@Param("email") String email);

    @Update("update ooad_project_helper.public.oauth_user  set authority = #{role} where uuid = #{uid}")
    void updateRole(@Param("role") String role,@Param("uid") Long uid);

    @Insert("insert into ooad_project_helper.public.oauth_user" +
            " (login, name,password,avatar_url) " +
            "values (#{user.login},#{user.name},#{user.password},#{user.avatar})")
    void saveFormUser(@Param("user") AuthUser user);


    @Insert("insert into ooad_project_helper.public.oauth_user" +
            "(login, avatar_url, name, email,  password,platform) " +
            "VALUES (#{user.login},#{user.avatar},#{user.name},#{user.email},#{user.password},#{user.platform})")
    void saveThirdUser(@Param("user") AuthUser user);


    @Update("update ooad_project_helper.public.oauth_user " +
            "set name = #{user.name}, " +
            "avatar_url = #{user.avatar},sex = #{user.sex}," +
            "introduction = #{user.introduction}," +
            "grade = #{user.grade},major = #{user.major},college = #{user.college} where uuid = #{user.id}")
    void updateInfo(@Param("user") AuthUser user);

    @Select("select count(*) from ooad_project_helper.public.oauth_user u where u.email = #{email}")
    long checkEmailHasBound(@Param("email") String email);

    @Update("update ooad_project_helper.public.oauth_user set email = #{email} where uuid = #{uid}")
    void bindEmail(@Param("uid") long uid, @Param("email") String email);
}
