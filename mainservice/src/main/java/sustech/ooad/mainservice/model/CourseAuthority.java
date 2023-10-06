package sustech.ooad.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseAuthority implements GrantedAuthority {
    long id;
    long courseId;
    long userId;
    String courseAuthority;

    @Override
    public String getAuthority() {
        return courseAuthority;
    }
}
