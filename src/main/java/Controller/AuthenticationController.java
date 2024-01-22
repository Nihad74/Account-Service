package Controller;

import account.Entity.Password;
import account.Entity.User;
import account.Exception.ErrorResponseUtil;
import account.Exception.PasswordExistsException;
import account.Service.AuthenticationService;
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

import java.util.Set;


@RequestMapping("/api/auth")
@Controller
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @Autowired
    private Set<String> breachedPasswords;




    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User user, BindingResult bindingResult){
        //checks if there are any errors in the request body
        if(bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, error, "/api/auth/signup"));
        }

        String password = user.getPassword();

        if(breachedPasswords.contains(password)){
            throw new PasswordExistsException();
        }


        return authenticationService.signup(user);
    }

    @PostMapping("/changepass")
    public ResponseEntity<?> changePassword(@RequestBody Password new_password){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return authenticationService.changePassword(username, new_password.getNew_password());
    }

}
