package sustech.ooad.mainservice.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import sustech.ooad.mainservice.model.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

}