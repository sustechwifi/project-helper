package sustech.ooad.mainservice.service;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sustech.ooad.mainservice.mapper.AuthUserMapper;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.util.MailService;
import sustech.ooad.mainservice.util.QqMailServiceImpl;
import sustech.ooad.mainservice.util.Result;
@Slf4j
@Service
public class LoginUserService implements UserDetailsService {

    final PasswordEncoder encoder;

    final AuthUserMapper authUserMapper;
    public LoginUserService(PasswordEncoder encoder, AuthUserMapper authUserMapper) {
        this.encoder = encoder;
        this.authUserMapper = authUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserMapper.selectFormUser(username);
        return authUser;
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        AuthUser authUser = authUserMapper.selectEmailUser(email);
        return authUser;
    }

    @Resource
    MailService mailService;

    @Resource
    TemplateEngine templateEngine;

    public void sendCaptcha(String to,String code){
        //创建邮件正文
        Context context = new Context();
        context.setVariable("captcha", code);
        String emailContent = templateEngine.process("emailTemplate", context);
        mailService.sendHtmlMail(to,"Welcome to Project Helper ",emailContent);
    }

    public boolean checkUsername(String username) {
        AuthUser authUser = authUserMapper.selectFormUser(username);
        return authUser == null;
    }

    public boolean registerFormUser(String username, String password) {
        try {
            authUserMapper.saveFormUser(new AuthUser(username, password));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
