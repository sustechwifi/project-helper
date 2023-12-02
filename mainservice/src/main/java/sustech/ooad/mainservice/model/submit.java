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
@Table(name = "submit")
public class submit {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid")
    private AuthUser userUuid;

    @Column(name = "feedback", length = Integer.MAX_VALUE)
    private String feedback;

    @Column(name = "score")
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "attachment", length = Integer.MAX_VALUE)
    private String attachment;

    @Column(name = "isgroup")
    private Boolean isgroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupid")
    private Group groupid;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homework")
    private Homework homework;

}