package com.accountservice.Repository;

import com.accountservice.Entity.BreachedPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreachedPasswordsRepository extends JpaRepository<BreachedPassword, Long> {

    boolean existsByPassword(String password);

}
