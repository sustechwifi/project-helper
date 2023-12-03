package sustech.ooad.mainservice.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sustech.ooad.mainservice.model.Homework;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkDto {

    Homework homework;
    List<attachment> attachment;
}
