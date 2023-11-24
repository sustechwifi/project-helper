package sustech.ooad.mainservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @OneToMany(mappedBy = "course")
    private Set<CourseAnnouncement> courseAnnouncements = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "course_member_list",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "user_uuid"))
    private Set<AuthUser> oauthUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<Group> groups = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<Homework> homework = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<Project> projects = new LinkedHashSet<>();

}