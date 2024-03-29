package sustech.ooad.mainservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "\"time\"")
    private String time;

    @Column(name = "time_state", length = Integer.MAX_VALUE)
    private String timeState;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;


    @Column(name = "attachment", length = Integer.MAX_VALUE)
    private String attachment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeworkid")
    private Homework homeworkid;

}