package sustech.ooad.mainservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "course_authority")
public class CourseAuthority implements GrantedAuthority {

    @Id
    long id;
    @Column(name = "course_id")
    long courseId;
    @Column(name = "auth_user_id")
    long userId;
    @Column(name = "authority")
    String courseAuthority;

    @Override
    public String getAuthority() {
        return courseAuthority;
    }
}
