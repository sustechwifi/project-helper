package sustech.ooad.mainservice.config.secutiry.captcha;

import org.springframework.security.core.AuthenticationException;

public class CaptchaVerifyException extends AuthenticationException {

    public CaptchaVerifyException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CaptchaVerifyException(String msg) {
        super(msg);
    }
}
