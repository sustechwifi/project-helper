package sustech.ooad.mainservice.config.secutiry;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@RequiredArgsConstructor
@EnableRedisIndexedHttpSession // 开启支持索引保存会话
public class SpringSessionBackedSessionRegistryConfig {

    // @EnableRedisIndexedHttpSession 会自动注册 FindByIndexNameSessionRepository
    // 我们只需要注入进来即可
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    
    // 使用SpringSessionBackedSessionRegistry，会自动替换默认的 SessionRegistry
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }
}
