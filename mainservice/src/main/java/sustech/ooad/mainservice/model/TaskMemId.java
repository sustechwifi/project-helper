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
public class TaskMemId implements Serializable {

    private static final long serialVersionUID = 5184505335331479122L;
    @NotNull
    @Column(name = "taskid", nullable = false)
    private Integer taskid;

    @NotNull
    @Column(name = "uuid", nullable = false)
    private BigDecimal uuid;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        TaskMemId entity = (TaskMemId) o;
        return Objects.equals(this.uuid, entity.uuid) &&
            Objects.equals(this.taskid, entity.taskid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, taskid);
    }

}