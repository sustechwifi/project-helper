package sustech.ooad.mainservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sustech.ooad.mainservice.util.MailService;

@SpringBootTest
class MainserviceApplicationTests {

    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    MailService mailService;

    @Test
    void contextLoads() {
    }

    @Test
    public void sendTemplateMail() {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("captcha", "123456");
        String emailContent = templateEngine.process("emailTemplate", context);

        mailService.sendHtmlMail("12110919@mail.sustech.edu.cn","Project Helper Register",emailContent);
    }


}
