package sustech.ooad.mainservice.model.dto;

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
}
