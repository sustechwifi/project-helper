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
public class GroupProjectId implements Serializable {

    private static final long serialVersionUID = -3987874727558401457L;
    @NotNull
    @Column(name = "groupid", nullable = false)
    private Integer groupid;

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
        GroupProjectId entity = (GroupProjectId) o;
        return Objects.equals(this.groupid, entity.groupid) &&
            Objects.equals(this.projectid, entity.projectid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupid, projectid);
    }

}