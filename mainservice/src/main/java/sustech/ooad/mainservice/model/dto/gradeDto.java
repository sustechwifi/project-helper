package sustech.ooad.mainservice.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sustech.ooad.mainservice.model.Grade;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class gradeDto {

    Grade grade;
    List<attachment> attachment;

}
