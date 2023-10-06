package sustech.ooad.mainservice.util;

public interface MailService {
    void sendHtmlMail(String to, String subject, String content);
    void sendSimpleMail(String to, String subject, String content);

}
