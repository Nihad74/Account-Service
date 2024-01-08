package com.accountservice.Service;


import com.accountservice.Configuration.UserExistsException;
import com.accountservice.Entity.User;
import com.accountservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> signup(User request) {
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


}
