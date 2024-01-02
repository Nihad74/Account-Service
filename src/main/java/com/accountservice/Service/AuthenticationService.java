package com.accountservice.Service;

import com.accountservice.AccountServiceApplication;
import com.accountservice.Configuration.SecurityConfig;
import com.accountservice.Entity.User;
import com.accountservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    public ResponseEntity<?> signup(User user) {
        user = securityConfig.updatePassword(user);
        userRepository.save(user);

        return ResponseEntity
                .status(200)
                .header("Content-Type", "application/json")
                .body(user);
    }
}
