package sustech.ooad.mainservice.mapper;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, BigDecimal> {

    AuthUser findAuthUserById(BigDecimal uuid);

}