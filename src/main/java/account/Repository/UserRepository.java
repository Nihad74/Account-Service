package account.Repository;

import account.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User>findUserByEmailIgnoreCase(String email);


    @Query("SELECT count(u) FROM User u")
    int size();

    @Modifying
    @Query("delete from User u where u.email = ?1")
    int deleteUserByEmail(String email);

    @Modifying
    @Query("update User u set u.failedLoginAttempts = ?1 where u.email = ?2")
    int updateFailedLoginAttempts(int failedLoginAttempts, String email);
}
