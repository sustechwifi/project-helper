package sustech.ooad.mainservice.service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sustech.ooad.mainservice.mapper.AuthUserMapper;
import sustech.ooad.mainservice.mapper.AuthUserRepository;
import sustech.ooad.mainservice.mapper.UserprojectRepository;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.model.Userproject;
import sustech.ooad.mainservice.model.dto.exhibitProjectDto;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;
import sustech.ooad.mainservice.util.auth.AuthFunctionalityImpl;
import sustech.ooad.mainservice.model.dto.attachment;

@Service
public class UserService {

    @Autowired
    UserprojectRepository userprojectRepository;
    @Autowired
    AuthUserRepository authUserRepository;

    @Resource
    AuthFunctionalityImpl authFunctionality;

    @Resource
    AuthUserMapper authUserMapper;

    public List<exhibitProjectDto> getUserProject() {
        List<Userproject> userprojectList = userprojectRepository.findUserprojectsByUser(
            authFunctionality.getUser());
        List<exhibitProjectDto> exhibitProjectDtoList = new ArrayList<>();
        for (Userproject u : userprojectList) {
            exhibitProjectDtoList.add(new exhibitProjectDto(u.getProject(), u.getExhibit(),
                attachment.divide(u.getProject().getAttachment())));
        }
        return exhibitProjectDtoList;
    }


    public List<AuthUser> getAllUsers(){
        return authUserMapper.getAllUser();
    }


    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean editUserInfo(AuthUser user){
        try {
            System.out.println(user);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            authUserMapper.updateUserByAdmin(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
