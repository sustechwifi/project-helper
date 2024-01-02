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
@Table(name = "\"group\"")
public class Group {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;


    @Column(name = "capacity")
    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_uuid")
    private AuthUser teacherUuid;

    @Column(name = "pre_time", length = Integer.MAX_VALUE)
    private String preTime;

    @Column(name = "ddl", length = Integer.MAX_VALUE)
    private String ddl;

}