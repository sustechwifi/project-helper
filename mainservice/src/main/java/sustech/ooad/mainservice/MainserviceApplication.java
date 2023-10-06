package sustech.ooad.mainservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("sustech.ooad.mainservice.mapper")
public class MainserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainserviceApplication.class, args);
    }

}
