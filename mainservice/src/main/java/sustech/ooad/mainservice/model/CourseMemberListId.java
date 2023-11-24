package sustech.ooad.mainservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Embeddable
public class CourseMemberListId implements Serializable {

    private static final long serialVersionUID = 2024142255560096545L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "course_id", nullable = false)
    private Integer courseId;

    @jakarta.validation.constraints.NotNull
    @Column(name = "user_uuid", nullable = false)
    private BigDecimal userUuid;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        CourseMemberListId entity = (CourseMemberListId) o;
        return Objects.equals(this.userUuid, entity.userUuid) &&
            Objects.equals(this.courseId, entity.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid, courseId);
    }

}