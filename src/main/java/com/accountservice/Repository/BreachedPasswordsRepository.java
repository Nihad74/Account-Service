package account.Repository;

import account.Entity.BreachedPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreachedPasswordsRepository extends JpaRepository<BreachedPassword, Long> {

    boolean existsByPassword(String password);

}
