package sustech.ooad.mainservice.config.secutiry.handler;


import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.InvalidSessionStrategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonInvalidSessionHandler implements InvalidSessionStrategy {


    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8"); // 返回JSON
        response.setStatus(HttpStatus.BAD_REQUEST.value());  // 状态码
        Map<String, Object> result = new HashMap<>(); // 返回结果
        result.put("msg", "当前会话已失效");
        result.put("code", 401);
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
