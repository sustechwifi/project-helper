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
public class GroupMemberListId implements Serializable {

    private static final long serialVersionUID = 4716562382583175136L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "group_id", nullable = false)
    private Integer groupId;

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
        GroupMemberListId entity = (GroupMemberListId) o;
        return Objects.equals(this.groupId, entity.groupId) &&
            Objects.equals(this.userUuid, entity.userUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userUuid);
    }

}