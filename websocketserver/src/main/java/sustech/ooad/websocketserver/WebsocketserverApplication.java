package sustech.ooad.websocketserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("sustech.ooad.websocketserver.mapper")
public class WebsocketserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketserverApplication.class, args);
    }

}
