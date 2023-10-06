package sustech.ooad.mainservice.config.secutiry.smtp;

import cn.hutool.core.util.StrUtil;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import sustech.ooad.mainservice.service.LoginUserService;

import static sustech.ooad.mainservice.util.ConstantField.EMAIL_LOGIN;

public class SmsAuthenticationProvider implements AuthenticationProvider {
    private final LoginUserService userDetailsServiceImpl;

    private final StringRedisTemplate stringRedisTemplate;

    public SmsAuthenticationProvider(LoginUserService userDetailsServiceImpl, StringRedisTemplate stringRedisTemplate) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        Object principal = authentication.getPrincipal();// 获取凭证也就是用户的邮箱
        String email = "";
        if (principal instanceof String) {
            email = (String) principal;
        }
        String inputCode = (String) authentication.getCredentials(); // 获取输入的验证码
        // 1. 检验Redis的验证码
        String key = EMAIL_LOGIN+email;
        String redisCode = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(redisCode)) {
            throw new BadCredentialsException("验证码已经过期或尚未发送，请重新发送验证码");
        }
        if (!inputCode.equals(redisCode)) {
            throw new BadCredentialsException("输入的验证码不正确，请重新输入");
        }
        //stringRedisTemplate.delete(key);// 删除验证码
        // 2. 根据邮箱查询用户信息
        UserDetails userDetails = userDetailsServiceImpl.loadUserByEmail(email);
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("email 用户不存在，请注册");
        }
        // 3. 创建已认证对象
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(principal,userDetails, userDetails.getAuthorities());
        authenticationResult.setDetails(userDetails);
        return authenticationResult;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        // 当 SmsAuthenticationToken 认证时，匹配该类
        return SmsAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
