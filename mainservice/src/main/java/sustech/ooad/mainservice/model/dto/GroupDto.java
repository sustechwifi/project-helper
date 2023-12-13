package sustech.ooad.mainservice.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sustech.ooad.mainservice.model.Group;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {

    Group group;
    List<Long> member;
}
