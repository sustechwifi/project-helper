package sustech.ooad.mainservice.service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sustech.ooad.mainservice.mapper.AuthUserRepository;
import sustech.ooad.mainservice.mapper.UserprojectRepository;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.model.Userproject;
import sustech.ooad.mainservice.model.dto.exhibitProjectDto;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;
import sustech.ooad.mainservice.util.auth.AuthFunctionalityImpl;

@Service
public class UserService {

    @Autowired
    UserprojectRepository userprojectRepository;
    @Autowired
    AuthUserRepository authUserRepository;

    @Resource
    AuthFunctionalityImpl authFunctionality;

    public List<exhibitProjectDto> getUserProject() {
        List<Userproject> userprojectList = userprojectRepository.findUserprojectsByUser(
            authFunctionality.getUser());
        List<exhibitProjectDto> exhibitProjectDtoList = new ArrayList<>();
        for (Userproject u : userprojectList) {
            exhibitProjectDto temp = new exhibitProjectDto();
            temp.setProject(u.getProject());
            temp.setExhibit(u.getExhibit());
            exhibitProjectDtoList.add(temp);
        }
        return exhibitProjectDtoList;
    }
}
