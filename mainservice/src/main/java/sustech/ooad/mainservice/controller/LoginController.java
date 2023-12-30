package sustech.ooad.mainservice.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.service.LoginUserService;
import sustech.ooad.mainservice.util.Result;
import sustech.ooad.mainservice.config.secutiry.captcha.CaptchaVO;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static sustech.ooad.mainservice.config.secutiry.handler.OauthAuthenticationSuccessHandler.*;
import static sustech.ooad.mainservice.util.ConstantField.*;


@Slf4j
@RequestMapping("/")
@RestController
public class LoginController {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    LoginUserService loginUserService;



    @GetMapping("/generate/echo")
    public String echo(@RequestParam String word){
        log.info("============ [echo] ============\n"+word);
        return word;
    }

    @GetMapping("/callback/oauth2")
    public Result<?> oauth2Callback() throws IOException {
        if (!oauthAuthenticationStack.isEmpty()){
            var auth = oauthAuthenticationStack.pollLast();
            AuthUser authUser = (AuthUser) auth.getPrincipal();
            return Result.ok(JSONUtil.toJsonStr(authUser));
        }else {
            return Result.err(402,"用户未同意授权");
        }
    }



    @GetMapping("/auth-info")
    public Result<?> info(Authentication authentication){
        if (authentication.getPrincipal() instanceof AuthUser){
            return Result.ok(authentication.getPrincipal());
        }else if(authentication.getDetails() instanceof AuthUser){
            return Result.ok(authentication.getDetails());
        }else {
            return Result.ok(authentication);
        }
    }

    @GetMapping("/generate/email/captcha")
    public Result<?> msmCaptcha(@RequestParam("email") String email) {
        log.info(email + "请求获取验证码");
        // 2. 以邮箱号为KEY，验证码为值，存入Redis，过期时间15分钟
        String code = RandomUtil.randomNumbers(6);
        loginUserService.sendCaptcha(email,code);
        // 存入 redis
        stringRedisTemplate.opsForValue().set(EMAIL_LOGIN+email, code, FIFTEEN_MINUTES);
        log.info(email + "生成验证码：" + code);
        // 3. 返回过期时间，方便前端提示多少时间内有效
        return Result.ok(15);
    }

    @GetMapping("/generate/captcha")
    public Result<?> generateCaptcha() {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        // 验证码
        String code = lineCaptcha.getCode();
        log.info("生成验证码：" + lineCaptcha.getCode());
        // 验证码图片BASE64
        String imageBase64 = lineCaptcha.getImageBase64Data();
        // 创建验证码对象
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setId(UUID.randomUUID().toString());
        captchaVO.setBase64(imageBase64);
        // 缓存验证码，10分钟有效
        stringRedisTemplate.opsForValue().set(captchaVO.getId(), code, ONE_MINUTES);
       // stringRedisTemplate.opsForValue().set("superkey", "super");
        return Result.ok(captchaVO);
    }

    @GetMapping("/register/check/username")
    public Result<?> checkUsername(@RequestParam("username") String username){
        if (loginUserService.checkUsername(username)){
            return Result.ok("valid username");
        }else {
            return Result.err(REPEATED_USERNAME,"repeated username");
        }
    }

    @PostMapping("/register")
    public Result<?> formRegister(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("captchaId") String captchaId,
            @RequestParam("captcha") String captcha
    ){
        if (Objects.equals(stringRedisTemplate.opsForValue().get(captchaId), captcha)){
            if (loginUserService.registerFormUser(username,password)) {
                return Result.ok(null);
            }else {
                return Result.err(REGISTER_ERR,"注册失败");
            }
        }else {
            return Result.err(CAPTCHA_ERR,"验证码错误");
        }
    }

}



