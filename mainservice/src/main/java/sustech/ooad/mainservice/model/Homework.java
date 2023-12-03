package sustech.ooad.mainservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "homework")
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "attachment", length = Integer.MAX_VALUE)
    private String attachment;

    @Column(name = "ddl", length = Integer.MAX_VALUE)
    private String ddl;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "isgroup")
    private Integer isgroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseid")
    private Course courseid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private AuthUser userid;

}