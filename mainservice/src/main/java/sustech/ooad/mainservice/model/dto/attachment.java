package sustech.ooad.mainservice.model.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class attachment {

    String attachment;
    String name;

    public static List<attachment> divide(String str) {
        String[] list = str.split(";");
        List<attachment> attachmentList = new ArrayList<>();
        for (String s : list) {
            attachment a = new attachment();
            a.attachment = s;
            String[] temp = s.split("/");
            a.name = temp[temp.length - 1];
            attachmentList.add(a);
        }
        return attachmentList;
    }

}
