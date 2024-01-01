package sustech.ooad.mainservice.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sustech.ooad.mainservice.model.Submit;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class submitDto {
    Submit submit;
    List<attachment> attachment;
}
