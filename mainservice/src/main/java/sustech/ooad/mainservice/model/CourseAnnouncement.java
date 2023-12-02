package sustech.ooad.mainservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@Getter
@Setter
@Entity
@Table(name = "course_announcement")
public class CourseAnnouncement {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid")
    private AuthUser userUuid;

    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment")
    private submit attachment;

}