package sustech.ooad.mainservice.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sustech.ooad.mainservice.model.Project;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class exhibitProjectDto {

    Project project;
    String exhibit;
    List<attachment> attachment;
}
