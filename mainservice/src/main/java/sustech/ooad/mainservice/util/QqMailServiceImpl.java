package sustech.ooad.mainservice.util;


import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Properties;

/**
 * 实现qq邮箱发送
 */
@Component
public class QqMailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Resource
    JavaMailSenderImpl sender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送文本邮件
     * @param to
     * @param subject
     * @param content
     */


    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        MimeMessage message = sender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            sender.send(message);
            logger.info("html邮件发送成功");
        } catch (MessagingException e) {
            logger.error("发送html邮件时发生异常！", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from); // 发送人
            helper.setTo(to); // 收件人
            helper.setSubject(subject); // 标题
            helper.setText(content); // 内容
            sender.send(message);
            logger.info("简单邮件已经发送。");
        } catch (MessagingException e) {
            logger.error("发送简单邮件时发生异常！", e);
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常！", e);
            e.printStackTrace();
        }
    }


 }
