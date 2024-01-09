package com.accountservice.Controller;


import com.accountservice.Entity.Password;
import com.accountservice.Service.AuthenticationService;
import com.accountservice.Entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@RequestMapping("/api/auth")
@Controller
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @Autowired
    private Set<String> breachedPasswords;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User user, BindingResult bindingResult) {
        //checks if there are any errors in the request body
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");

        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();

            response.put("message", error);
            response.put("path", "/api/auth/signup");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json")
                    .body(response);
        }
        String password = user.getPassword();
        if (breachedPasswords.contains(password)) {
            response.put("message", "The password is in the hacker's database!");
            response.put("path", "/api/auth/signup");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json")
                    .body(response);
        }


        return authenticationService.signup(user);
    }

    @PostMapping("/changepass")
    public ResponseEntity<?> changePassword(@RequestBody Password new_password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return authenticationService.changePassword(username, new_password.getNew_password());
    }
}
