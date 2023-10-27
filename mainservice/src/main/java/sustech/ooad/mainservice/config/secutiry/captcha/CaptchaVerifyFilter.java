package sustech.ooad.mainservice.config.secutiry.captcha;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;
import sustech.ooad.mainservice.config.secutiry.captcha.CaptchaVerifyException;

import java.io.IOException;

public class CaptchaVerifyFilter extends GenericFilterBean {
    // 请求匹配器
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/login", "POST");
    // 用户输入的验证码
    public static final String FORM_CAPTCHA_KEY = "captcha";
    // 验证码ID
    public static final String FORM_CAPTCHA_ID_KEY = "captchaId";
    // 认证失败处理器
    private final AuthenticationFailureHandler failureHandler;
    // RedisTemplate
    private final StringRedisTemplate stringRedisTemplate;

    public CaptchaVerifyFilter(AuthenticationFailureHandler failureHandler, StringRedisTemplate redisTemplate) {
        this.failureHandler = failureHandler;
        this.stringRedisTemplate = redisTemplate;
    }

    /**
     * 进入用户名密码认证之前，对验证码进行校验
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (DEFAULT_ANT_PATH_REQUEST_MATCHER.matches((HttpServletRequest) request)) {
            // /login请求时，执行验证码校验
            try {
                // 获取请求参数
                String captcha = request.getParameter(FORM_CAPTCHA_KEY);
                String captchaId = request.getParameter(FORM_CAPTCHA_ID_KEY);
                if (StrUtil.isEmpty(captcha) || StrUtil.isEmpty(captchaId)) {
                    throw new CaptchaVerifyException("验证码参数为空");
                }
                // 未查询到ID对应的验证码
                String cacheData = stringRedisTemplate.opsForValue().get(captchaId);
                if (StrUtil.isEmpty(cacheData)) {
                    throw new CaptchaVerifyException("验证码已过期");
                }
                // 校验验证码是否输入正确
                if (!cacheData.equals(captcha)) {
                    throw new CaptchaVerifyException("验证码输入错误");
                }
                filterChain.doFilter(request, response);
            } catch (AuthenticationException e) {
                failureHandler.onAuthenticationFailure((HttpServletRequest) request, (HttpServletResponse) response, e);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
