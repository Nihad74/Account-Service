package com.accountservice.Service;


import com.accountservice.Entity.UserAdapter;
import com.accountservice.Exception.SamePasswordException;
import com.accountservice.Exception.UserExistsException;
import com.accountservice.Entity.User;
import com.accountservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Set;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Set<String> breachedPasswords;


    @Autowired
    private PaymentService paymentService;

    public ResponseEntity<?> signup(User request) {

        //check if password is breached


        //setup User
        var user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLastName(request.getLastName());

        if(userRepository.findUserByEmailIgnoreCase(user.getEmail()).isPresent()){
            throw new UserExistsException();
        }
        user.setAuthority("ROLE_USER");
        userRepository.save(user);

        return ResponseEntity
                .status(200)
                .header("Content-Type", "application/json")
                .body(user);
    }

    public ResponseEntity<?> changePassword(String username, String newPassword) throws RuntimeException {
        UserAdapter userAdapter = (UserAdapter) paymentService.loadUserByUsername(username);

        if(passwordEncoder.matches(newPassword, userAdapter.getUser().getPassword())){
            throw new SamePasswordException();
        }

        if(newPassword.length()<12){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password length must be 12 chars minimum!");
        } else if (breachedPasswords.contains(newPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
        String newHashedPassword = passwordEncoder.encode(newPassword);
        userAdapter.getUser().setPassword(newHashedPassword);
        userRepository.save(userAdapter.getUser());

        return ResponseEntity
                .status(200)
                .header("Content-Type", "application/json")
                .body(Map.of("email", userAdapter.getUser().getEmail().toLowerCase(), "status",
                        "The password has been updated successfully"));
    }
}
