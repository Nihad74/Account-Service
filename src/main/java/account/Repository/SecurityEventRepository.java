package account.Repository;

import account.Entity.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SecurityEventRepository extends JpaRepository<SecurityEvent, Long> {

    @Query("select se from SecurityEvent se where se.subject = ?1 order by se.date desc limit 5")
    List<SecurityEvent> findLastFiveSecurityEventsByUsername(String username);
}
