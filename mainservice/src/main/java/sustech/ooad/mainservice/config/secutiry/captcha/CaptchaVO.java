package sustech.ooad.mainservice.config.secutiry.captcha;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaptchaVO implements Serializable {
    // 唯一ID
    private String id;
    // 验证码图片 Base64
    private String base64;
}
