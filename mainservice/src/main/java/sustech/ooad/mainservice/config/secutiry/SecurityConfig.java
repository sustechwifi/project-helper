package sustech.ooad.mainservice.config.secutiry;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import sustech.ooad.mainservice.config.secutiry.captcha.CaptchaVerifyFilter;
import sustech.ooad.mainservice.config.secutiry.handler.*;

import static sustech.ooad.mainservice.config.secutiry.smtp.SmsLoginConfigurer.smsLogin;


@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {

    @Resource
    StringRedisTemplate stringRedisTemplate;


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置所有的Http请求必须认证
        http.authorizeHttpRequests()
                .requestMatchers(
                        "/email/login/**",
                        "/login/**",
                        "/register/**",
                        "/generate/**",
                        "/content/download/**",
                        "/ws/chat/**"
                ).permitAll()
                // 基于路径的访问控制
                //.requestMatchers("/user/**").hasRole("ROLE_User")
                .anyRequest().authenticated();

        // 开启表单登录
        http.formLogin()
//                .loginPage("/login.html") // 自定义登录页面（注意要同步配置loginProcessingUrl）
//                .loginProcessingUrl("/custom/login")  // 自定义登录处理URL
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new JsonAuthenticationSuccessHandler())
                .failureHandler(new JsonAuthenticationFailureHandler());

        http.addFilterBefore(new CaptchaVerifyFilter(
                        new JsonAuthenticationFailureHandler(),
                        stringRedisTemplate),
                UsernamePasswordAuthenticationFilter.class
        );

        // 开启 oauth2 登录
        http.oauth2Login()
                .successHandler(new JsonAuthenticationSuccessHandler())
                .failureHandler(new JsonAuthenticationFailureHandler());

        // 开启短信验证码登录  禁用写法：http.apply(smsLogin()).disable();
        // smsLogin()来自：import static com.pearl.security.auth.sms.SmsLoginConfigurer.smsLogin;
        http.apply(smsLogin())
                .successHandler(new JsonAuthenticationSuccessHandler())// 成功处理器
                .failureHandler(new JsonAuthenticationFailureHandler()) // 失败处理器
                .emailParameter("email") // 邮箱参数名称
                .codeParameter("code"); // 验证码参数名称

        // 登出配置
        http.logout()
                .clearAuthentication(true) // 清理Authentication ，默认true
                //.deleteCookies("jwt") // 删除指定 cookie
                .invalidateHttpSession(true) // 设置当前登录用户Session（保存登录后的用户信息）无效，默认true
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout", "GET")
                ))
                .logoutSuccessHandler(new JsonLogoutSuccessHandler());

        // 会话策略
        http.sessionManagement(session -> session
                // 固定会话攻击保护策略
                .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // 会话创建策略
                // 自定义非法会话处理
                .invalidSessionStrategy(new JsonInvalidSessionHandler())
        );


        // 开启Basic认证
        http.httpBasic();


        // 异常配置
        http.exceptionHandling()
                .accessDeniedHandler(new JsonAccessDeniedHandler()) // 权限不足
                .authenticationEntryPoint(new JsonAuthenticationEntryPoint()); // 认证入口


        // 启用跨域处理配置
        http.cors();

        // 关闭 CSRF
        http.csrf().disable();
        return http.build();
    }

    /**
     * 密码器
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

