package sustech.ooad.mainservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "userproject")
public class Userproject {

    @EmbeddedId
    private UserprojectId id;

    @MapsId("uuid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uuid", nullable = false)
    private AuthUser user;

    @MapsId("projectid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projectid", nullable = false)
    private Project project;

    @Column(name = "exhibit", length = Integer.MAX_VALUE)
    private String exhibit;

}