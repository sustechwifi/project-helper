package sustech.ooad.mainservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sustech.ooad.mainservice.model.Course;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class noticeDto {
    String description;
    Integer id;
    Course course;
    long uuid;
    String user_name;
}
