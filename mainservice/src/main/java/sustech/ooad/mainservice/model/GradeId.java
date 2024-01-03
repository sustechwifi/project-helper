package sustech.ooad.mainservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Embeddable
public class GradeId implements Serializable {

    private static final long serialVersionUID = -9147233388838867966L;
    @NotNull
    @Column(name = "homeworkid", nullable = false)
    private Integer homeworkid;

    @NotNull
    @Column(name = "url", nullable = false, length = Integer.MAX_VALUE)
    private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        GradeId entity = (GradeId) o;
        return Objects.equals(this.homeworkid, entity.homeworkid) &&
            Objects.equals(this.url, entity.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeworkid, url);
    }

}