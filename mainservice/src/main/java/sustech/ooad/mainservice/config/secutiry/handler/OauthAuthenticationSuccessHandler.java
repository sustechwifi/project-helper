package sustech.ooad.mainservice.config.secutiry.handler;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import sustech.ooad.mainservice.model.AuthUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class OauthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    /**
     * 登录成功后直接返回 JSON
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 成功认证的用户信息
     */



    public static LinkedList<Authentication> oauthAuthenticationStack = new LinkedList<>();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8"); // 返回JSON
        response.setStatus(HttpStatus.OK.value());  // 状态码 200
        Map<String, Object> result = new HashMap<>(); // 返回结果
        result.put("msg", "认证成功");
        result.put("code", 0);
        result.put("data", authentication);
        oauthAuthenticationStack.add(authentication);
        response.getWriter().write("<h1>授权登录成功，请关闭当前窗口，点击 NEXT 完成登录</h1>");
    }
}
