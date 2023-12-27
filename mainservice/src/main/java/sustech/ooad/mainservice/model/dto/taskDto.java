package sustech.ooad.mainservice.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sustech.ooad.mainservice.model.Task;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class taskDto {
    Task task;
    List<Long> member;
}
