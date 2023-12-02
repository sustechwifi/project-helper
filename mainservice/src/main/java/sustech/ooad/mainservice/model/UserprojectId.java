package sustech.ooad.mainservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Embeddable
public class UserprojectId implements Serializable {

    private static final long serialVersionUID = 9177533477980539229L;
    @NotNull
    @Column(name = "uuid", nullable = false)
    private BigDecimal uuid;

    @NotNull
    @Column(name = "projectid", nullable = false)
    private Integer projectid;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        UserprojectId entity = (UserprojectId) o;
        return Objects.equals(this.uuid, entity.uuid) &&
            Objects.equals(this.projectid, entity.projectid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, projectid);
    }

}