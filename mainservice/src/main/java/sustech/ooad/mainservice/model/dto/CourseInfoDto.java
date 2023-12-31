package sustech.ooad.mainservice.model.dto;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sustech.ooad.mainservice.model.Course;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfoDto {
    Integer id;
    String Course;
    String auth;
    List<String> ta;
    String teacher;
}
