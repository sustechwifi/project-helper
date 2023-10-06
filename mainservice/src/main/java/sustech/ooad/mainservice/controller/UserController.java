package sustech.ooad.mainservice.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.service.AuthService;
import sustech.ooad.mainservice.util.MailService;
import sustech.ooad.mainservice.util.Result;
import sustech.ooad.mainservice.util.auth.AuthFunctionality;

import static sustech.ooad.mainservice.util.ConstantField.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    AuthService authService;

    @Resource
    AuthFunctionality authFunctionality;

    @Resource
    MailService mailService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    // 认证角色：发送邮箱验证码
    @GetMapping("/auth/role")
    public Result<?> genAuthRole(@RequestParam("email") String email) {
        log.info(email + "请求获取验证码");
        // 2. 以邮箱号为KEY，验证码为值，存入Redis，过期时间5分钟
        String code = RandomUtil.randomNumbers(6);
        mailService.sendSimpleMail(email,"Project helper - Auth role",code);
        // 存入 redis
        stringRedisTemplate.opsForValue().set(EMAIL_AUTH_ROLE+email, code, FIVE_MINUTES);
        log.info(email + "生成验证码：" + code);
        // 3. 返回过期时间，方便前端提示多少时间内有效
        return Result.ok(5);
    }

    // 只能认证一次角色
    @PreAuthorize("hasRole('UNKNOWN')")
    @PostMapping("/auth/role")
    public Result<?> authRole(@RequestParam("code") String code,
                              @RequestParam("role") String role){
        String email = authFunctionality.getUser().getEmail();
        String key = EMAIL_AUTH_ROLE+email;
        String s = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(s) || !s.equals(code)){
            return Result.err(CAPTCHA_ERR,"invalid check code");
        }
        if (authService.authRole(email,role)){
            return Result.ok(role);
        }else {
            return Result.err(AUTH_ROLE_DENIED,"auth fail: role="+role);
        }
    }


//    @PostMapping("/auth/course/{course}")
//    public Result< ? > updateCourseAuthority(@PathVariable("course")String course,
//                                       @RequestParam("authority")String authority,
//                                       @RequestParam("code") String code
//    ) {
//        // TODO 修改角色的课程权限
//        return Result.ok(null);
//    }



    // 修改/绑定邮箱
    @GetMapping("/bind/email")
    public Result<?> sendBindEmailCode(@RequestParam("email") String email) {
        log.info(email + "请求获取验证码");
        // 2. 以邮箱号为KEY，验证码为值，存入Redis，过期时间5分钟
        String code = RandomUtil.randomNumbers(6);
        mailService.sendSimpleMail(email,"Project helper - Auth role",code);
        // 存入 redis
        stringRedisTemplate.opsForValue().set(EMAIL_BIND_USER+email, code, FIVE_MINUTES);
        log.info(email + "生成验证码：" + code);
        // 3. 返回过期时间，方便前端提示多少时间内有效
        return Result.ok(5);
    }


    @PostMapping ("/bind/email")
    public Result<?> bindEmail(@RequestParam("email") String email,
                               @RequestParam("code") String code) {
        long uid = authFunctionality.getUser().getId();
        String key = EMAIL_BIND_USER+email;
        String s = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(s) || !s.equals(code)){
            return Result.err(CAPTCHA_ERR,"invalid check code");
        }
        if (authService.bindEmail(uid,email)){
            return Result.ok(null);
        }else {
            return Result.err(REPEATED_EMAIL,"email has already bound");
        }
    }

    // 修改个人信息
    @PreAuthorize(ROLE_CHECK)
    @PostMapping("/edit/info")
    public Result<?> editInfo(@RequestBody AuthUser authUser) {
        long currId = authFunctionality.getUser().getId();
        if (currId == authUser.getId()){
            authService.editInfo(authUser);
            return Result.ok(null);
        }else {
            return Result.err(AUTH_SESSION_DENIED,"只能操作当前会话的用户！");
        }
    }
}
