package com.accountservice.Controller;


import com.accountservice.Service.AuthenticationService;
import com.accountservice.Entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/auth")
@Controller
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User user){
        return authenticationService.signup(user);
    }

    @PostMapping("/changepass")
    public ResponseEntity<?> changePassword(@RequestParam String name,
                                            @RequestParam String email,
                                            @RequestParam String newPassword){
        return null;
    }

}
